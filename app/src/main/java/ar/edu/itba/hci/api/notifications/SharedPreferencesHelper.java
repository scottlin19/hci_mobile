package ar.edu.itba.hci.api.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedPreferencesHelper {


    public static void saveToSharedPreferences(Context context,List<Device> deviceList){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Set<String> devicesSet = new HashSet<>();

        deviceList.forEach(d -> {
            if(d.getMeta().getNotifStatus())
                devicesSet.add(gson.toJson(d));
        });

        editor.putStringSet("devices", devicesSet);
        editor.apply();
    }
    public static void refreshSavedPreferences(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    List<Device> devicesList = new ArrayList<>();

                    System.out.println("DEVICES "+response.body().getResult());
                    response.body().getResult().forEach(device->{
                        Boolean notif;
                        if((notif = device.getMeta().getNotif_status()) == null){
                            device.getMeta().setNotif_status(true);
                            ApiClient.getInstance().modifyDevice(device, new Callback<Result<Boolean>>() {
                                @Override
                                public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                                    if(response.isSuccessful()){
                                       devicesList.add(device);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Result<Boolean>> call, Throwable t) {

                                }
                            });
                        }else if(notif == true){
                            devicesList.add(device);
                        }
                    });



                    Set<String> devicesSet = new HashSet<>();

                    devicesList.forEach(d -> {
                        devicesSet.add(gson.toJson(d));
                    });

                    editor.putStringSet("devices", devicesSet);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {
                Log.e("Device Detail Error", "Get devices callback failure.");
            }
        });
    }


}
