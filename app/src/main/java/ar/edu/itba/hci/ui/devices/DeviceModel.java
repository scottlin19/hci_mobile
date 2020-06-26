package ar.edu.itba.hci.ui.devices;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.database.DeviceRepository;
import ar.edu.itba.hci.database.wrapper.AcDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.AlarmDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.BlindsDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.DeviceWrapper;
import ar.edu.itba.hci.database.wrapper.DoorDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.FaucetDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.FridgeDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.LampDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.OvenDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.SpeakerDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.VacuumDeviceWrapper;

public class DeviceModel extends AndroidViewModel {
    private DeviceRepository repo;
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


    public DeviceModel(@NonNull Application application,DeviceRepository repo) {
       super(application);
       this.repo = repo;
        lampDevices = repo.getAllLamps();
        alarmDevices = repo.getAllAlarms();
        acDevices = repo.getAllAcs();
        speakerDevices = repo.getAllSpeakers();
        faucetDevices = repo.getAllFaucets();
        fridgeDevices = repo.getAllFridges();
        vacuumDevices = repo.getAllVacuums();
        ovenDevices = repo.getAllOvens();
        doorDevices = repo.getAllDoors();
        blindsDevices = repo.getAllBlinds();

    }

    public void fetch(){
        repo.fetchDevices();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update(Device device){
        repo.insert(device);
    }

    public void insert(Device device){
        repo.insert(device);
    }

    public LiveData<List<LampDeviceWrapper>> getLampDevices() {
        return lampDevices;
    }

    public LiveData<List<SpeakerDeviceWrapper>> getSpeakerDevices() {
        return speakerDevices;
    }

    public LiveData<List<AcDeviceWrapper>> getAcDevices() {
        return acDevices;
    }

    public LiveData<List<AlarmDeviceWrapper>> getAlarmDevices() {
        return alarmDevices;
    }

    public LiveData<List<FaucetDeviceWrapper>> getFaucetDevices() {
        return faucetDevices;
    }

    public LiveData<List<FridgeDeviceWrapper>> getFridgeDevices() {
        return fridgeDevices;
    }

    public LiveData<List<VacuumDeviceWrapper>> getVacuumDevices() {
        System.out.println(vacuumDevices.getValue());
        return vacuumDevices;
    }

    public LiveData<List<OvenDeviceWrapper>> getOvenDevices() {
        return ovenDevices;
    }

    public LiveData<List<DoorDeviceWrapper>> getDoorDevices() {
        return doorDevices;
    }

    public LiveData<List<BlindsDeviceWrapper>> getBlindsDevices() {
        return blindsDevices;
    }
}
