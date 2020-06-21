package ar.edu.itba.hci.api.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ar.edu.itba.hci.R;

public class Room implements Serializable {

    @SerializedName("id")
    @Expose(serialize = false)
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("meta")
    @Expose
    private RoomMeta meta;



    public Room() {
    }

    public Room(String name, RoomMeta meta) {
        this.name = name;
        this.meta = meta;
    }

    public Room(String id, String name, RoomMeta meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;

    }

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

    public RoomMeta getMeta() {
        return meta;
    }

    public void setMeta(RoomMeta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        if (this.getId() != null)
        {
            if (this.getMeta() != null)
                return String.format("%s - %s - %s", this.getId(), this.getName(), this.getMeta());
            else
                return String.format("%s - %s", this.getId(), this.getName());
        }
        else
        {
            if (this.getMeta() != null)
                return String.format("%s - %s", this.getName(), this.getMeta());
            else
                return this.getName();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
