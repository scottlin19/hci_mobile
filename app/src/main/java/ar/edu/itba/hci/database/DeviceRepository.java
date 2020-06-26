package ar.edu.itba.hci.database;
import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ar.edu.itba.hci.api.ApiClient;
import ar.edu.itba.hci.api.Result;
import ar.edu.itba.hci.api.models.Device;


import ar.edu.itba.hci.database.wrapper.AcDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.AlarmDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.BlindsDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.DatabaseAsyncTask;
import ar.edu.itba.hci.database.wrapper.DeviceWrapper;
import ar.edu.itba.hci.database.wrapper.DoorDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.FaucetDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.FridgeDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.LampDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.OvenDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.SpeakerDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.VacuumDeviceWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceRepository {
//    private DeviceDao acDao,alarmDao,faucetDao,blindsDao,fridgeDao,vacuumDao,speakerDao,lampDao,doorDao,ovenDao;
    private DeviceDatabase db;
    private LiveData<List<LampDeviceWrapper>> lampDevices;
    private LiveData<List<SpeakerDeviceWrapper>> speakerDevices;
    private LiveData<List<AcDeviceWrapper>> acDevices;
    private LiveData<List<AlarmDeviceWrapper>> alarmDevices;
    private LiveData<List<FaucetDeviceWrapper>> faucetDevices;
    private LiveData<List<FridgeDeviceWrapper>> fridgeDevices;
    private LiveData<List<VacuumDeviceWrapper>> vacuumDevices;
    private LiveData<List<OvenDeviceWrapper>> ovenDevices;
    private LiveData<List<DoorDeviceWrapper>> doorDevices;
    private LiveData<List<BlindsDeviceWrapper>> blindsDevices;



    public DeviceRepository(Application application){
        db = Room.databaseBuilder(application,DeviceDatabase.class,"device").fallbackToDestructiveMigration().build();
        getAll();
        refresh();
    }


    public void refresh(){
        new DatabaseAsyncTask((t)->{db.clearAllTables();return null;},(t)->{
            fetchDevices();return null;
        });
    }

    private void getAll(){
        lampDevices = db.getAllLamps();
        speakerDevices = db.getAllSpeakers();
        acDevices = db.getAllAcs();
        alarmDevices = db.getAllAlarms();
        fridgeDevices = db.getAllFridges();
        ovenDevices = db.getAllOvens();
        doorDevices = db.getAllDoors();
        blindsDevices = db.getAllBlinds();
        vacuumDevices = db.getAllVacuums();
        faucetDevices = db.getAllFaucets();
    }

    public LiveData<List<SpeakerDeviceWrapper>> getAllSpeakers(){

        return speakerDevices;
    }
    public LiveData<List<LampDeviceWrapper>> getAllLamps(){

        return lampDevices;
    }
    public LiveData<List<AcDeviceWrapper>> getAllAcs(){

        return acDevices;
    }
    public LiveData<List<VacuumDeviceWrapper>> getAllVacuums(){
        return vacuumDevices;
    }

    public LiveData<List<OvenDeviceWrapper>> getAllOvens(){

        return ovenDevices;
    }
    public LiveData<List<DoorDeviceWrapper>> getAllDoors(){

        return doorDevices;
    }
    public LiveData<List<FridgeDeviceWrapper>> getAllFridges(){

        return fridgeDevices;
    }

    public LiveData<List<FaucetDeviceWrapper>> getAllFaucets(){

        return faucetDevices;
    }

    public LiveData<List<BlindsDeviceWrapper>> getAllBlinds(){

        return blindsDevices;
    }

    public LiveData<List<AlarmDeviceWrapper>> getAllAlarms(){

        return alarmDevices;
    }


    public void fetchDevices(){
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if(response.isSuccessful()){
                    List<Device> list = response.body().getResult();
                    System.out.println("DEVICES EN FETCH: "+list.toString());
                    insert(list.toArray(new Device[0]));
                }
            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {
                Log.e("ERROR","Device call failure");
            }
        });
    }



    public  void insert(Device... device) {
     db.getQueryExecutor().execute(
            () -> {

                    db.insertAll(device);

            }
        );
    }

        public void update(Device... device){
            db.getQueryExecutor().execute(
                ()->{db.updateAll(device);}
            );

            for(int i = 0; i < device.length; i++){
                ApiClient.getInstance().modifyDevice(device[i], new Callback<Result<Boolean>>() {
                    @Override
                    public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                        if(response.isSuccessful()){
                            System.out.println("repo updated device in api");
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<Boolean>> call, Throwable t) {

                    }
                });
            }
        }

}
