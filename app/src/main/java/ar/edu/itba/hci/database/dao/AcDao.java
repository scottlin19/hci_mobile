package ar.edu.itba.hci.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.database.wrapper.AcDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.DeviceWrapper;

@Dao
public interface AcDao {

    @Query("SELECT * FROM acDevice")
    LiveData<List<AcDeviceWrapper>> getAll();


    @Query("DELETE FROM acDevice")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(AcDeviceWrapper... devices);

    @Update
    void updateAll(AcDeviceWrapper... devices);

    @Delete
    void deleteAll(AcDeviceWrapper... devices);


}
