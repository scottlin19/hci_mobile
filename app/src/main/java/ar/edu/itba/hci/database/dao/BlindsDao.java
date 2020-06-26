package ar.edu.itba.hci.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.database.wrapper.BlindsDeviceWrapper;
import ar.edu.itba.hci.database.wrapper.DeviceWrapper;

@Dao
public interface BlindsDao{

    @Query("SELECT * FROM blindsDevice")
    LiveData<List<BlindsDeviceWrapper>> getAll();

    @Query("DELETE FROM blindsDevice")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(BlindsDeviceWrapper... devices);

    @Update
    void updateAll(BlindsDeviceWrapper... devices);

    @Delete
    void deleteAll(BlindsDeviceWrapper... devices);


}
