package ar.edu.itba.hci.ui.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ar.edu.itba.hci.MainActivity;
import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.api.notifications.NotificationBroadcastReceiver;
import ar.edu.itba.hci.api.notifications.SharedPreferencesHelper;
import ar.edu.itba.hci.ui.devices.actions.AcActions;
import ar.edu.itba.hci.ui.devices.actions.AlarmActions;
import ar.edu.itba.hci.ui.devices.actions.BlindsActions;
import ar.edu.itba.hci.ui.devices.actions.DoorActions;
import ar.edu.itba.hci.ui.devices.actions.FaucetActions;
import ar.edu.itba.hci.ui.devices.actions.FridgeActions;
import ar.edu.itba.hci.ui.devices.actions.LampActions;
import ar.edu.itba.hci.ui.devices.actions.OvenActions;
import ar.edu.itba.hci.ui.devices.actions.SpeakerActions;
import ar.edu.itba.hci.ui.devices.actions.VacuumActions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceDetailsActivity extends AppCompatActivity {
    private DeviceDetailsViewModel deviceDetailsViewModel;
    private Device device;
    private LiveData<Device> liveDevice;
    MenuItem fav_btn;
    MenuItem noti_btn;
    private BroadcastReceiver broadcastReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);


        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private void registerReceiver(){
        if(broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                        deviceDetailsViewModel.loadDevice();
                        abortBroadcast();
                }
            };

        }
        IntentFilter filter = new IntentFilter(NotificationBroadcastReceiver.DEVICE_NOTIFICATION);
        filter.setPriority(2);
        registerReceiver(broadcastReceiver, filter);


    }
    @Override
    public void onBackPressed() {
        if(this.isTaskRoot())
            startActivity(new Intent(this , MainActivity.class));
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        SharedPreferencesHelper.refreshSavedPreferences(getApplicationContext());


    }


    private void init(){
        Toolbar toolbar = findViewById(R.id.device_details_toolbar);
        TextView textView = findViewById(R.id.device_name);
        TextView textView1 = findViewById(R.id.device_room);
        ImageView iv = findViewById(R.id.device_icon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(getIntent().hasExtra("device")){

            String deviceId = getIntent().getStringExtra("device");
            System.out.println("device id:"+ deviceId);
            deviceDetailsViewModel = new ViewModelProvider(this, new DeviceDetailsViewModel(deviceId)).get(DeviceDetailsViewModel.class);
            liveDevice = deviceDetailsViewModel.getDevice();
            liveDevice.observe(this, device -> {
                this.device = device;
                Fragment actions;
                switch (device.getType().getName()){
                    case "speaker":
                        actions = SpeakerActions.newInstance(device);
                        break;
                    case "ac":
                        actions = AcActions.newInstance(device);
                        break;
                    case "alarm":
                        actions = AlarmActions.newInstance(device);
                        break;
                    case "door":
                        actions = DoorActions.newInstance(device);
                        break;
                    case "faucet":
                        actions =  FaucetActions.newInstance(device);
                        break;
                    case "vacuum":
                        actions = VacuumActions.newInstance(device);
                        break;
                    case "refrigerator":
                        actions = FridgeActions.newInstance(device);
                        break;
                    case "lamp":
                        actions = LampActions.newInstance(device);
                        break;
                    case "oven":
                        actions = OvenActions.newInstance(device);
                        break;
                    case "blinds":
                        actions = BlindsActions.newInstance(device);
                        break;
                    default:
                        actions = null;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_actions_container,actions,"ACTIONS").commit();
                textView.setText(device.getName());
                String room = device.getRoomName();
                if(room != null) textView1.setText(device.getRoomName());
                else textView1.setText(R.string.no_room);
                iv.setImageResource(IconAdapter.getIntIcon(device.getMeta().getIcon()));

                System.out.println(device.getName());
                System.out.println("Room: "+ device.getRoom());
                System.out.println("Type: " +device.getType());
                System.out.println("Meta: "+device.getMeta());

            });
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        fav_btn = menu.findItem(R.id.toggle_favorite);
        noti_btn = menu.findItem(R.id.action_toggle_notifications);

        liveDevice.observe(this, device -> {
            changeFavIcon(device);
            fav_btn.setChecked(device.getMeta().getFavorite());
            changeNotificationIcon(device);
            noti_btn.setChecked(device.getMeta().getNotif_status());
        });


        return true;
    }

    private void changeFavIcon(Device device) {
        if(device.getMeta().getFavorite()){
            fav_btn.setIcon(R.drawable.ic_baseline_favorite_24);
        }
        else{
            fav_btn.setIcon(R.drawable.ic_baseline_favorite_border_24);
        }
    }

    private void changeNotificationIcon(Device device) {
        if(device.getMeta().getNotifStatus()){
            noti_btn.setIcon(R.drawable.ic_notifications_white_24dp);
        }
        else{
            noti_btn.setIcon(R.drawable.ic_outline_notifications_off_24);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggle_favorite:
                item.setChecked(!device.getMeta().getFavorite());
                device.getMeta().setFavorite(item.isChecked());
                break;

            case R.id.action_toggle_notifications:
                //noti_isChecked = !item.isChecked();

                item.setChecked(!device.getMeta().getNotifStatus());
                device.getMeta().setnotifStatus(item.isChecked());
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        ApiClient.getInstance().modifyDevice(device, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    changeNotificationIcon(device);
                    changeFavIcon(device);
                    System.out.println(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {

                Log.e("TOGGLE ERROR", "error on toggle");
                Toast.makeText(getBaseContext(), "error on toggle", Toast.LENGTH_SHORT).show();
            }
        });

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageButton sstHelp = findViewById(R.id.sst_help_btn);
        sstHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String device_type = getStringResourceByName(device.getType().getName());
                String device_msg = getStringResourceByName(device.getType().getName().concat("SSTCommands"));
                new MaterialAlertDialogBuilder(DeviceDetailsActivity.this)
                        .setTitle(getResources().getString(R.string.sstDialogTitle,device_type))
                        .setMessage(device_msg)
                        .setPositiveButton("Ok",null)
                        .show();
            }
        });

    }

    private String getStringResourceByName(String aString) {
        int resId = getResources().getIdentifier(aString, "string", this.getPackageName());
        return getString(resId);
    }
}