package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.OvenDeviceState;

@Entity(tableName = "ovenDevice")
public class OvenDeviceWrapper extends DeviceWrapper{

    @Embedded(prefix = "state_")
    private OvenDeviceState state;

    public OvenDeviceWrapper(){
        super();
    }

    @Ignore
    public OvenDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta, OvenDeviceState ovenDeviceState, Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = ovenDeviceState;
    }

    public OvenDeviceState getState() {
        return state;
    }

    public void setState(OvenDeviceState ovenDeviceState) {
        this.state = ovenDeviceState;
    }
}
