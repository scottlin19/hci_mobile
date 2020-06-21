package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VacuumDeviceState extends DeviceState{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("batteryLevel")
    @Expose
    private Integer batteryLevel;
    @SerializedName("location")
    @Expose
    private Object location;

    /**
     * No args constructor for use in serialization
     *
     */
    public VacuumDeviceState() {
    }

    /**
     *
     * @param mode
     * @param location
     * @param status
     * @param batteryLevel
     */
    public VacuumDeviceState(String status, String mode, Integer batteryLevel, Object location) {
        super();
        this.status = status;
        this.mode = mode;
        this.batteryLevel = batteryLevel;
        this.location = location;
    }

    protected VacuumDeviceState(Parcel in) {
        status = in.readString();
        mode = in.readString();
        if (in.readByte() == 0) {
            batteryLevel = null;
        } else {
            batteryLevel = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(mode);
        if (batteryLevel == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(batteryLevel);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VacuumDeviceState> CREATOR = new Creator<VacuumDeviceState>() {
        @Override
        public VacuumDeviceState createFromParcel(Parcel in) {
            return new VacuumDeviceState(in);
        }

        @Override
        public VacuumDeviceState[] newArray(int size) {
            return new VacuumDeviceState[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }
}
