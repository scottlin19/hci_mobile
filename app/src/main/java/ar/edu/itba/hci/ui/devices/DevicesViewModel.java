package ar.edu.itba.hci.ui.devices;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.ui.devices.category.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevicesViewModel extends ViewModel {
    MutableLiveData<List<Category>> categoryList;
    Map<String,Category> categoryMap;
    Map<String,Integer> iconMap;
    public DevicesViewModel(){
        categoryList = new MutableLiveData<List<Category>>();
        categoryMap = new HashMap<>();
        iconMap = new HashMap<>();
        loadIconMap();
        loadCategories();

    }

    private void loadIconMap(){
        if(iconMap.size() == 0){
            iconMap.put("mdi-speaker",R.drawable.ic_speaker);
            iconMap.put("mdi-water-pump",R.drawable.ic_water_pump);
            iconMap.put("mdi-blinds",R.drawable.ic_blinds);
            iconMap.put("mdi-lightbulb-on-outline",R.drawable.ic_lightbulb_on);
            iconMap.put("mdi-stove",R.drawable.ic_stove);
            iconMap.put("mdi-air-conditioner",R.drawable.ic_air_conditioner);
            iconMap.put("mdi-door",R.drawable.ic_door);
            iconMap.put("mdi-alarm-light-outline",R.drawable.ic_alarm_light_outline);
            iconMap.put("mdi-robot-vacuum",R.drawable.ic_robot_vacuum);
            iconMap.put("mdi-fridge-outline",R.drawable.ic_fridge);
        }

    }
    private void loadCategories(){
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Device>> result = response.body();
                    if(result != null) {
                        result.getResult().forEach(device -> {
                            Log.println(Log.DEBUG,"Device",device.toString());
                            DeviceType type = device.getType();
                            Integer icon_id = iconMap.get(device.getMeta().getIcon());
                            if(icon_id == null){
                                icon_id = R.drawable.ic_image;
                            }

                            categoryMap.put(type.getName(),new Category(type.getName(),icon_id));
                        });
                        categoryList.setValue(new ArrayList<Category>(categoryMap.values()));
                    }

                } else {
                    Log.d("ERROR","error");

                }
            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

            }
        });
    }



    public LiveData<List<Category>> getCategoryList(){
        return categoryList;
    }
}
