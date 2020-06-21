package ar.edu.itba.hci.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.R;

public class RoomMeta implements Parcelable {

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("blocked")
    @Expose
    private Boolean blocked;

    protected RoomMeta(Parcel in) {
        icon = in.readString();
        byte tmpBlocked = in.readByte();
        blocked = tmpBlocked == 0 ? null : tmpBlocked == 1;
    }

    public static final Creator<RoomMeta> CREATOR = new Creator<RoomMeta>() {
        @Override
        public RoomMeta createFromParcel(Parcel in) {
            return new RoomMeta(in);
        }

        @Override
        public RoomMeta[] newArray(int size) {
            return new RoomMeta[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(icon);
        parcel.writeByte((byte) (blocked == null ? 0 : blocked ? 1 : 2));
    }
}
