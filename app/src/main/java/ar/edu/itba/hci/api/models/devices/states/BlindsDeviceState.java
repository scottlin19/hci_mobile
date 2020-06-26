package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BlindsDeviceState extends DeviceState{
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
    @Ignore
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
    @Ignore
    protected BlindsDeviceState(Parcel in) {
        status = in.readString();
        if (in.readByte() == 0) {
            level = null;
        } else {
            level = in.readInt();
        }
        if (in.readByte() == 0) {
            currentLevel = null;
        } else {
            currentLevel = in.readInt();
        }
    }

    public static final Creator<BlindsDeviceState> CREATOR = new Creator<BlindsDeviceState>() {
        @Override
        public BlindsDeviceState createFromParcel(Parcel in) {
            return new BlindsDeviceState(in);
        }

        @Override
        public BlindsDeviceState[] newArray(int size) {
            return new BlindsDeviceState[size];
        }
    };

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

    @Override
    public String[] compare(DeviceState deviceState) {
        BlindsDeviceState param_dev = (BlindsDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();

        if(!getStatus().equals(param_dev.getStatus()))
            ret_desc.add(String.format("Status changed to: %s", param_dev.getStatus()));

        if(!getCurrentLevel().equals(param_dev.getCurrentLevel()))
            ret_desc.add(String.format("Current level changed to: %s", param_dev.getCurrentLevel()));

        if(!getLevel().equals(param_dev.getLevel()))
            ret_desc.add(String.format("Level changed to: %s", param_dev.getLevel()));

        return ret_desc.toArray(new String[0]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        if (level == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(level);
        }
        if (currentLevel == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(currentLevel);
        }
    }
}
