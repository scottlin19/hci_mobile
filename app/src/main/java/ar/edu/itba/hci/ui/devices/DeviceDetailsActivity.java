package ar.edu.itba.hci.ui.devices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.api.models.Room;

public class DeviceDetailsActivity extends AppCompatActivity {
    private Device device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        Toolbar toolbar = findViewById(R.id.device_toolbar);
        if(getIntent().hasExtra("device")){
//            this.device = (Device) getIntent().getSerializableExtra("device");
            this.device = getIntent().getParcelableExtra("device");
            if(this.device != null){
                System.out.println(this.device.getName());
                System.out.println("Type: " +this.device.getType());
                System.out.println("Meta: "+this.device.getMeta());
            }
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = findViewById(R.id.device_name);
        textView.setText(device.getName());
        ImageView iv = findViewById(R.id.device_icon);
        System.out.println(device.getMeta().getIcon());
        iv.setImageResource(IconAdapter.getIntIcon(device.getMeta().getIcon()));

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room_menu, menu);
        return true;
    }

}