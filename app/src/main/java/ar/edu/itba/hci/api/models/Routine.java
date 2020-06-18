package ar.edu.itba.hci.api.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Routine {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("actions")
    @Expose
    private List<RoutineAction> actions = null;

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

    public List<RoutineAction> getActions() {
        return actions;
    }

    public void setActions(List<RoutineAction> actions) {
        this.actions = actions;
    }

}