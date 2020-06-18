package ar.edu.itba.hci.ui.rooms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.R;

public class RoomsActivity extends AppCompatActivity {
    RecyclerView rv;
    List<String> roomNames;
    List<Integer> roomIcons;
    RecyclerViewRoomsAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv = findViewById(R.id.rooms_rv);

        roomIcons = new ArrayList<>();
        roomNames = new ArrayList<>();

        roomIcons.add(R.drawable.ic_baseline_hotel_24);
        roomNames.add("Bedroom");
        roomIcons.add(R.drawable.baby_bottle_outline);
        roomNames.add("Baby's Room");
        roomIcons.add(R.drawable.chef_hat_white);
        roomNames.add("Kitchen");
        roomIcons.add(R.drawable.ic_baseline_directions_car_24);
        roomNames.add("Garage");
        roomIcons.add(R.drawable.ic_baseline_restaurant_24);
        roomNames.add("Dinning Room");
        roomIcons.add(R.drawable.ic_baseline_weekend_24);
        roomNames.add("Living Room");
        roomIcons.add(R.drawable.shower);
        roomNames.add("Bathroom");

        adapter = new RecyclerViewRoomsAdapter(this, roomNames, roomIcons);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(adapter);
    }
}

