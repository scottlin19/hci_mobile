package ar.edu.itba.hci.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class DeviceMeta implements Parcelable {
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("favorite")
    @Expose
    private Boolean favorite;
    @SerializedName("blocked")
    @Expose
    private Boolean blocked;
    @SerializedName("notif_status")
    @Expose
    private Boolean notif_status;
    /**
     * No args constructor for use in serialization
     *
     */

    /**
     *
     * @param icon
     * @param favorite
     * @param blocked
     * @param notif_status
     *
     */
    public DeviceMeta(String icon, Boolean favorite,Boolean blocked, Boolean notif_status) {
        super();
        this.icon = icon;
        this.favorite = favorite;
        this.blocked = blocked;
        this.notif_status = notif_status;
    }


    protected DeviceMeta(Parcel in) {
        icon = in.readString();
        byte tmpFavorite = in.readByte();
        favorite = tmpFavorite == 0 ? null : tmpFavorite == 1;
        byte tmpBlocked = in.readByte();
        blocked = tmpBlocked == 0 ? null : tmpBlocked == 1;
        byte tmpNotif_status = in.readByte();
        notif_status = tmpNotif_status == 0 ? null : tmpNotif_status == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeByte((byte) (favorite == null ? 0 : favorite ? 1 : 2));
        dest.writeByte((byte) (blocked == null ? 0 : blocked ? 1 : 2));
        dest.writeByte((byte) (notif_status == null ? 0 : notif_status ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Boolean getBlocked() {
        return blocked;
    }

    public Boolean getNotif_status() {
        return notif_status;
    }

    public void setNotif_status(Boolean notif_status) {
        this.notif_status = notif_status;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getNotifStatus() {
        return notif_status;
    }

    public void setnotifStatus(Boolean notif_status) {
        this.notif_status = notif_status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceMeta that = (DeviceMeta) o;
        return icon.equals(that.icon) &&
                favorite.equals(that.favorite) &&
                blocked.equals(that.blocked) &&
                notif_status.equals(that.notif_status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, favorite, blocked, notif_status);
    }
}


