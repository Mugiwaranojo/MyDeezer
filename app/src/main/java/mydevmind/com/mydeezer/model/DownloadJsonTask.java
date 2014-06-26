package mydevmind.com.mydeezer.model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.androidquery.util.XmlDom;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fitec on 25/06/2014.
 */
public class DownloadJsonTask extends AsyncTask<String, Void, String> {

    private ListView listView;
    private ArrayList<Music> musics;
    private Activity context;

    public DownloadJsonTask(Activity context, ListView listView, ArrayList<Music> musics) {
        this.context= context;
        this.listView = listView;
        this.musics= musics;
    }

    @Override
    protected void onPostExecute(String result)
    {
        musics.clear();
        try {
            JSONObject jsonObj = new JSONObject(result);
            // Getting JSON Array node
            JSONArray tracks = jsonObj.getJSONArray("data");
            for (int i = 0; i < tracks.length(); i++) {
                JSONObject track = tracks.getJSONObject(i);
                Music m = new Music();
                m.setTitle(track.getString("title"));
                m.setArtist(track.getJSONObject("artist").getString("name"));
                m.setAlbum(track.getJSONObject("album").getString("title"));
                m.setDuration(track.getInt("duration"));
                m.setFavorite(false);
                m.setSampleUrl(track.getString("preview"));
                m.setLink(track.getString("link"));
                m.setCoverUrl(track.getJSONObject("album").getString("cover"));
                musics.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(String... strings) {
        return download_xmlDom(strings[0]);
    }

    private String download_xmlDom(String url){
        return "";
    }

}
