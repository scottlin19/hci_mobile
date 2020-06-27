package ar.edu.itba.hci.api.models.devices.states;

import android.content.res.Resources;
import android.os.Parcel;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.notifications.NotificationBroadcastReceiver;

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
    public String[] compare(DeviceState deviceState, Resources resources) {
        BlindsDeviceState param_dev = (BlindsDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();
        String status, change, currLevel, level;
        int id;

        change = resources.getString(R.string.changed_to);

        if(!getStatus().equals(param_dev.getStatus())) {
            id = resources.getIdentifier(param_dev.getStatus(), "string", NotificationBroadcastReceiver.PACKAGE_NAME);
            status = resources.getString(R.string.status);
            ret_desc.add(String.format("%s %s: %s", status, change, id == 0 ? param_dev.getStatus() : resources.getString(id)));
        }

        if(!getCurrentLevel().equals(param_dev.getCurrentLevel())){
            currLevel = resources.getString(R.string.curr_level);
            ret_desc.add(String.format("%s %s: %s", currLevel, change, param_dev.getCurrentLevel()));
        }

        if(!getLevel().equals(param_dev.getLevel())){
            level = resources.getString(R.string.level);
            ret_desc.add(String.format("%s %s: %s", level, change, param_dev.getCurrentLevel()));
        }

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
