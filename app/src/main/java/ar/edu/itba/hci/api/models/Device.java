package ar.edu.itba.hci.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.edu.itba.hci.api.models.devices.states.DeviceState;


public class Device<T extends DeviceState> {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose(serialize = false)
    private DeviceType type;
    @SerializedName("room")
    @Expose(serialize = false)
    private Room room;
    @SerializedName("state")
    @Expose(serialize = false)
    private T state;
    @SerializedName("meta")
    @Expose
    private DeviceMeta meta;

    /**
     * No args constructor for use in serialization
     **/
    public Device() {
    }

    /**
     *
     * @param meta
     * @param name
     * @param id
     * @param state
     * @param type
     */
    public Device(String id, String name, DeviceType type, T state,Room room, DeviceMeta meta) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
        this.room = room;
        this.meta = meta;
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

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(T state) {
        this.state = state;
    }

    public DeviceMeta getMeta() {
        return meta;
    }

    public void setMeta(DeviceMeta meta) {
        this.meta = meta;
    }

    public Room getRoom() { return this.room; }

    @Override
    public String toString() {
       return "Device: " + name;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}