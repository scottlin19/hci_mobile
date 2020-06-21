package ar.edu.itba.hci.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceMeta implements Parcelable {
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("favorite")
    @Expose
    private Boolean favorite;

    /**
     * No args constructor for use in serialization
     *
     */
    public DeviceMeta() {
    }

    /**
     *
     * @param icon
     * @param favorite
     */
    public DeviceMeta(String icon, Boolean favorite) {
        super();
        this.icon = icon;
        this.favorite = favorite;
    }

    protected DeviceMeta(Parcel in) {
        icon = in.readString();
        byte tmpFavorite = in.readByte();
        favorite = tmpFavorite == 0 ? null : tmpFavorite == 1;
    }

    public static final Creator<DeviceMeta> CREATOR = new Creator<DeviceMeta>() {
        @Override
        public DeviceMeta createFromParcel(Parcel in) {
            return new DeviceMeta(in);
        }

        @Override
        public DeviceMeta[] newArray(int size) {
            return new DeviceMeta[size];
        }
    };

    @Override
    public String toString() {
        return "DeviceMeta{" +
                "icon='" + icon + '\'' +
                ", favorite=" + favorite +
                '}';
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(icon);
        parcel.writeByte((byte) (favorite == null ? 0 : favorite ? 1 : 2));

    }
}


