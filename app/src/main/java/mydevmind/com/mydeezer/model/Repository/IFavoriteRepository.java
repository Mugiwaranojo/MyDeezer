package mydevmind.com.mydeezer.model.Repository;

import mydevmind.com.mydeezer.model.modelObject.Music;

/**
 * Created by Fitec on 30/06/2014.
 */
public interface IFavoriteRepository {

    public boolean add(Music m);

    public boolean remove(Music m);

    public boolean isFavorite(Music m);

}
