package mydevmind.com.mydeezer.fragments;

import mydevmind.com.mydeezer.model.Music;

/**
 * Created by Mugiwara on 12/04/2017.
 */

public interface IOnFavoriteChange {

    public void onFavoriteChange(Music m, boolean isFavorite);
}
