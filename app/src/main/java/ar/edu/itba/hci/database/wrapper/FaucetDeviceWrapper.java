package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.FaucetDeviceState;

@Entity(tableName = "faucetDevice")
public class FaucetDeviceWrapper extends DeviceWrapper {

    @Embedded(prefix = "state_")
    private FaucetDeviceState state;

    public FaucetDeviceWrapper() {
        super();
    }

    @Ignore
    public FaucetDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta, FaucetDeviceState state, Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = state;
    }

    public FaucetDeviceState getState() {
        return state;
    }

    public void setState(FaucetDeviceState state) {
        this.state = state;
    }
}
