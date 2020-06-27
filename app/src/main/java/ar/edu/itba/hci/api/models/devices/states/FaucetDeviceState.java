package ar.edu.itba.hci.api.models.devices.states;

import android.content.res.Resources;
import android.os.Parcel;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.notifications.NotificationBroadcastReceiver;

public class FaucetDeviceState extends DeviceState{

    @ColumnInfo(name = "status")
    @SerializedName("status")
    @Expose
    private String status;


    @ColumnInfo(name = "quantity")
    @SerializedName("quantity")
    @Expose
    private Float quantity;

    @ColumnInfo(name = "unit")
    @SerializedName("unit")
    @Expose
    private String unit;

    @ColumnInfo(name = "dispensedQuantity")
    @SerializedName("dispensedQuantity")
    @Expose
    private Float dispensedQuantity;


    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
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

    public Float getQuantity() {return quantity;}

    public Float getDispensedQuantity() { return dispensedQuantity;}

    public String getUnit() {return unit;}

    @Override
    public String[] compare(DeviceState deviceState, Resources resources) {
        FaucetDeviceState param_dev = (FaucetDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();
        String status, change;

        change = resources.getString(R.string.changed_to);

        if(!getStatus().equals(param_dev.getStatus())) {
            int id = resources.getIdentifier(param_dev.getStatus(), "string", NotificationBroadcastReceiver.PACKAGE_NAME);
            status = resources.getString(R.string.status);
            ret_desc.add(String.format("%s %s: %s", status, change, id == 0 ? param_dev.getStatus() : resources.getString(id)));
        }


      /*  Float disp_quant =getDispensedQuantity();
        Float param_dev_disp_quant = param_dev.getDispensedQuantity();


        if(!compareDispQuant){
            ret_desc.add(String.format("Dispensed quantity changed to: %s", param_dev.getDispensedQuantity()));
        }
        Float quant =getQuantity();
        Float param_dev_quant = param_dev.getQuantity();
        boolean compareQuant = quant == null ? param_dev_quant == null :  Float.compare(quant, param_dev_quant) == 0;

        if(!compareQuant)
            ret_desc.add(String.format("Quantity changed to: %s", param_dev.getQuantity()));
        Float unit =getQuantity();
        Float param_dev_unit = param_dev.getQuantity();
        boolean compareUnit = quant == null ? param_dev_quant == null :  Float.compare(quant, param_dev_quant) == 0;
        if(!getUnit().equals(param_dev.getUnit()))
            ret_desc.add(String.format("Unit changed to: %s", param_dev.getUnit()));*/

        return  ret_desc.toArray(new String[0]);
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDispensedQuantity(Float dispensedQuantity) {
        this.dispensedQuantity = dispensedQuantity;
    }
}
