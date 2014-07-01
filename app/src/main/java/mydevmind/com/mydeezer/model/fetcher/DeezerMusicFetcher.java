package mydevmind.com.mydeezer.model.fetcher;

import android.content.Context;

/**
 * Created by Joan on 27/06/2014.
 * Provider fetch musics
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

    @Override
    public void fetchMusicsForArtist(String artistName, OnMusicFetcherResultListener listener) {
        connectionManager.cancelAll();
        String url="http://api.deezer.com/search?q=";
        url+= artistName.trim().replace(" ", "+");
        connectionManager.performUrlRequest(url, this.listener);
    }

    @Override
    public void stop(){
        connectionManager.stop();
    }
}
