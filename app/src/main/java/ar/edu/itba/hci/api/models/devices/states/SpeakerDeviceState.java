package ar.edu.itba.hci.api.models.devices.states;

import android.content.res.Resources;
import android.os.Parcel;

import androidx.room.Embedded;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ar.edu.itba.hci.R;
import ar.edu.itba.hci.api.notifications.NotificationBroadcastReceiver;

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
    public String[] compare(DeviceState deviceState, Resources resources) {
        SpeakerDeviceState param_dev = (SpeakerDeviceState) deviceState;
        ArrayList<String> ret_desc = new ArrayList<>();
        String status, change, genre;
        int id;

        change = resources.getString(R.string.changed_to);

        if(!getStatus().equals(param_dev.getStatus())) {
            id = resources.getIdentifier(param_dev.getStatus(), "string", NotificationBroadcastReceiver.PACKAGE_NAME);
            status = resources.getString(R.string.status);
            ret_desc.add(String.format("%s %s: %s", status, change, id == 0 ? param_dev.getStatus() : resources.getString(id)));
        }

        if(!getGenre().equals(param_dev.getGenre())){
            genre = resources.getString(R.string.genre);
            ret_desc.add(String.format("%s %s: %s", genre, change, param_dev.getGenre()));
        }

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
