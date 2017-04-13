package mydevmind.com.mydeezer.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import mydevmind.com.mydeezer.Repository.DatabaseManager;
import mydevmind.com.mydeezer.Repository.ManageFavorites;

/**
 * Created by Fitec on 25/06/2014.
 */
public class DownloadJsonTask extends AsyncTask<String, Void, String> {

    private ListView listView;
    private ArrayList<Music> musics;
    private ProgressDialog spinner;
    private DatabaseManager db;

    public DownloadJsonTask(Context context, ListView listView, ArrayList<Music> musics, ProgressDialog spinner) {
        this.listView = listView;
        this.musics = musics;
        this.spinner = spinner;
        this.db = DatabaseManager.getInstance(context);
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
                m.setFavorite(true);
                m.setSampleUrl(track.getString("preview"));
                m.setLink(track.getString("link"));
                m.setCoverUrl(track.getJSONObject("album").getString("cover"));
                if(!db.isFavorite(m)){
                    m.setFavorite(false);
                }
                musics.add(m);
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                spinner.dismiss();
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
        String result="";
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            result = convertStreamToString(is);
            is.close();
        } catch (IOException e) {
            Log.e("Hub", "Error getting the image from server : " + e.getMessage().toString());
        }

        return result;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
