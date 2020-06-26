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
import ar.edu.itba.hci.database.wrapper.SpeakerDeviceWrapper;

@Dao
public interface SpeakerDao{
    @Query("SELECT * FROM speakerDevice")
    LiveData<List<SpeakerDeviceWrapper>> getAll();

    @Query("DELETE FROM speakerDevice")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SpeakerDeviceWrapper... devices);

    @Update
    void updateAll(SpeakerDeviceWrapper... devices);

    @Delete
    void deleteAll(SpeakerDeviceWrapper... devices);
}
