package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.AlarmDeviceState;
import ar.edu.itba.hci.api.models.devices.states.BlindsDeviceState;
import ar.edu.itba.hci.api.models.devices.states.DoorDeviceState;

@Entity(tableName = "blindsDevice")
public class BlindsDeviceWrapper extends DeviceWrapper {

    @Embedded(prefix = "state_")
    private BlindsDeviceState state;

    public BlindsDeviceWrapper() {
        super();
    }

    @Ignore
    public BlindsDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta, BlindsDeviceState state, Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = state;
    }

    public BlindsDeviceState getState() {
        return state;
    }

    public void setState(BlindsDeviceState state) {
        this.state = state;
    }
}
