package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AcDeviceState  extends DeviceState{

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
    @Ignore
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
    @Ignore
    protected AcDeviceState(Parcel in) {
        status = in.readString();
        if (in.readByte() == 0) {
            temperature = null;
        } else {
            temperature = in.readInt();
        }
        mode = in.readString();
        verticalSwing = in.readString();
        horizontalSwing = in.readString();
        fanSpeed = in.readString();
    }

    public static final Creator<AcDeviceState> CREATOR = new Creator<AcDeviceState>() {
        @Override
        public AcDeviceState createFromParcel(Parcel in) {
            return new AcDeviceState(in);
        }

        @Override
        public AcDeviceState[] newArray(int size) {
            return new AcDeviceState[size];
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

    @Override
    public String[] compare(DeviceState deviceState) {
        AcDeviceState param_dev = (AcDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();

        if(!getStatus().equals(param_dev.getStatus()))
            ret_desc.add(String.format("Status changed to: %s", param_dev.getStatus()));

        if(!getFanSpeed().equals(param_dev.getFanSpeed()))
            ret_desc.add(String.format("Fan speed changed to: %s", param_dev.getFanSpeed()));

        if(!getHorizontalSwing().equals(param_dev.getHorizontalSwing()))
            ret_desc.add(String.format("Horizontal Swing changed to: %s", param_dev.getHorizontalSwing()));

        if(!getMode().equals(param_dev.getMode()))
            ret_desc.add(String.format("Mode changed to: %s", param_dev.getMode()));

        if(!getTemperature().equals(param_dev.getTemperature()))
            ret_desc.add(String.format("Temperature changed to: %s", param_dev.getTemperature()));

        if(!getVerticalSwing().equals(param_dev.getVerticalSwing()))
            ret_desc.add(String.format("Vertical swing changed to: %s", param_dev.getVerticalSwing()));

        return ret_desc.toArray(new String[0]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        if (temperature == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(temperature);
        }
        parcel.writeString(mode);
        parcel.writeString(verticalSwing);
        parcel.writeString(horizontalSwing);
        parcel.writeString(fanSpeed);
    }
}
