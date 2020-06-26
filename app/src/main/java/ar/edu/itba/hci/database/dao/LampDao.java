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
import ar.edu.itba.hci.database.wrapper.LampDeviceWrapper;

@Dao
public interface LampDao{

    @Query("SELECT * FROM lampDevice")
    LiveData<List<LampDeviceWrapper>> getAll();

    @Query("DELETE FROM lampDevice")
    void deleteAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(LampDeviceWrapper... devices);

    @Update
    void updateAll(LampDeviceWrapper... devices);

    @Delete
    void deleteAll(LampDeviceWrapper... devices);
}
