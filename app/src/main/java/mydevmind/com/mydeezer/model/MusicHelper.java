package mydevmind.com.mydeezer.model;

import android.content.Context;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mydevmind.com.mydeezer.Repository.FavoriteRepository;

/**
 * Created by Mugiwara on 14/04/2017.
 */

public class MusicHelper {

    public static Music decodeDeezerJson(Context context, JSONObject object) throws JSONException {
        JSONObject track = object;
        Music m = new Music();
        m.setTitle(track.getString("title"));
        m.setArtist(track.getJSONObject("artist").getString("name"));
        m.setAlbum(track.getJSONObject("album").getString("title"));
        m.setFavorite(true);
        m.setSampleUrl(track.getString("preview"));
        m.setLink(track.getString("link"));
        m.setCoverUrl(track.getJSONObject("album").getString("cover"));
        if (!FavoriteRepository.getInstance(context).isFavorite(m)) {
            m.setFavorite(false);
        }
        return m;
    }
}
