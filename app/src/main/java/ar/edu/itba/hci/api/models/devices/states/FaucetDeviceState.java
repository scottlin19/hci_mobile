package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaucetDeviceState extends DeviceState{
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public FaucetDeviceState() {
    }

    /**
     *
     * @param status
     */
    public FaucetDeviceState(String status) {
        super();
        this.status = status;
    }

    protected FaucetDeviceState(Parcel in) {
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FaucetDeviceState> CREATOR = new Creator<FaucetDeviceState>() {
        @Override
        public FaucetDeviceState createFromParcel(Parcel in) {
            return new FaucetDeviceState(in);
        }

        @Override
        public FaucetDeviceState[] newArray(int size) {
            return new FaucetDeviceState[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
