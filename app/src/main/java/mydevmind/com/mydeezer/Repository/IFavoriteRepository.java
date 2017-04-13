package mydevmind.com.mydeezer.Repository;

import java.util.ArrayList;

import mydevmind.com.mydeezer.MusicListActivity;
import mydevmind.com.mydeezer.model.Music;

/**
 * Created by Fitec on 30/06/2014.
 */
public interface IFavoriteRepository {

    public boolean add(Music m);

    public boolean remove(Music m);

    public boolean isFavorite(Music m);

    public ArrayList<Music> getAll();

}
