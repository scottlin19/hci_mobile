package ar.edu.itba.hci.api.models;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.edu.itba.hci.MainActivity;
import ar.edu.itba.hci.R;

public class RoutineAction {

    @SerializedName("device")
    @Expose
    private Device device;
    @SerializedName("actionName")
    @Expose
    private String actionName;
    @SerializedName("params")
    @Expose
    private List<String> params = null;
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

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public RoutineMeta getMeta() {
        return meta;
    }

    public void setMeta(RoutineMeta meta) {
        this.meta = meta;
    }
}