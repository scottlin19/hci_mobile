package ar.edu.itba.hci.api.models.devices.states;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlarmDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public AlarmDeviceState() {
    }

    /**
     *
     * @param status
     */
    public AlarmDeviceState(String status) {
        super();
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
