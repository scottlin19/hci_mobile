package ar.edu.itba.hci.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RoutineParams {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private String value;

    public RoutineParams(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public RoutineParams(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}


