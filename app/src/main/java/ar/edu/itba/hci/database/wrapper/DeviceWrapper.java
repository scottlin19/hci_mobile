package ar.edu.itba.hci.database.wrapper;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.PrimaryKey;

import ar.edu.itba.hci.api.models.Device;
import ar.edu.itba.hci.api.models.DeviceMeta;
import ar.edu.itba.hci.api.models.DeviceType;
import ar.edu.itba.hci.api.models.Room;
import ar.edu.itba.hci.api.models.devices.states.DeviceState;

public abstract class DeviceWrapper {

    @PrimaryKey
    @NonNull
    protected String id;
    protected String name;
    @Embedded(prefix = "device_type_")
    protected DeviceType deviceType;
    @Embedded(prefix = "device_meta_")
    protected DeviceMeta deviceMeta;

    @Embedded(prefix = "room_")
    private Room room;

    public DeviceWrapper(String id, String name, DeviceType deviceType, DeviceMeta deviceMeta,Room room) {
        this.id = id;
        this.name = name;
        this.deviceType = deviceType;
        this.deviceMeta = deviceMeta;
        this.room = room;
    }

    public DeviceWrapper() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceMeta getDeviceMeta() {
        return deviceMeta;
    }

    public void setDeviceMeta(DeviceMeta deviceMeta) {
        this.deviceMeta = deviceMeta;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
