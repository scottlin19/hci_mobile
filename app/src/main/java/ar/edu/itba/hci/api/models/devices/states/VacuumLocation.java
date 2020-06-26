package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VacuumLocation implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public VacuumLocation() {
    }

    /**
     *
     * @param id
     * @param name
     */
    public VacuumLocation(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    @Ignore
    protected VacuumLocation(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<VacuumLocation> CREATOR = new Creator<VacuumLocation>() {
        @Override
        public VacuumLocation createFromParcel(Parcel in) {
            return new VacuumLocation(in);
        }

        @Override
        public VacuumLocation[] newArray(int size) {
            return new VacuumLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
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

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
