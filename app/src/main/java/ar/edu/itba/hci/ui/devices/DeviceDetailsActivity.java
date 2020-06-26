package ar.edu.itba.hci.ui.devices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.api.notifications.NotificationBroadcastReceiver;
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
    private Device device;
    MenuItem fav_btn;
    MenuItem noti_btn;
    private BroadcastReceiver broadcastReceiver;
    private boolean wasNotifChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = "Device updated";
                Toast.makeText(DeviceDetailsActivity.this,message, Toast.LENGTH_LONG).show();
                abortBroadcast();
            }
        };
        IntentFilter filter = new IntentFilter(NotificationBroadcastReceiver.DEVICE_NOTIFICATION);
        filter.setPriority(2);
        registerReceiver(broadcastReceiver,filter);



    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        if(noti_btn.isChecked()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
                @Override
                public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                    if (response.isSuccessful()) {
                        List<Device> devicesList = response.body().getResult();

                        Set<String> devicesSet = new HashSet<>();

                        devicesList.forEach(d -> {
                            devicesSet.add(gson.toJson(d));

                        });

                        editor.putStringSet("devices", devicesSet);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call<Result<List<Device>>> call, Throwable t) {
                    Log.e("Device Detail Error", "Get devices callback failure.");
                }
            });
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.device_toolbar);


        if(getIntent().hasExtra("device")){

            String deviceId = getIntent().getStringExtra("device");
            System.out.println("device id:"+ deviceId);
            ApiClient.getInstance().getDevice(deviceId, new Callback<Result<Device>>() {
                @Override
                public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                    if(response.isSuccessful()){
                        device = response.body().getResult();

                        Fragment actions;
                        switch (device.getType().getName()){
                            case "speaker":
                                System.out.println("Speaker actions");
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
                        TextView textView = findViewById(R.id.device_name);
                        textView.setText(device.getName());
                        TextView textView1 = findViewById(R.id.device_room);
                        if(device != null){
                            String room = device.getRoomName();
                            if(room != null)
                                textView1.setText(device.getRoomName());
                            else textView1.setText(R.string.no_room);
                        }

                        ImageView iv = findViewById(R.id.device_icon);

                        iv.setImageResource(IconAdapter.getIntIcon(device.getMeta().getIcon()));
                    }
                }

                @Override
                public void onFailure(Call<Result<Device>> call, Throwable t) {

                }
            });

//            this.device = getIntent().getParcelableExtra("device");
            if(this.device != null){
                System.out.println(this.device.getName());
                System.out.println("Room: "+ this.device.getRoom());
                System.out.println("Type: " +this.device.getType());
                System.out.println("Meta: "+this.device.getMeta());
            }
        }



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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
        while(device == null){
            System.out.println("while  XD ");
        }

            changeFavIcon();

            fav_btn.setChecked(device.getMeta().getFavorite());
            changeNotificationIcon();
        wasNotifChecked = device.getMeta().getNotif_status();
            noti_btn.setChecked(wasNotifChecked);


        return true;
    }

    private void changeFavIcon() {
        if(device.getMeta().getFavorite()){
            fav_btn.setIcon(R.drawable.ic_baseline_favorite_24);
        }
        else{
            fav_btn.setIcon(R.drawable.ic_baseline_favorite_border_24);
        }
    }

    private void changeNotificationIcon() {
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
                return false;
        }

        ApiClient.getInstance().modifyDevice(device, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    changeNotificationIcon();
                    changeFavIcon();
                    System.out.println(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {

                Log.e("TOGGLE ERROR", "error on toggle");
                Toast.makeText(getBaseContext(), "error on toggle", Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }


}