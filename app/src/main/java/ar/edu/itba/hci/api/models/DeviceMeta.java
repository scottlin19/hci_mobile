package ar.edu.itba.hci.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceMeta {
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

}


