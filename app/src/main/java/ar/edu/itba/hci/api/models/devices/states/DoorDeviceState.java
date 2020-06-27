package ar.edu.itba.hci.api.models.devices.states;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ar.edu.itba.hci.MainActivity;
import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.notifications.NotificationBroadcastReceiver;

public class DoorDeviceState extends DeviceState {

    @ColumnInfo(name = "status")
    @SerializedName("status")
    @Expose
    private String status;

    @ColumnInfo(name = "lock")
    @SerializedName("lock")
    @Expose
    private String lock;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public DoorDeviceState() {
    }

    /**
     *
     * @param lock
     * @param status
     */
    public DoorDeviceState(String status, String lock) {
        super();
        this.status = status;
        this.lock = lock;
    }
    @Ignore
    protected DoorDeviceState(Parcel in) {
        status = in.readString();
        lock = in.readString();
    }

    public static final Creator<DoorDeviceState> CREATOR = new Creator<DoorDeviceState>() {
        @Override
        public DoorDeviceState createFromParcel(Parcel in) {
            return new DoorDeviceState(in);
        }

        @Override
        public DoorDeviceState[] newArray(int size) {
            return new DoorDeviceState[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    @Override
    public String[] compare(DeviceState deviceState, Resources resources) {
        DoorDeviceState param_dev = (DoorDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();
        String status, change, lockSt;

        change = resources.getString(R.string.changed_to);

        if(!getStatus().equals(param_dev.getStatus())) {
            int id = resources.getIdentifier(param_dev.getStatus(), "string", NotificationBroadcastReceiver.PACKAGE_NAME);
            status = resources.getString(R.string.status);
            ret_desc.add(String.format("%s %s: %s", status, change, id == 0 ? param_dev.getStatus() : resources.getString(id)));
        }

        if(!getLock().equals(param_dev.getLock())) {
            int id = resources.getIdentifier(param_dev.getLock(), "string", NotificationBroadcastReceiver.PACKAGE_NAME);
            lockSt = resources.getString(R.string.lock_status);
            ret_desc.add(String.format("%s %s: %s", lockSt, change, id == 0 ? param_dev.getLock() : resources.getString(id)));
        }

        return  ret_desc.toArray(new String[0]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(lock);
    }
}
