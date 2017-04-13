package mydevmind.com.mydeezer.fetcher;

/**
 * Created by Fitec on 27/06/2014.
 * Interface musicFetcher for providers
 */
public interface IMusicFetcher {

    public void fetchMusics(String textQuery, IOnMusicFetcherResultListener listener);
    public void stop();
}
