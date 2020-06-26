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
import ar.edu.itba.hci.database.wrapper.OvenDeviceWrapper;

@Dao
public interface OvenDao {

    @Query("SELECT * FROM ovenDevice")
    LiveData<List<OvenDeviceWrapper>> getAll();

    @Query("DELETE FROM ovenDevice")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(OvenDeviceWrapper... devices);

    @Update
    void updateAll(OvenDeviceWrapper... devices);

    @Delete
    void deleteAll(OvenDeviceWrapper... devices);


}
