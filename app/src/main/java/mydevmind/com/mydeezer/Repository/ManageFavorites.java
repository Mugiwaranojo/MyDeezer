package mydevmind.com.mydeezer.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mydevmind.com.mydeezer.fragments.IOnFavoriteChange;
import mydevmind.com.mydeezer.model.Music;

/**
 * Created by Mugiwara on 11/04/2017.
 */

public class ManageFavorites {

     private static IOnFavoriteChange listener;

    public static void setListener(IOnFavoriteChange listener) {
        ManageFavorites.listener = listener;
    }

    public static void save(Context context, ArrayList<Music> musics){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(musics);
        prefsEditor.putString("musicsFavorites", json);
        prefsEditor.commit();
    }

    public  static ArrayList<Music> load(Context context){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("musicsFavorites", "");
        List<Music> musics;
        if(json.equals("")){
            musics = new ArrayList<Music>();
        }else {
            Music[] favoritesMusics = gson.fromJson(json, Music[].class);
            musics = Arrays.asList(favoritesMusics);
            musics = new ArrayList<Music>(musics);
        }
        return (ArrayList<Music>) musics;
    }

    public static void add(Context context, Music music){
        ArrayList<Music> favorites = load(context);
        if(!favorites.contains(music)) {
            favorites.add(music);
            listener.onFavoriteChange(music, true);
        }
        save(context, favorites);
    }

    public static void remove(Context context, Music music){
        ArrayList<Music> favorites = load(context);
        favorites.remove(music);
        listener.onFavoriteChange(music, false);
        save(context, favorites);
    }

    public static boolean isFavorite(Context context, Music music){
        ArrayList<Music> favorites = load(context);
        return favorites.contains(music);
    }
}
