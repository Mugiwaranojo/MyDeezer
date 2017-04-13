package mydevmind.com.mydeezer.fetcher;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

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
import mydevmind.com.mydeezer.model.Music;

/**
 * Created by Fitec on 25/06/2014.
 */
public class DownloadJsonTask extends AsyncTask<String, Void, String>  {


    private IOnRequestResultListener onConnectionResultListener;

    public DownloadJsonTask(IOnRequestResultListener onConnectionResultListener) {
       this.onConnectionResultListener= onConnectionResultListener;
    }

    @Override
    protected void onPostExecute(String result)
    {
        try {
            JSONObject jsonObj = new JSONObject(result);
            onConnectionResultListener.onRequestResult(jsonObj, null);
        } catch (JSONException e) {
            e.printStackTrace();
            onConnectionResultListener.onRequestResult(null, e);
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
