package ar.edu.itba.hci.api.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoutineAction {

    @SerializedName("device")
    @Expose
    private Device device;
    @SerializedName("actionName")
    @Expose
    private String actionName;
    @SerializedName("params")
    @Expose
    private List<Object> params = null;
    @SerializedName("meta")
    @Expose
    private RoutineMeta meta;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public RoutineMeta getMeta() {
        return meta;
    }

    public void setMeta(RoutineMeta meta) {
        this.meta = meta;
    }

}