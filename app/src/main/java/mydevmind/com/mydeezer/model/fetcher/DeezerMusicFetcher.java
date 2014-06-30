package mydevmind.com.mydeezer.model.fetcher;

import android.content.Context;

/**
 * Created by Fitec on 27/06/2014.
 */
public class DeezerMusicFetcher implements MusicFetcher{

    private ConnectionManager connectionManager;

    private OnConnectionResultListener listener;

    @Override
    public void setOnConnectionResultListener(OnConnectionResultListener listener) {
        this.listener= listener;
    }
    public DeezerMusicFetcher(Context context){
        connectionManager = new VolleyConnectionManager(context);
    }

    public void setListener(OnConnectionResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void fetchMusicsForArtist(String artistName, OnMusicFetcherResultListener listener) {
        String url="http://api.deezer.com/search?q=";
        url+= artistName.replace(" ", "+").trim();
        connectionManager.performUrlRequest(url, this.listener);
    }
}
