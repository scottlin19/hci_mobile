package ar.edu.itba.hci.ui.devices;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.api.models.devices.states.AcDeviceState;
import ar.edu.itba.hci.database.wrapper.AcDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.DeviceWrapper;
import ar.edu.itba.hci.database.wrapper.LampDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.WrapperAdapter;
import ar.edu.itba.hci.ui.devices.actions.AcActions;
import ar.edu.itba.hci.ui.devices.category.Category;
import ar.edu.itba.hci.ui.devices.category.RecyclerViewCategoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevicesFragment extends Fragment {

   List<Category> categoryList;
    Map<String,Boolean> categoryMap;
    Fragment instance;





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_devices, container, false);
        categoryList = new ArrayList<>();

        categoryMap = new HashMap<String,Boolean>(){{
            put("ac",false);
            put("alarm",false);
            put("blinds",false);
            put("vacuum",false);
            put("oven",false);
            put("refrigerator",false);
            put("door",false);
            put("lamp",false);
            put("speaker",false);
            put("faucet",false);
        }};
        instance = this;

        RecyclerView rv = root.findViewById(R.id.rv_category);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);

        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if(response.isSuccessful()){
                    List<Device> deviceList = response.body().getResult();
                    deviceList.forEach(device ->{
                        if(categoryMap.get(device.getType().getName()) == false){
                            String categName = device.getType().getName();
                            categoryMap.put(categName,true);
                            Integer icon_id = IconAdapter.getIntIcon(device.getMeta().getIcon());
                            if(icon_id == null){
                                icon_id = R.drawable.ic_image;
                            }
                            categoryList.add(new Category(categName,icon_id));

                        }
                        final RecyclerViewCategoryAdapter adapter = new RecyclerViewCategoryAdapter(getContext(), categoryList,instance);
                        rv.setAdapter(adapter);
                    });

                }

            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

            }
        });


        DeviceModel deviceModel = new ViewModelProvider(this, new DeviceModelFactory(getActivity().getApplication())).get(DeviceModel.class);
        System.out.println("DEVICES FRAGMENT");

       /* deviceModel.getAcDevices().observe(getViewLifecycleOwner(), acDeviceWrappers -> {
            System.out.println(" ---------AC WRAPPER OBSERVER FUERA DEL  IF---------");
            if(!acDeviceWrappers.isEmpty() && !checkMap(acDeviceWrappers.get(0).getDeviceType().getName())){
                System.out.println("----------AC WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = acDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = acDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                categoryMap.put(type,true);

                adapter.notifyDataSetChanged();

            }
        });
        deviceModel.getAlarmDevices().observe(getViewLifecycleOwner(), alarmDeviceWrappers -> {
            System.out.println("----------ALARM WRAPPER OBSERVER FUERA DEL  IF---------");
            if(!alarmDeviceWrappers.isEmpty() && !checkMap(alarmDeviceWrappers.get(0).getDeviceType().getName())){
                System.out.println("----------ALARM WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = alarmDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = alarmDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                categoryMap.put(type,true);
                adapter.notifyDataSetChanged();
            }
        });
        deviceModel.getBlindsDevices().observe(getViewLifecycleOwner(), blindsDeviceWrappers -> {
            System.out.println("----------BLIND WRAPPER OBSERVER FUERA DEL  IF---------");
            if(!blindsDeviceWrappers.isEmpty() && !checkMap(blindsDeviceWrappers.get(0).getDeviceType().getName())){
                System.out.println("----------BLIND WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = blindsDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = blindsDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                categoryMap.put(type,true);
                adapter.notifyDataSetChanged();
            }
        });
        deviceModel.getDoorDevices().observe(getViewLifecycleOwner(), doorDeviceWrappers -> {
            System.out.println("----------DOOR WRAPPER OBSERVER FUERA DEL  IF---------");
            if(!doorDeviceWrappers.isEmpty() && !checkMap(doorDeviceWrappers.get(0).getDeviceType().getName())){
                System.out.println("----------DOOR WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = doorDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = doorDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                adapter.notifyDataSetChanged();
            }
        });
        deviceModel.getFaucetDevices().observe(getViewLifecycleOwner(), faucetDeviceWrappers -> {
            System.out.println("---------- FAUCET WRAPPER OBSERVER FUERA DEL  IF---------");
            if(!faucetDeviceWrappers.isEmpty() && !checkMap(faucetDeviceWrappers.get(0).getDeviceType().getName())){

                System.out.println("----------FAUCET WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = faucetDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = faucetDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                adapter.notifyDataSetChanged();
            }
        });
        deviceModel.getFridgeDevices().observe(getViewLifecycleOwner(), fridgeDeviceWrappers -> {
            System.out.println("----------FRIDGE WRAPPER OBSERVER FUERA DEL  IF---------");
            if(!fridgeDeviceWrappers.isEmpty() && !checkMap(fridgeDeviceWrappers.get(0).getDeviceType().getName())){
                System.out.println("----------FRIDGE WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = fridgeDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = fridgeDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                adapter.notifyDataSetChanged();
            }
        });
        deviceModel.getLampDevices().observe(getViewLifecycleOwner(), lampDeviceWrappers -> {
            System.out.println("----------LAMP WRAPPER OBSERVER FUERA DEL  IF---------");
            System.out.println(lampDeviceWrappers);
            if(!lampDeviceWrappers.isEmpty() && !checkMap(lampDeviceWrappers.get(0).getDeviceType().getName())){
                System.out.println("----------LAMP WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = lampDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = lampDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                adapter.notifyDataSetChanged();
                System.out.println(categoryList.getValue());
            }

        });

        deviceModel.getOvenDevices().observe(getViewLifecycleOwner(), ovenDeviceWrappers -> {
            System.out.println("----------"+" oven"+" WRAPPER OBSERVER FUERA DEL  IF---------");
            if(!ovenDeviceWrappers.isEmpty() && !checkMap(ovenDeviceWrappers.get(0).getDeviceType().getName())){
                System.out.println("----------"+" oven"+" WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = ovenDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = ovenDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                adapter.notifyDataSetChanged();


            }
        });
        deviceModel.getSpeakerDevices().observe(getViewLifecycleOwner(), speakerDeviceWrappers -> {
            System.out.println("----------"+" speaker"+" WRAPPER OBSERVER FUERA DEL  IF---------");
            if(!speakerDeviceWrappers.isEmpty() && !checkMap(speakerDeviceWrappers.get(0).getDeviceType().getName())){
                System.out.println("----------"+" speaker"+" WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = speakerDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = speakerDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                adapter.notifyDataSetChanged();


            }
        });
        deviceModel.getVacuumDevices().observe(getViewLifecycleOwner(), vacuumDeviceWrappers -> {
            System.out.println("----------VACUUM WRAPPER OBSERVER FUERA DEL  IF---------");
            if(!vacuumDeviceWrappers.isEmpty() && !checkMap(vacuumDeviceWrappers.get(0).getDeviceType().getName())){
                System.out.println("----------VACUUM WRAPPER OBSERVER DENTRO DEL IF---------");
                DeviceWrapper deviceWrapper = vacuumDeviceWrappers.get(0);
                Integer icon_id = IconAdapter.getIntIcon(deviceWrapper.getDeviceMeta().getIcon());
                if(icon_id == null){
                    icon_id = R.drawable.ic_image;
                }
                String type = vacuumDeviceWrappers.get(0).getDeviceType().getName();
                categoryList.getValue().add(new Category(type,icon_id));
                adapter.notifyDataSetChanged();


            }
        });
*/


        Toolbar toolbar = root.findViewById(R.id.device_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> {

            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            fm.popBackStack();
//
        });



        deviceModel.fetch();


        return root;
    }

    private synchronized  boolean checkMap(String name){
        return categoryMap.get(name);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}