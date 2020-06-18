package ar.edu.itba.hci.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceType {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("powerUsage")
    @Expose
    private Integer powerUsage;

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
}
