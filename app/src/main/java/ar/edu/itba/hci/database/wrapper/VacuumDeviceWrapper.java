package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.VacuumDeviceState;

@Entity(tableName = "vacuumDevice")
public class VacuumDeviceWrapper extends DeviceWrapper{

    @Embedded(prefix = "state_")
    private VacuumDeviceState state;

    public VacuumDeviceWrapper() {
        super();
    }

    @Ignore
    public VacuumDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta,VacuumDeviceState state, Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = state;
    }

    public VacuumDeviceState getState() {
        return state;
    }

    public void setState(VacuumDeviceState vacuumDeviceState) {
        this.state = vacuumDeviceState;
    }
}
