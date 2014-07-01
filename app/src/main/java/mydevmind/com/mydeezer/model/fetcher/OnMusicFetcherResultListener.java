package mydevmind.com.mydeezer.model.fetcher;

import java.util.ArrayList;

import mydevmind.com.mydeezer.model.modelObject.Music;

/**
 * Created by Fitec on 27/06/2014.
 * Interface for query result
 */
public interface OnMusicFetcherResultListener {

    public void onMusicFetcherResult(ArrayList<Music> results, Exception e);
}
