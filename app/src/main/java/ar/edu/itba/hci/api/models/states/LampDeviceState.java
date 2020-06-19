package ar.edu.itba.hci.api.models.states;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LampDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("brightness")
    @Expose
    private Integer brightness;

    /**
     * No args constructor for use in serialization
     *
     */
    public LampDeviceState() {
    }

    /**
     *
     * @param brightness
     * @param color
     * @param status
     */
    public LampDeviceState(String status, String color, Integer brightness) {
        super();
        this.status = status;
        this.color = color;
        this.brightness = brightness;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

}
