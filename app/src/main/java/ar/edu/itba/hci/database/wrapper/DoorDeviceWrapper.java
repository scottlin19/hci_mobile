package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.DoorDeviceState;

@Entity(tableName = "doorDevice")
public class DoorDeviceWrapper extends DeviceWrapper {
    @Embedded(prefix = "state_")
    private DoorDeviceState state;

    public DoorDeviceWrapper() {
        super();
    }

    @Ignore
    public DoorDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta, DoorDeviceState state, Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = state;
    }

    public DoorDeviceState getState() {
        return state;
    }

    public void setState(DoorDeviceState state) {
        this.state = state;
    }
}
