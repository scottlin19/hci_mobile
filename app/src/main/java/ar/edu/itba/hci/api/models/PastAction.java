package ar.edu.itba.hci.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PastAction {

    @SerializedName("timestamp")
    @Expose
    private Date timestamp;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("params")
    @Expose
    private String[] params;
    @SerializedName("result")
    @Expose
    private Object result;

    public Date getTimestamp() {
        return timestamp;
    }


    public String getDeviceId() {
        return deviceId;
    }


    public String getAction() {
        return action;
    }


    public String[] getParams() {
        return params;
    }


    public Object getResult() {
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("device: %s - action: %s - timestamp: %s", getDeviceId(), getAction(), getTimestamp());
    }
}