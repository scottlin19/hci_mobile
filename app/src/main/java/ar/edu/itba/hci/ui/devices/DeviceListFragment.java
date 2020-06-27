package ar.edu.itba.hci.ui.devices;

import android.content.Intent;
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


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.ui.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceListFragment extends Fragment {
    private List<Device> deviceList;
    private String categoryName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_device_list, container, false);


        Bundle bundle = getArguments();
        categoryName = bundle.getString("category");
        System.out.println(categoryName);

        RecyclerView rv = root.findViewById(R.id.rv_device_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);
        deviceList = new ArrayList<>();
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if(response.isSuccessful()){

                    response.body().getResult().forEach(device->{

                        if((device.getMeta().getNotif_status()) == null){
                            device.getMeta().setNotif_status(true);
                            ApiClient.getInstance().modifyDevice(device, new Callback<Result<Boolean>>() {
                                @Override
                                public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                                    if(response.isSuccessful()){
                                        deviceList.add(device);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Result<Boolean>> call, Throwable t) {

                                }
                            });
                        }else{
                            deviceList.add(device);
                        }


                    });
                    if(deviceList != null){
                        deviceList = deviceList.stream().filter(device -> device.getType().getName().equals(categoryName)).collect(Collectors.toList());
                    }

                    System.out.println(deviceList);
                    RecyclerViewDeviceAdapter adapter = new RecyclerViewDeviceAdapter(getContext(),deviceList);
                    rv.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

            }
        });


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
