package ar.edu.itba.hci.api.models.states;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcDeviceState extends DeviceState{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("temperature")
    @Expose
    private Integer temperature;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("verticalSwing")
    @Expose
    private String verticalSwing;
    @SerializedName("horizontalSwing")
    @Expose
    private String horizontalSwing;
    @SerializedName("fanSpeed")
    @Expose
    private String fanSpeed;

    /**
     * No args constructor for use in serialization
     *
     */
    public AcDeviceState() {
    }

    /**
     *
     * @param mode
     * @param horizontalSwing
     * @param temperature
     * @param verticalSwing
     * @param fanSpeed
     * @param status
     */
    public AcDeviceState(String status, Integer temperature, String mode, String verticalSwing, String horizontalSwing, String fanSpeed) {
        super();
        this.status = status;
        this.temperature = temperature;
        this.mode = mode;
        this.verticalSwing = verticalSwing;
        this.horizontalSwing = horizontalSwing;
        this.fanSpeed = fanSpeed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getVerticalSwing() {
        return verticalSwing;
    }

    public void setVerticalSwing(String verticalSwing) {
        this.verticalSwing = verticalSwing;
    }

    public String getHorizontalSwing() {
        return horizontalSwing;
    }

    public void setHorizontalSwing(String horizontalSwing) {
        this.horizontalSwing = horizontalSwing;
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }
}
