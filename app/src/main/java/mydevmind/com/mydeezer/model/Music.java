package mydevmind.com.mydeezer.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fitec on 23/06/2014.
 */
public class Music implements Serializable{

    private String title;
    private String artist;
    private String album;
    private boolean isFavorite;
    private String sampleUrl;
    private String link;
    private String coverUrl;

    public Music(){
        this.title = "";
        this.artist = "";
        this.album = "";
        this.isFavorite = false;
        this.sampleUrl = "";
        this.link = "";
        this.coverUrl = "";
    }

    public Music(String title, String artist, String album, boolean isFavorite, String sampleUrl, String link, String coverUrl) {
        this.title = title;
        this.artist = artist;
        this.album = album;
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
        return new Music("Her favorite song", "Mayer Hawthorne", "Her favorite song", true,
                         "https://api.spotify.com/v1/albums/1d1EbySoXWEVztSUsgjpOY",
                         "https://open.spotify.com/album/1d1EbySoXWEVztSUsgjpOY",
                         "https://i.scdn.co/image/de86ae567463a2d3577cafbcd76bd248e376c027");
    }

    public static ArrayList<Music> getAllMusics(int n){
        ArrayList<Music> allMusics= new ArrayList<Music>(n);
        for(int i=0; i<n; i++){
            int track= 1+i;
            allMusics.add(i,new Music("track - "+track, "Unknown Artist", "Unknown Album", false, "", "", "https://i.scdn.co/image/de86ae567463a2d3577cafbcd76bd248e376c027"));
        }
        return allMusics;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Music){
            Music tmp= (Music) o;
            return this.artist.equals(tmp.artist) && this.album.equals(tmp.album) && this.title.equals(tmp.title);
        }else {
            return false;
        }
    }
}
