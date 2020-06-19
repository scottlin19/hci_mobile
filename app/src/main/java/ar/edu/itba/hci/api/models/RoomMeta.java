package ar.edu.itba.hci.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomMeta implements Serializable {

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("blocked")
    @Expose
    private Boolean blocked;

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

}
