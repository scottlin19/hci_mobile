package ar.edu.itba.hci.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import ar.edu.itba.hci.api.models.devices.states.DeviceState;


public class Device<T extends DeviceState> implements Parcelable {


    @Embedded(prefix = "device_type_")
    @SerializedName("type")
    @Expose(serialize = false)
    private DeviceType type;


    @Embedded(prefix = "room_")
    @SerializedName("room")
    @Expose(serialize = false)
    private Room room;


    @Embedded(prefix = "device_state_")
    @SerializedName("state")
    @Expose(serialize = false)
    private T state;


    @Embedded(prefix = "meta_")
    @SerializedName("meta")
    @Expose
    private DeviceMeta meta;


    @PrimaryKey
    @NotNull
    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("name")
    @Expose
    private String name;

    /**
     * No args constructor for use in serialization
     **/
    public Device() {
    }

    /**
     *
     * @param meta
     * @param name
     * @param id
     * @param state
     * @param type
     */
    public Device(String id, String name, DeviceType type, T state,Room room, DeviceMeta meta) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
        this.room = room;
        this.meta = meta;
    }


    protected Device(Parcel in) {
        type = in.readParcelable(DeviceType.class.getClassLoader());
        room = in.readParcelable(Room.class.getClassLoader());
        Class<?> type = (Class<?>) in.readSerializable();
        state = in.readParcelable(type.getClassLoader());
        meta = in.readParcelable(DeviceMeta.class.getClassLoader());
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };


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

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public T getState() {
        return state;
    }

    public void setState(T state) {
        this.state = state;
    }

    public DeviceMeta getMeta() {
        return meta;
    }

    public void setMeta(DeviceMeta meta) {
        this.meta = meta;
    }

    public Room getRoom() { return this.room; }

    public void setRoom(Room room) { this.room = room; }

    public String getRoomName() { return this.room == null ? null : this.room.getName(); }

    @Override
    public String toString() {
        return "Device{" +


                ", meta={ "+meta.getNotifStatus()+"}"+
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device<?> device = (Device<?>) o;
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(type, i);
        parcel.writeParcelable(room, i);
        final Class<?> objectsType = state.getClass();

        parcel.writeSerializable(objectsType);
        parcel.writeParcelable(state, i);
        parcel.writeParcelable(meta, i);
        parcel.writeString(id);
        parcel.writeString(name);
    }


}