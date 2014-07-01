package mydevmind.com.mydeezer.model.fetcher;

/**
 * Created by Fitec on 27/06/2014.
 * Interface musicFetcher for providers
 */
public interface MusicFetcher {

    public void fetchMusicsForArtist(String artistName, OnMusicFetcherResultListener listener);
    public void setOnConnectionResultListener(OnConnectionResultListener listener);
    public void stop();
}
