package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.FridgeDeviceState;
@Entity(tableName = "fridgeDevice")
public class FridgeDeviceWrapper extends DeviceWrapper {
    @Embedded(prefix = "state_")
    private FridgeDeviceState state;

    public FridgeDeviceWrapper(){
        super();
    }

    @Ignore
    public FridgeDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta, FridgeDeviceState state, Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = state;
    }

    public FridgeDeviceState getState() {
        return state;
    }

    public void setState(FridgeDeviceState state) {
        this.state = state;
    }
}
