package ar.edu.itba.hci.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.api.models.Device;

import ar.edu.itba.hci.api.models.devices.states.DeviceState;

@Dao
public interface DeviceDao<T extends DeviceState> {

    @Query("SELECT * FROM device")
    LiveData<List<Device<T>>>getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Device<T>... devices);

    @Update
    void updateAll(Device<T>... devices);

    @Delete
    void deleteAll(Device<T>... devices);
}
