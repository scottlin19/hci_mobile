package ar.edu.itba.hci.ui.devices;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
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
import ar.edu.itba.hci.api.models.IconAdapter;
import ar.edu.itba.hci.database.DeviceRepository;
import ar.edu.itba.hci.ui.devices.category.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevicesViewModel extends ViewModel {
    private MutableLiveData<List<Category>> categoryList;
    private MutableLiveData<List<Device>> deviceList;
    private Map<String,Category> categoryMap;
    private DeviceRepository repo;
    public DevicesViewModel(){

        categoryList = new MutableLiveData<>();
        deviceList = new MutableLiveData<>();
        categoryMap = new HashMap<>();


        load();

    }

    private void load(){
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    List<Device> resultList = response.body().getResult();
                    if(resultList != null) {
                        resultList.forEach(device -> {
                            Log.println(Log.DEBUG,"Device",device.toString());
                            DeviceType type = device.getType();
                            Integer icon_id = IconAdapter.getIntIcon(device.getMeta().getIcon());
                            if(icon_id == null){
                                icon_id = R.drawable.ic_image;
                            }
                            if(device.getMeta().getNotifStatus() == null){
                                device.getMeta().setnotifStatus(true);
                                updateDevice(device);
                            }

                            categoryMap.put(type.getName(),new Category(type.getName(),icon_id));
                        });
                        deviceList.setValue(resultList);
                        categoryList.setValue(new ArrayList<>(categoryMap.values()));

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

    private void updateDevice(Device device) {
        ApiClient.getInstance().modifyDevice(device, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    System.out.println("se actualizo el device");
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {

            }
        });
    }


    public LiveData<List<Category>> getCategoryList(){
        return categoryList;
    }

    public LiveData<List<Device>> getDeviceList(){
        return deviceList;
    }
}
