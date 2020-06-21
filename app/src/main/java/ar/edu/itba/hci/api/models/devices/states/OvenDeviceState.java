package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.edu.itba.hci.api.models.devices.states.DeviceState;

public class OvenDeviceState extends DeviceState{

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

    protected OvenDeviceState(Parcel in) {
        status = in.readString();
        if (in.readByte() == 0) {
            temperature = null;
        } else {
            temperature = in.readInt();
        }
        heat = in.readString();
        grill = in.readString();
        convection = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        if (temperature == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(temperature);
        }
        dest.writeString(heat);
        dest.writeString(grill);
        dest.writeString(convection);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OvenDeviceState> CREATOR = new Creator<OvenDeviceState>() {
        @Override
        public OvenDeviceState createFromParcel(Parcel in) {
            return new OvenDeviceState(in);
        }

        @Override
        public OvenDeviceState[] newArray(int size) {
            return new OvenDeviceState[size];
        }
    };

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
