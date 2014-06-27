package mydevmind.com.mydeezer.model.modelObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fitec on 23/06/2014.
 */
public class Music implements Serializable{

    private String title;
    private String artist;
    private String album;
    private Integer duration;
    private boolean isFavorite;
    private String sampleUrl;
    private String link;
    private String coverUrl;

    public Music(){
        this.title = "";
        this.artist = "";
        this.album = "";
        this.duration = 0;
        this.isFavorite = false;
        this.sampleUrl = "";
        this.link = "";
        this.coverUrl = "";
    }

    public Music(String title, String artist, String album, Integer duration, boolean isFavorite, String sampleUrl, String link, String coverUrl) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.isFavorite = isFavorite;
        this.sampleUrl = sampleUrl;
        this.link = link;
        this.coverUrl = coverUrl;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getSampleUrl() {
        return sampleUrl;
    }

    public void setSampleUrl(String sampleUrl) {
        this.sampleUrl = sampleUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public static Music getDefaultMusic(){
        return new Music("Her favorite song", "Mayer Hawthorne", "Her favorite song", 30, true,
                         "https://api.spotify.com/v1/albums/1d1EbySoXWEVztSUsgjpOY",
                         "https://open.spotify.com/album/1d1EbySoXWEVztSUsgjpOY",
                         "https://i.scdn.co/image/de86ae567463a2d3577cafbcd76bd248e376c027");
    }


}
