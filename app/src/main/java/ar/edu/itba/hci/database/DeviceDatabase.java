package ar.edu.itba.hci.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ar.edu.itba.hci.api.models.Device;

//@Database(entities = (Device.class),version = 1)
public abstract class DeviceDatabase extends RoomDatabase {
    public abstract DeviceDao timeDao();
}
