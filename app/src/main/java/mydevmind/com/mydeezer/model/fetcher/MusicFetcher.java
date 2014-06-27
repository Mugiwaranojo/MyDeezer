package mydevmind.com.mydeezer.model.fetcher;

import java.util.ArrayList;

import mydevmind.com.mydeezer.model.modelObject.Music;

/**
 * Created by Fitec on 27/06/2014.
 */
public interface MusicFetcher {

    public void fetchMusicsForArtist(String artistName, OnMusicFetcherResultListener listener);
    public void setOnConnectionResultListener(OnConnectionResultListener listener);
}
