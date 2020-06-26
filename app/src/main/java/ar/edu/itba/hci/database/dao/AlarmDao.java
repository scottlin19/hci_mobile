package ar.edu.itba.hci.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.itba.hci.database.wrapper.AlarmDeviceWrapper;


@Dao
public interface AlarmDao{
    @Query("SELECT * FROM alarmDevice")
    LiveData<List<AlarmDeviceWrapper>> getAll();

    @Query("DELETE FROM alarmDevice")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(AlarmDeviceWrapper... devices);

    @Update
    void updateAll(AlarmDeviceWrapper... devices);

    @Delete
    void deleteAll(AlarmDeviceWrapper... devices);
}
