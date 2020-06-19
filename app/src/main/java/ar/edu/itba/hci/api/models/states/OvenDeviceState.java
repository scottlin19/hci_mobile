package ar.edu.itba.hci.api.models.states;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OvenDeviceState extends DeviceState {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("temperature")
    @Expose
    private Integer temperature;
    @SerializedName("heat")
    @Expose
    private String heat;
    @SerializedName("grill")
    @Expose
    private String grill;
    @SerializedName("convection")
    @Expose
    private String convection;

    /**
     * No args constructor for use in serialization
     *
     */
    public OvenDeviceState() {
    }

    /**
     *
     * @param heat
     * @param convection
     * @param temperature
     * @param grill
     * @param status
     */
    public OvenDeviceState(String status, Integer temperature, String heat, String grill, String convection) {
        super();
        this.status = status;
        this.temperature = temperature;
        this.heat = heat;
        this.grill = grill;
        this.convection = convection;
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

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public String getGrill() {
        return grill;
    }

    public void setGrill(String grill) {
        this.grill = grill;
    }

    public String getConvection() {
        return convection;
    }

    public void setConvection(String convection) {
        this.convection = convection;
    }

}
