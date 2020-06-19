package ar.edu.itba.hci.api.models.states;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoorDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("lock")
    @Expose
    private String lock;

    /**
     * No args constructor for use in serialization
     *
     */
    public DoorDeviceState() {
    }

    /**
     *
     * @param lock
     * @param status
     */
    public DoorDeviceState(String status, String lock) {
        super();
        this.status = status;
        this.lock = lock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

}
