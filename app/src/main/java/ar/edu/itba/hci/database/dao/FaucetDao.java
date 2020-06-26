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
import ar.edu.itba.hci.database.wrapper.FaucetDeviceWrapper;

@Dao
public interface FaucetDao{

    @Query("SELECT * FROM faucetDevice")
    LiveData<List<FaucetDeviceWrapper>> getAll();

    @Query("DELETE FROM faucetDevice")
    void deleteAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FaucetDeviceWrapper... devices);

    @Update
    void updateAll(FaucetDeviceWrapper... devices);

    @Delete
    void deleteAll(FaucetDeviceWrapper... devices);

}
