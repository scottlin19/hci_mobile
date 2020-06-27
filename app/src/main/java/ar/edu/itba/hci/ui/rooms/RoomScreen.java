package ar.edu.itba.hci.ui.rooms;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.ui.devices.RecyclerViewDeviceAdapter;
import ar.edu.itba.hci.ui.home.HomeFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomScreen extends AppCompatActivity {

    private Room room;
    private List<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_screen);


        if(getIntent().hasExtra("room")){
            this.room = getIntent().getParcelableExtra("room");
            System.out.println(room.getMeta().getIcon());
        }
        if(getIntent().hasExtra("devices")){
           deviceList = getIntent().getParcelableArrayListExtra("devices");
           deviceList.forEach(d -> d.setRoom(room));
           System.out.println("ROOM DEVICES: "+deviceList);
        }


        Toolbar toolbar = findViewById(R.id.room_screen_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(room.getName());

        RecyclerView rv = findViewById(R.id.rv_room_screen);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);
        RecyclerViewDeviceAdapter adapter = new RecyclerViewDeviceAdapter(this,deviceList);
        rv.setAdapter(adapter);
//        ImageView iv = findViewById(R.id.room_icon);
//        iv.setImageResource(IconAdapter.getIntIcon(room.getMeta().getIcon()));
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room_menu, menu);
        MenuItem checkable = menu.findItem(R.id.action_toggle_notifications);
        checkable.setChecked(true);
        for(Device dev : deviceList) {
            if (!dev.getMeta().getNotifStatus()) {
                checkable.setChecked(false);
                checkable.setIcon(R.drawable.ic_outline_notifications_off_24);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle_notifications:
                item.setChecked(!item.isChecked());
                if(item.isChecked()) item.setIcon(R.drawable.ic_notifications_white_24dp);
                else item.setIcon(R.drawable.ic_outline_notifications_off_24);

                for(Device dev : deviceList) {
                    if(dev.getMeta().getNotifStatus() != item.isChecked()) {
                        dev.getMeta().setnotifStatus(item.isChecked());
                        updateNotif(dev);
                    }
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateNotif(Device device) {
        ApiClient.getInstance().modifyDevice(device, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(!response.isSuccessful()) {
                    Log.e("Update notifications", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                Log.e("Update notifications failure", t.getLocalizedMessage());
            }
        });
    }
}