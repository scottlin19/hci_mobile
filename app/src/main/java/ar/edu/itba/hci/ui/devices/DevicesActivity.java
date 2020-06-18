package ar.edu.itba.hci.ui.devices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.ui.devices.category.Category;
import ar.edu.itba.hci.ui.devices.category.RecyclerViewCategoryAdapter;

public class DevicesActivity extends AppCompatActivity {

    List<Category> categoryList;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Speakers",R.drawable.ic_speaker));
        categoryList.add(new Category("TVs",R.drawable.ic_tv));
        categoryList.add(new Category("Lamps",R.drawable.ic_lightbulb_on));
        categoryList.add(new Category("Fridges",R.drawable.ic_fridge));
        categoryList.add(new Category("Ovens",R.drawable.ic_stove));
        categoryList.add(new Category("Vacuums",R.drawable.ic_robot_vacuum));
        categoryList.add(new Category("Faucets",R.drawable.ic_water_pump));
        categoryList.add(new Category("Blinds",R.drawable.ic_blinds));
        categoryList.add(new Category("A/Cs",R.drawable.ic_ac_unit));
        categoryList.add(new Category("Alarms",R.drawable.ic_alarm_light_outline));
        categoryList.add(new Category("Doors",R.drawable.ic_door));

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview_categ);
        RecyclerViewCategoryAdapter adapter = new RecyclerViewCategoryAdapter(this,categoryList);
        rv.setLayoutManager(new GridLayoutManager(this,3));
        rv.setAdapter(adapter);



    }

}