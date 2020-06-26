package ar.edu.itba.hci.database;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.util.List;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.database.dao.AcDao;
import ar.edu.itba.hci.database.dao.AlarmDao;
import ar.edu.itba.hci.database.dao.BlindsDao;
import ar.edu.itba.hci.database.dao.DoorDao;
import ar.edu.itba.hci.database.dao.FaucetDao;
import ar.edu.itba.hci.database.dao.FridgeDao;
import ar.edu.itba.hci.database.dao.LampDao;
import ar.edu.itba.hci.database.dao.OvenDao;
import ar.edu.itba.hci.database.dao.SpeakerDao;
import ar.edu.itba.hci.database.dao.VacuumDao;
import ar.edu.itba.hci.database.wrapper.AcDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.AlarmDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.BlindsDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.DoorDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.FaucetDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.FridgeDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.LampDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.OvenDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.SpeakerDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.VacuumDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.WrapperAdapter;

@Database(entities = {AcDeviceWrapper.class, AlarmDeviceWrapper.class, BlindsDeviceWrapper.class, OvenDeviceWrapper.class, VacuumDeviceWrapper.class,
        SpeakerDeviceWrapper.class, FaucetDeviceWrapper.class, LampDeviceWrapper.class, DoorDeviceWrapper.class, FridgeDeviceWrapper.class},version = 1,exportSchema = false)
public abstract class DeviceDatabase extends RoomDatabase {
    public abstract AcDao acDao();
    public abstract AlarmDao alarmDao();
    public abstract BlindsDao blindsDao();
    public abstract DoorDao doorDao();
    public abstract FaucetDao faucetDao();
    public abstract SpeakerDao speakerDao();
    public abstract VacuumDao vacuumDao();
    public abstract OvenDao ovenDao();
    public abstract LampDao lampDao();
    public abstract FridgeDao fridgeDao();


    public LiveData<List<SpeakerDeviceWrapper>> getAllSpeakers(){

        return speakerDao().getAll();
    }
    public LiveData<List<LampDeviceWrapper>> getAllLamps(){

        return lampDao().getAll();
    }
    public LiveData<List<AcDeviceWrapper>> getAllAcs(){

        return acDao().getAll();
    }
    public LiveData<List<VacuumDeviceWrapper>> getAllVacuums(){
        return vacuumDao().getAll();
    }

    public LiveData<List<OvenDeviceWrapper>> getAllOvens(){

        return ovenDao().getAll();
    }
    public LiveData<List<DoorDeviceWrapper>> getAllDoors(){

        return doorDao().getAll();
    }
        public LiveData<List<FridgeDeviceWrapper>> getAllFridges(){

        return fridgeDao().getAll();
    }

    public LiveData<List<FaucetDeviceWrapper>> getAllFaucets(){

        return faucetDao().getAll();
    }

    public LiveData<List<BlindsDeviceWrapper>> getAllBlinds(){

        return blindsDao().getAll();
    }

    public LiveData<List<AlarmDeviceWrapper>> getAllAlarms(){

        return alarmDao().getAll();
    }

    public void clearAllTables(){

        alarmDao().deleteAll();

        acDao().deleteAll();

        alarmDao().deleteAll();
        blindsDao().deleteAll();
        doorDao().deleteAll();
        faucetDao().deleteAll();
        speakerDao().deleteAll();
        vacuumDao().deleteAll();
        ovenDao().deleteAll();
        lampDao().deleteAll();
        fridgeDao().deleteAll();
    }

    public void insertAll(Device... device){
        for(int i = 0; i < device.length; i++){
            switch(device[i].getType().getName()){
                case "speaker":
                    speakerDao().insertAll(WrapperAdapter.fromDeviceToSpeakerWrapper(device[i]));
                    break;
                case "faucet":
                    faucetDao().insertAll(WrapperAdapter.fromDeviceToFaucetWrapper(device[i]));
                    break;
                case "blinds":
                    blindsDao().insertAll(WrapperAdapter.fromDeviceToBlindsWrapper(device[i]));
                    break;
                case "lamp":
                    System.out.println("INSERTANDO LAMP");
                    lampDao().insertAll(WrapperAdapter.fromDeviceToLampWrapper(device[i]));
                    break;
                case "oven":
                    ovenDao().insertAll(WrapperAdapter.fromDeviceToOvenWrapper(device[i]));
                    break;
                case "ac":
                    acDao().insertAll(WrapperAdapter.fromDeviceToAcWrapper(device[i]));
                    break;
                case "door":
                    doorDao().insertAll(WrapperAdapter.fromDeviceToDoorWrapper(device[i]));
                    break;
                case "alarm":
                    alarmDao().insertAll(WrapperAdapter.fromDeviceToAlarmWrapper(device[i]));
                    break;
                case "vacuum":
                    vacuumDao().insertAll(WrapperAdapter.fromDeviceToVacuumWrapper(device[i]));
                    break;
                case "refrigerator":
                    fridgeDao().insertAll(WrapperAdapter.fromDeviceToFridgeWrapper(device[i]));
                    break;
            }
        }
    }

    public void updateAll(Device[] device) {
        for(int i = 0; i < device.length; i++){
            switch(device[i].getType().getName()){
                case "speaker":
                    speakerDao().updateAll(WrapperAdapter.fromDeviceToSpeakerWrapper(device[i]));
                    break;
                case "faucet":
                    faucetDao().updateAll(WrapperAdapter.fromDeviceToFaucetWrapper(device[i]));
                    break;
                case "blinds":
                    blindsDao().updateAll(WrapperAdapter.fromDeviceToBlindsWrapper(device[i]));
                    break;
                case "lamp":
                    lampDao().updateAll(WrapperAdapter.fromDeviceToLampWrapper(device[i]));
                    break;
                case "oven":
                    ovenDao().updateAll(WrapperAdapter.fromDeviceToOvenWrapper(device[i]));
                    break;
                case "ac":
                    acDao().updateAll(WrapperAdapter.fromDeviceToAcWrapper(device[i]));
                    break;
                case "door":
                    doorDao().updateAll(WrapperAdapter.fromDeviceToDoorWrapper(device[i]));
                    break;
                case "alarm":
                    alarmDao().updateAll(WrapperAdapter.fromDeviceToAlarmWrapper(device[i]));
                    break;
                case "vacuum":
                    vacuumDao().updateAll(WrapperAdapter.fromDeviceToVacuumWrapper(device[i]));
                    break;
                case "refrigerator":
                    fridgeDao().updateAll(WrapperAdapter.fromDeviceToFridgeWrapper(device[i]));
                    break;
            }
        }
    }
}
