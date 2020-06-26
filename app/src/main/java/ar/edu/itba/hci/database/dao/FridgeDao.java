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
import ar.edu.itba.hci.database.wrapper.FridgeDeviceWrapper;

@Dao
public interface FridgeDao{

    @Query("SELECT * FROM fridgeDevice")
    LiveData<List<FridgeDeviceWrapper>> getAll();

    @Query("DELETE FROM fridgeDevice")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FridgeDeviceWrapper... devices);

    @Update
    void updateAll(FridgeDeviceWrapper... devices);

    @Delete
    void deleteAll(FridgeDeviceWrapper... devices);


}
