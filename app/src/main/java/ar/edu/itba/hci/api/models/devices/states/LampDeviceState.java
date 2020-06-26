package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    @Ignore
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
    @Ignore
    protected LampDeviceState(Parcel in) {
        status = in.readString();
        color = in.readString();
        if (in.readByte() == 0) {
            brightness = null;
        } else {
            brightness = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(color);
        if (brightness == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(brightness);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LampDeviceState> CREATOR = new Creator<LampDeviceState>() {
        @Override
        public LampDeviceState createFromParcel(Parcel in) {
            return new LampDeviceState(in);
        }

        @Override
        public LampDeviceState[] newArray(int size) {
            return new LampDeviceState[size];
        }
    };

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

    @Override
    public String[] compare(DeviceState deviceState) {
        LampDeviceState param_dev = (LampDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();

        if(!getStatus().equals(param_dev.getStatus()))
            ret_desc.add(String.format("Status changed to: %s", param_dev.getStatus()));

        if(!getBrightness().equals(param_dev.getBrightness()))
            ret_desc.add(String.format("Brightness changed to: %s", param_dev.getBrightness()));

        if(!getColor().equals(param_dev.getColor()))
            ret_desc.add(String.format("Color changed to: %s", param_dev.getColor()));

        return  ret_desc.toArray(new String[0]);
    }
}
