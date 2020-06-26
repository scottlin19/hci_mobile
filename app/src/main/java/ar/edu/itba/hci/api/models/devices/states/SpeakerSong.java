package ar.edu.itba.hci.api.models.devices.states;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.SQLOutput;

public class SpeakerSong extends DeviceState implements Parcelable {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("album")
    @Expose
    private String album;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("progress")
    @Expose
    private String progress;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public SpeakerSong() {
    }

    /**
     *
     * @param duration
     * @param artist
     * @param album
     * @param progress
     * @param title
     */
    public SpeakerSong(String title, String artist, String album, String duration, String progress) {
        super();
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.progress = progress;
    }
    @Ignore
    protected SpeakerSong(Parcel in) {
        title = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readString();
        progress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(duration);
        dest.writeString(progress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpeakerSong> CREATOR = new Creator<SpeakerSong>() {
        @Override
        public SpeakerSong createFromParcel(Parcel in) {
            return new SpeakerSong(in);
        }

        @Override
        public SpeakerSong[] newArray(int size) {
            return new SpeakerSong[size];
        }
    };

    public String getTitle(){

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {

        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}
