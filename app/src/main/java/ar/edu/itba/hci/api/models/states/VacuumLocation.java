package ar.edu.itba.hci.api.models.states;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VacuumLocation implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;


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


}
