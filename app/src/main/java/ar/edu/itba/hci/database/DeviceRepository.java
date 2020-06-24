package ar.edu.itba.hci.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

import ar.edu.itba.hci.MainActivity;
import ar.edu.itba.hci.api.models.Device;

public class DeviceRepository {
    private DeviceDao dao;
    private DeviceDatabase db;
    private LiveData<List<Device>> devices;

    public DeviceRepository(Application application){
        db = Room.databaseBuilder(application,DeviceDatabase.class,"device").build();
        dao = db.timeDao();
        devices = dao.getAll();
    }

    LiveData<List<Device>> getAllDevices(){
        return devices;
    }



    public  void insert(Device device) {
     db.getQueryExecutor().execute(
            () -> {
                dao.insertAll(device);
            }
        );
    }
        public void update(Device device){
            db.getQueryExecutor().execute(
                ()->{dao.updateAll(device);}
            );
        }
}
