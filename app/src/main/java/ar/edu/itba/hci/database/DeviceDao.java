package ar.edu.itba.hci.database;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.api.models.Device;

public interface DeviceDao {

    @Query("SELECT * FROM device")
    LiveData<List<Device>> getAll();

    @Insert
    void insertAll(Device... devices);

    @Update
    void updateAll(Device... devices);
}
