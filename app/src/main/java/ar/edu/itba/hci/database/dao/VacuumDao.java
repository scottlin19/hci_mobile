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
import ar.edu.itba.hci.database.wrapper.VacuumDeviceWrapper;

@Dao
public interface VacuumDao{

    @Query("SELECT * FROM vacuumDevice")
    LiveData<List<VacuumDeviceWrapper>> getAll();


    @Query("DELETE FROM vacuumDevice")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(VacuumDeviceWrapper... devices);

    @Update
    void updateAll(VacuumDeviceWrapper... devices);

    @Delete
    void deleteAll(VacuumDeviceWrapper... devices);

}
