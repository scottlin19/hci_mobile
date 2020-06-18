package ar.edu.itba.hci.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Device {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private DeviceType type;
    @SerializedName("state")
    @Expose
    private DeviceState state;
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
    public Device(String id, String name, DeviceType type, DeviceState state, DeviceMeta meta) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
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

    public void setState(DeviceState state) {
        this.state = state;
    }

    public DeviceMeta getMeta() {
        return meta;
    }

    public void setMeta(DeviceMeta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
       return "Device xd";
    }

}