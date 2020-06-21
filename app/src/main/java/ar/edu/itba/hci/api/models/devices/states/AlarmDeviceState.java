package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlarmDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public AlarmDeviceState() {
    }

    /**
     *
     * @param status
     */
    public AlarmDeviceState(String status) {
        super();
        this.status = status;
    }

    protected AlarmDeviceState(Parcel in) {
        status = in.readString();
    }

    public static final Creator<AlarmDeviceState> CREATOR = new Creator<AlarmDeviceState>() {
        @Override
        public AlarmDeviceState createFromParcel(Parcel in) {
            return new AlarmDeviceState(in);
        }

        @Override
        public AlarmDeviceState[] newArray(int size) {
            return new AlarmDeviceState[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
    }
}
