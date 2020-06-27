package ar.edu.itba.hci.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Params<T> {
    @SerializedName("params")
    @Expose
    private T params = null;

    public Params() {
    }

    public Params(T result) {
        this.params = result;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T result) {
        this.params = result;
    }
}
