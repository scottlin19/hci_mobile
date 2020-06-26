package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.LampDeviceState;

@Entity(tableName = "lampDevice")
public class LampDeviceWrapper extends DeviceWrapper {

    @Embedded(prefix = "state_")
    private LampDeviceState state;

    public LampDeviceWrapper(){
        super();
    }

    @Ignore
    public LampDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta, LampDeviceState state, Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = state;
    }

    public LampDeviceState getState() {
        return state;
    }

    public void setState(LampDeviceState state) {
        this.state = state;
    }
}
