package ar.edu.itba.hci.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class DeviceType implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("powerUsage")
    @Expose
    private Integer powerUsage;

    public DeviceType(String id, String name, Integer powerUsage) {
        this.id = id;
        this.name = name;
        this.powerUsage = powerUsage;
    }

    protected DeviceType(Parcel in) {
        id = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            powerUsage = null;
        } else {
            powerUsage = in.readInt();
        }
    }

    public static final Creator<DeviceType> CREATOR = new Creator<DeviceType>() {
        @Override
        public DeviceType createFromParcel(Parcel in) {
            return new DeviceType(in);
        }

        @Override
        public DeviceType[] newArray(int size) {
            return new DeviceType[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPowerUsage() {
        return powerUsage;
    }

    public void setPowerUsage(Integer powerUsage) {
        this.powerUsage = powerUsage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        if (powerUsage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(powerUsage);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceType that = (DeviceType) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                powerUsage.equals(that.powerUsage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, powerUsage);
    }
}
