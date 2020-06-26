package ar.edu.itba.hci.api.models.devices.states;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    @Ignore
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
    @Ignore
    protected FridgeDeviceState(Parcel in) {
        if (in.readByte() == 0) {
            freezerTemperature = null;
        } else {
            freezerTemperature = in.readInt();
        }
        if (in.readByte() == 0) {
            temperature = null;
        } else {
            temperature = in.readInt();
        }
        mode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (freezerTemperature == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(freezerTemperature);
        }
        if (temperature == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(temperature);
        }
        dest.writeString(mode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FridgeDeviceState> CREATOR = new Creator<FridgeDeviceState>() {
        @Override
        public FridgeDeviceState createFromParcel(Parcel in) {
            return new FridgeDeviceState(in);
        }

        @Override
        public FridgeDeviceState[] newArray(int size) {
            return new FridgeDeviceState[size];
        }
    };

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

    @Override
    public String[] compare(DeviceState deviceState) {
        FridgeDeviceState param_dev = (FridgeDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();

        if(!getFreezerTemperature().equals(param_dev.getFreezerTemperature()))
            ret_desc.add(String.format("Freezer temperature changed to: %s", param_dev.getFreezerTemperature()));

        if(!getMode().equals(param_dev.getMode()))
            ret_desc.add(String.format("Mode changed to: %s", param_dev.getMode()));

        if(!getTemperature().equals(param_dev.getTemperature()))
            ret_desc.add(String.format("Temperature changed to: %s", param_dev.getTemperature()));

        return  ret_desc.toArray(new String[0]);
    }
}
