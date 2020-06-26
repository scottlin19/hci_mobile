package ar.edu.itba.hci.database.wrapper;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.SpeakerDeviceState;

@Entity(tableName = "speakerDevice")
public class SpeakerDeviceWrapper extends DeviceWrapper {
    @Embedded(prefix = "state_")
    private SpeakerDeviceState state;

    public SpeakerDeviceWrapper(){
        super();
    }



    @Ignore
    public SpeakerDeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta, SpeakerDeviceState state, Room room) {
        super(id, name, deviceType, deviceMeta,room);
        this.state = state;
    }

    public SpeakerDeviceState getState() {
        return state;
    }

    public void setState(SpeakerDeviceState state) {
        this.state = state;
    }
}
