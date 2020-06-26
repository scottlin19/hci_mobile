package ar.edu.itba.hci.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.database.wrapper.DeviceWrapper;
import ar.edu.itba.hci.database.wrapper.DoorDeviceWrapper;

@Dao
public interface DoorDao{

    @Query("SELECT * FROM doorDevice")
    LiveData<List<DoorDeviceWrapper>> getAll();

    @Query("DELETE FROM doorDevice")
    void deleteAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(DoorDeviceWrapper... devices);

    @Update
    void updateAll(DoorDeviceWrapper... devices);

    @Delete
    void deleteAll(DoorDeviceWrapper... devices);


}
