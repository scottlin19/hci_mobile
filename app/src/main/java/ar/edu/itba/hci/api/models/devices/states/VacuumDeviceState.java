package ar.edu.itba.hci.api.models.devices.states;

import android.content.res.Resources;
import android.os.Parcel;

import androidx.room.Embedded;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Objects;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.notifications.NotificationBroadcastReceiver;

public class VacuumDeviceState extends  DeviceState{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("batteryLevel")
    @Expose
    private Integer batteryLevel;
    @Embedded(prefix = "vacuum_location_")
    @SerializedName("location")
    @Expose
    private VacuumLocation location;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public VacuumDeviceState() {
    }

    /**
     *
     * @param mode
     * @param location
     * @param status
     * @param batteryLevel
     */

    public VacuumDeviceState(String status, String mode, Integer batteryLevel, VacuumLocation location) {
        super();
        this.status = status;
        this.mode = mode;
        this.batteryLevel = batteryLevel;
        this.location = location;
    }

    @Ignore
    protected VacuumDeviceState(Parcel in) {
        status = in.readString();
        mode = in.readString();
        if (in.readByte() == 0) {
            batteryLevel = null;
        } else {
            batteryLevel = in.readInt();
        }
        location = in.readParcelable(VacuumLocation.class.getClassLoader());
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

    public VacuumLocation getLocation() {
        return location;
    }

    public void setLocation(VacuumLocation location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(mode);
        if (batteryLevel == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(batteryLevel);
        }
        parcel.writeParcelable(location, i);
    }

    @Override
    public String[] compare(DeviceState deviceState, Resources resources) {
        VacuumDeviceState param_dev = (VacuumDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();
        String status, change, batt, location, mode;
        int id;

        change = resources.getString(R.string.changed_to);

        if(!getStatus().equals(param_dev.getStatus())) {
            id = resources.getIdentifier(param_dev.getStatus(), "string", NotificationBroadcastReceiver.PACKAGE_NAME);
            status = resources.getString(R.string.status);
            ret_desc.add(String.format("%s %s: %s", status, change, id == 0 ? param_dev.getStatus() : resources.getString(id)));
        }

        if(param_dev.getBatteryLevel() <= 5) {
            batt = resources.getString(R.string.low_battery);
            ret_desc.add(String.format("%s: %%%s", batt, param_dev.getBatteryLevel()));
        }
            VacuumLocation vl = getLocation(), param_dev_vl = param_dev.getLocation();
            boolean compareLocation = Objects.equals(vl, param_dev_vl);

        if(compareLocation == false){
            location = resources.getString(R.string.location);
            ret_desc.add(String.format("%s %s: %s", location, change, param_dev.getLocation()));
        }

        if(!getMode().equals(param_dev.getMode())){
            id = resources.getIdentifier(param_dev.getMode(), "string", NotificationBroadcastReceiver.PACKAGE_NAME);
            mode = resources.getString(R.string.mode);
            ret_desc.add(String.format("%s %s: %s", mode, change, id == 0 ? param_dev.getMode() : resources.getString(id)));
        }

        return  ret_desc.toArray(new String[0]);
    }
}
