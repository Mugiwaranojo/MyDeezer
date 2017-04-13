package mydevmind.com.mydeezer.fetcher;

import java.util.ArrayList;

import mydevmind.com.mydeezer.model.Music;

/**
 * Created by Fitec on 27/06/2014.
 * Interface for query result
 */
public interface IOnMusicFetcherResultListener {

    public void onMusicFetcherResult(ArrayList<Music> results, Exception e);
}
