package ar.edu.itba.hci.api.models.states;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class FridgeDeviceState extends DeviceState {

    @SerializedName("freezerTemperature")
    @Expose
    private Integer freezerTemperature;
    @SerializedName("temperature")
    @Expose
    private Integer temperature;
    @SerializedName("mode")
    @Expose
    private String mode;

    /**
     * No args constructor for use in serialization
     *
     */
    public FridgeDeviceState() {
    }

    /**
     *
     * @param mode
     * @param freezerTemperature
     * @param temperature
     */
    public FridgeDeviceState(Integer freezerTemperature, Integer temperature, String mode) {
        super();
        this.freezerTemperature = freezerTemperature;
        this.temperature = temperature;
        this.mode = mode;
    }

    public Integer getFreezerTemperature() {
        return freezerTemperature;
    }

    public void setFreezerTemperature(Integer freezerTemperature) {
        this.freezerTemperature = freezerTemperature;
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
}
