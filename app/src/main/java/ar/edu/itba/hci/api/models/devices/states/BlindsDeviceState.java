package ar.edu.itba.hci.api.models.devices.states;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlindsDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("currentLevel")
    @Expose
    private Integer currentLevel;

    /**
     * No args constructor for use in serialization
     *
     */
    public BlindsDeviceState() {
    }

    /**
     *
     * @param currentLevel
     * @param level
     * @param status
     */
    public BlindsDeviceState(String status, Integer level, Integer currentLevel) {
        super();
        this.status = status;
        this.level = level;
        this.currentLevel = currentLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }
}
