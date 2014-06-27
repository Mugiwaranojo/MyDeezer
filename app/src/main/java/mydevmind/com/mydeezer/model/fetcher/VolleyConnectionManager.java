package mydevmind.com.mydeezer.model.fetcher;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import mydevmind.com.mydeezer.model.BitmapLruCache;

/**
 * Created by Fitec on 27/06/2014.
 */
public class VolleyConnectionManager implements ConnectionManager {

    private Context context;
    private static RequestQueue mVolleyRequestQueue;
    private static ImageLoader mVolleyImageLoader;

    public  VolleyConnectionManager(Context context){
        // On initialise notre Thread-Pool et notre ImageLoader
        mVolleyRequestQueue = Volley.newRequestQueue(context);
        mVolleyImageLoader = new ImageLoader(mVolleyRequestQueue, new BitmapLruCache());
        mVolleyRequestQueue.start();
    }

    public static ImageLoader getmVolleyImageLoader(){
        return mVolleyImageLoader;
    }

    @Override
    public void performUrlRequest(String url, final OnConnectionResultListener listener) {
        // On va créer une Request pour Volley.
        // JsonArrayRequest hérite de Request et transforme automatiquement les données reçues en un JSONArray
        Log.d("query JSON to search : ", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Ce code est appelé quand la requête réussi. Étant ici dans le thread principal, on va pouvoir mettre à jour notre Adapter
                        listener.onConnectionResult(jsonObject, null);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onConnectionResult(null, new Exception());
                    }

        });
        request.setTag(this);
        // On ajoute la Request au RequestQueue pour la lancer
        mVolleyRequestQueue.add(request);
    }

    public void stop(){
        mVolleyRequestQueue.cancelAll(this);
        mVolleyRequestQueue.stop();
    }
}
