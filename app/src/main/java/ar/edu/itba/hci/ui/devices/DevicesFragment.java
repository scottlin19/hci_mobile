package ar.edu.itba.hci.ui.devices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.ui.devices.category.Category;
import ar.edu.itba.hci.ui.devices.category.RecyclerViewCategoryAdapter;
import ar.edu.itba.hci.ui.home.HomeFragment;

public class DevicesFragment extends Fragment {

    List<Category> categoryList;

    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_devices, container, false);
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

        RecyclerView rv = (RecyclerView) root.findViewById(R.id.rv_category);

            RecyclerViewCategoryAdapter adapter = new RecyclerViewCategoryAdapter(getContext(),categoryList);
            rv.setLayoutManager(new GridLayoutManager(getContext(),3));
            rv.setAdapter(adapter);
            Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                HomeFragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();


                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });




        return root;
    }


}