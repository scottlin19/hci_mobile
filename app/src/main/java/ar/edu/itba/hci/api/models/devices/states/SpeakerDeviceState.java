package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ar.edu.itba.hci.api.models.devices.SpeakerDevice;

public class SpeakerDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("volume")
    @Expose
    private Integer volume;
    @SerializedName("genre")
    @Expose
    private String genre;
    @Embedded(prefix = "speaker_song_")
    @SerializedName("song")
    @Expose
    private SpeakerSong song;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public SpeakerDeviceState() {
    }

    /**
     *
     * @param volume
     * @param song
     * @param genre
     * @param status
     */
    public SpeakerDeviceState(String status, Integer volume, String genre, SpeakerSong song) {
        super();
        this.status = status;
        this.volume = volume;
        this.genre = genre;
        this.song = song;
    }

    @Ignore
    protected SpeakerDeviceState(Parcel in) {
        status = in.readString();
        if (in.readByte() == 0) {
            volume = null;
        } else {
            volume = in.readInt();
        }
        genre = in.readString();
        song = in.readParcelable(SpeakerSong.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        if (volume == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(volume);
        }
        dest.writeString(genre);
        dest.writeParcelable(song, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpeakerDeviceState> CREATOR = new Creator<SpeakerDeviceState>() {
        @Override
        public SpeakerDeviceState createFromParcel(Parcel in) {
            return new SpeakerDeviceState(in);
        }

        @Override
        public SpeakerDeviceState[] newArray(int size) {
            return new SpeakerDeviceState[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public SpeakerSong getSong() {
        return song;
    }

    public void setSong(SpeakerSong song) {
        this.song = song;
    }

    @Override
    public String[] compare(DeviceState deviceState) {
        SpeakerDeviceState param_dev = (SpeakerDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();

        if(!getStatus().equals(param_dev.getStatus()))
            ret_desc.add(String.format("Status changed to: %s", param_dev.getStatus()));

        if(!getGenre().equals(param_dev.getGenre()))
            ret_desc.add(String.format("Genre changed to: %s", param_dev.getGenre()));

        if((getSong() == null && param_dev.getSong() != null) || (getSong() != null && !getSong().equals(param_dev.getSong())))
            ret_desc.add(String.format("Song changed to: %s", param_dev.getSong()));

        if(!getVolume().equals(param_dev.getVolume()))
            ret_desc.add(String.format("Volume changed to: %s", param_dev.getVolume()));

        return  ret_desc.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return "SpeakerDeviceState{" +
                "status='" + status + '\'' +
                ", volume=" + volume +
                ", genre='" + genre + '\'' +
                ", song=" + song +
                '}';
    }
}
