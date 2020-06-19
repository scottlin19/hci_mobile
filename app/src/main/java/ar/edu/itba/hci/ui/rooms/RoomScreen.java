package ar.edu.itba.hci.ui.rooms;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Room;

public class RoomScreen extends AppCompatActivity {

    private Room room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        if(getIntent().hasExtra("room")){
            room = (Room) getIntent().getSerializableExtra("room");
        }
        toolBarLayout.setTitle(room.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room_menu, menu);
        return true;
    }

}