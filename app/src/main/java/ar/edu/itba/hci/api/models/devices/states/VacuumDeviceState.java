package ar.edu.itba.hci.api.models.devices.states;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VacuumDeviceState extends DeviceState {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("batteryLevel")
    @Expose
    private Integer batteryLevel;
    @SerializedName("location")
    @Expose
    private Object location;

    /**
     * No args constructor for use in serialization
     *
     */
    public VacuumDeviceState() {
    }

    /**
     *
     * @param mode
     * @param location
     * @param status
     * @param batteryLevel
     */
    public VacuumDeviceState(String status, String mode, Integer batteryLevel, Object location) {
        super();
        this.status = status;
        this.mode = mode;
        this.batteryLevel = batteryLevel;
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }
}
