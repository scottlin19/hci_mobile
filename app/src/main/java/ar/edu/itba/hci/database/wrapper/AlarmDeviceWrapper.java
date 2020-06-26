package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.AlarmDeviceState;

@Entity(tableName = "alarmDevice")
public class AlarmDeviceWrapper extends DeviceWrapper {
    @Embedded(prefix = "state_")
    private AlarmDeviceState state;

    public AlarmDeviceWrapper() {
        super();
    }



    @Ignore
    public AlarmDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta, AlarmDeviceState state, Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = state;
    }

    public AlarmDeviceState getState() {
        return state;
    }

    public void setState(AlarmDeviceState state) {
        this.state = state;
    }
}
