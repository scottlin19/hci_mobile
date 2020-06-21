package ar.edu.itba.hci.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.ui.devices.category.Category;
import ar.edu.itba.hci.ui.devices.category.RecyclerViewCategoryAdapter;
import ar.edu.itba.hci.ui.home.HomeFragment;

public class DevicesFragment extends Fragment {

    List<Category> categoryList;
    List<Device> deviceList;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        categoryList = new ArrayList<>();
        deviceList = new ArrayList<>();
        RecyclerView rv = (RecyclerView) root.findViewById(R.id.rv_category);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);

        DevicesViewModel deviceViewModel = new ViewModelProvider(this).get(DevicesViewModel.class);
        deviceViewModel.getDeviceList().observe(getViewLifecycleOwner(),deviceList-> {this.deviceList = deviceList;
            System.out.println("Se cargo device list");
            System.out.println(this.deviceList);});

        deviceViewModel.getCategoryList().observe(getViewLifecycleOwner(), categoryList -> {
            this.categoryList = categoryList;
            System.out.println("Se cargo category list");
            final  RecyclerViewCategoryAdapter adapter = new RecyclerViewCategoryAdapter(getContext(), categoryList,this.deviceList,this);

            rv.setAdapter(adapter);
        });




        Toolbar toolbar = (Toolbar) root.findViewById(R.id.device_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> {



            HomeFragment newFragment = new HomeFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();


            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.nav_host_fragment, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });




        return root;
    }


}