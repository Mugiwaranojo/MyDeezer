package mydevmind.com.mydeezer.model.fetcher;


import org.json.JSONObject;

/**
 * Created by Fitec on 27/06/2014.
 */
public interface OnConnectionResultListener {

    public void onConnectionResult(JSONObject result, Exception e);
}