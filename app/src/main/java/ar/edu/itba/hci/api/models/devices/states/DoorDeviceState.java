package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoorDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("lock")
    @Expose
    private String lock;

    /**
     * No args constructor for use in serialization
     *
     */
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(lock);
    }
}
