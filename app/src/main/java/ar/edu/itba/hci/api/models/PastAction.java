package ar.edu.itba.hci.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class PastAction implements Comparable<PastAction>{

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
    private List<Object> params;

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


    public List<Object> getParams() {
        return params;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
    //    public String[] getResult() {
//        return result;
//    }

    @NonNull
    @Override
    public String toString() {
        return String.format("device: %s - action: %s - timestamp: %s", getDeviceId(), getAction(), getTimestamp());
    }

    @Override
    public int compareTo(PastAction o) {
        return timestamp.compareTo(o.timestamp);
    }
}