package ar.edu.itba.hci.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result<T> {
    @SerializedName("result")
    @Expose
    private T result = null;

    public Result() {
    }

    public Result(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
