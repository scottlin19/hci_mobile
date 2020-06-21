package ar.edu.itba.hci.ui.rooms;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.ui.devices.RecyclerViewDeviceAdapter;
import ar.edu.itba.hci.ui.home.HomeFragment;

public class RoomScreen extends AppCompatActivity {

    private Room room;
    private List<Device> deviceList;
    private boolean isChecked = false;

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
            System.out.println("ROOM DEVICES: "+deviceList);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.room_screen_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(room.getName());

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_room_screen);
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
        checkable.setChecked(isChecked);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_toggle_notifications:
                isChecked = !item.isChecked();
                item.setChecked(isChecked);
                if(isChecked){
                    // Notifications disabled
                    item.setIcon(R.drawable.ic_notifications_white_24dp);
                }
                else{
                    // Notifications enabled
                    item.setIcon(R.drawable.ic_baseline_notifications_off_24);
                }
                return true;

            case R.id.action_edit:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}