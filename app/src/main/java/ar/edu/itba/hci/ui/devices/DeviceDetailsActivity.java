package ar.edu.itba.hci.ui.devices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.api.models.Room;
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

public class DeviceDetailsActivity extends AppCompatActivity {
    private Device device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        Toolbar toolbar = findViewById(R.id.device_toolbar);
        if(getIntent().hasExtra("device")){

            this.device = getIntent().getParcelableExtra("device");
            if(this.device != null){
                System.out.println(this.device.getName());
                System.out.println("Room: "+ this.device.getRoom());
                System.out.println("Type: " +this.device.getType());
                System.out.println("Meta: "+this.device.getMeta());
            }
        }
        Fragment actions;
       switch(this.device.getType().getName()){
           case "speaker":
               System.out.println("Speaker actions");
               actions = new SpeakerActions();
               break;
           case "ac":
               actions = new AcActions();
               break;
           case "alarm":
               actions = new AlarmActions();
               break;
           case "door":
               actions = new DoorActions();
               break;
           case "faucet":
               actions = new FaucetActions();
               break;
           case "vacuum":
               actions = new VacuumActions();
               break;
           case "refrigerator":
               actions = new FridgeActions();
               break;
           case "lamp":
               actions = new LampActions();
               break;
           case "oven":
               actions = new OvenActions();
               break;
           case "blinds":
               actions = new BlindsActions();
               break;
           default:
               actions = null;
               break;

       }
       getSupportFragmentManager().beginTransaction().replace(R.id.fragment_actions_container,actions,"ACTIONS").commit();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView textView = findViewById(R.id.device_name);
        textView.setText(device.getName());
        TextView textView1 = findViewById(R.id.device_room);
        if(device != null){
            textView1.setText(device.getRoomName());
        }

        ImageView iv = findViewById(R.id.device_icon);
        System.out.println(device.getMeta().getIcon());
        iv.setImageResource(IconAdapter.getIntIcon(device.getMeta().getIcon()));

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device_menu, menu);
        return true;
    }

}