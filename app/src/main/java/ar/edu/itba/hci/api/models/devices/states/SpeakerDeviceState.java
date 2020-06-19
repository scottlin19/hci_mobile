package ar.edu.itba.hci.api.models.devices.states;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("song")
    @Expose
    private SpeakerSong song;

    /**
     * No args constructor for use in serialization
     *
     */
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
}
