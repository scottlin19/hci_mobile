package ar.edu.itba.hci.api.models.devices.states;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpeakerSong extends DeviceState {
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

    public String getTitle() {
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
}
