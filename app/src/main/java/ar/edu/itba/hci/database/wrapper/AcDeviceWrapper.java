package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.AcDeviceState;


@Entity(tableName = "acDevice")
public class AcDeviceWrapper extends DeviceWrapper {

    @Embedded(prefix = "state_")
    private AcDeviceState state;

    public AcDeviceWrapper(){
        super();
    }



    @Ignore
    public AcDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta, AcDeviceState acDeviceState,Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = acDeviceState;
    }

    public AcDeviceState getState() {
        return state;
    }

    public void setState(AcDeviceState acDeviceState) {
        this.state = acDeviceState;
    }
}
