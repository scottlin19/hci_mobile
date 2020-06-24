package ar.edu.itba.hci.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.ui.Utility;

public class DeviceListFragment extends Fragment {
    private List<Device> deviceList;
    private String categoryName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_device_list, container, false);


        Bundle bundle = getArguments();
        deviceList = bundle.getParcelableArrayList("devices");
        System.out.println(deviceList);
        categoryName = bundle.getString("category");


        RecyclerView rv = root.findViewById(R.id.rv_device_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);
        RecyclerViewDeviceAdapter adapter = new RecyclerViewDeviceAdapter(getContext(),deviceList);
        rv.setAdapter(adapter);

        Toolbar toolbar = root.findViewById(R.id.device_list_toolbar);

        toolbar.setTitle(Utility.capitalizeFirstLetter(categoryName));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> {

            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            fm.popBackStack();
        });

        return root;
    }


}
