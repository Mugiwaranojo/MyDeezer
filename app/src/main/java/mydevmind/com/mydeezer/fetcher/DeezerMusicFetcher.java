package mydevmind.com.mydeezer.fetcher;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mydevmind.com.mydeezer.model.Music;
import mydevmind.com.mydeezer.model.MusicHelper;

/**
 * Created by Mugiwara on 13/04/2017.
 */

public class DeezerMusicFetcher implements IMusicFetcher, IOnRequestResultListener{

    private static final String DEEZER_SEARCH_URL = "http://api.deezer.com/search?q=";

    private Context context;
    private IOnMusicFetcherResultListener onMusicFetcherResultListener;
    private DownloadJsonTask jsonTask;

    public DeezerMusicFetcher(Context context) {
        this.context = context;
    }

    @Override
    public void fetchMusics(String textQuery, IOnMusicFetcherResultListener listener) {
        this.onMusicFetcherResultListener= listener;
        String url= DEEZER_SEARCH_URL+textQuery.replaceAll(" ", "%20");
        Log.d("query JSON to search : ", url);
        jsonTask = new DownloadJsonTask(this);
        jsonTask.execute(url);
    }

    @Override
    public void onRequestResult(JSONObject result, Exception e) {
        ArrayList<Music> musicArrayList = new ArrayList<Music>();
        try{
            // Getting JSON Array node
            JSONArray tracks = result.getJSONArray("data");
            for (int i = 0; i < tracks.length(); i++) {
                JSONObject track = tracks.getJSONObject(i);
                Music m = MusicHelper.decodeDeezerJson(context, track);
                musicArrayList.add(m);
            }
            onMusicFetcherResultListener.onMusicFetcherResult(musicArrayList, e);
        } catch (JSONException e1) {
            e1.printStackTrace();
            onMusicFetcherResultListener.onMusicFetcherResult(null, e1);
        }
    }

    @Override
    public void stop() {
        if(jsonTask!=null){
            jsonTask.cancel(true);
        }
    }
}
