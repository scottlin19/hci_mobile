package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaucetDeviceState extends DeviceState{
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("quantity")
    @Expose
    private Float quantity;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("dispensedQuantity")
    @Expose
    private Float dispensedQuantity;


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
    public FaucetDeviceState(String status, Float quantity, String unit, Float dispensedQuantity) {
        super();
        this.status = status;
        this.quantity = quantity;
        this.unit = unit;
        this.dispensedQuantity = dispensedQuantity;
    }

    protected FaucetDeviceState(Parcel in) {
        status = in.readString();
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readFloat();
        }
        if (in.readByte() == 0) {
            unit= null;
        } else {
            unit = in.readString();
        }
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readFloat();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeFloat(quantity);
        }
        if (unit == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(unit);
        }
        if (dispensedQuantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(dispensedQuantity);
        }

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

    public float getQuantity() {return quantity;}

    public float getDispensedQuantity() { return dispensedQuantity;}

    public String getUnit() {return unit;}


}
