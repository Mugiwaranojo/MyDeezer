package mydevmind.com.mydeezer.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import mydevmind.com.mydeezer.model.Music;

/**
 * Created by Mugiwara on 13/04/2017.
 */

public class FavoriteRepository implements IFavoriteRepository {

    private static FavoriteRepository instance;
    private DatabaseManager dbm;

    private FavoriteRepository(Context context){
        dbm = DatabaseManager.getInstance(context);
    }

    public static FavoriteRepository getInstance(Context context){
        if(instance==null){
            instance= new FavoriteRepository(context);
        }
        return instance;
    }

    @Override
    public boolean add(Music m) {
        if(isFavorite(m)) return false;
        ContentValues values = new ContentValues();
        values.put("title", m.getTitle());
        values.put("artist", m.getArtist());
        values.put("album", m.getAlbum());
        values.put("isFavorite", m.isFavorite() ? 1 : 0);
        values.put("sampleUrl", m.getSampleUrl());
        values.put("link", m.getLink());
        values.put("coverUrl", m.getCoverUrl());
        long line= dbm.getWritableDatabase().insert("favorite", null, values);
        return line != 0;
    }

    @Override
    public boolean remove(Music m) {
        String[] identifier = {m.getTitle(), m.getArtist(), m.getAlbum()};
        long line= dbm.getWritableDatabase().delete("favorite", "title=? and artist=? and album=?", identifier);
        return line != 0;
    }

    @Override
    public boolean isFavorite(Music m) {
        String[] identifier = {m.getTitle(), m.getArtist(), m.getAlbum()};
        Cursor c= dbm.getReadableDatabase().rawQuery("select * from favorite where title=? and artist=? and album=?;", identifier);
        return c.getCount()>0;
    }

    @Override
    public ArrayList<Music> getAll() {
        ArrayList<Music> musics = new ArrayList<Music>();
        Cursor c= dbm.getReadableDatabase().rawQuery("select * from favorite ", null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            Music m= new Music();
            m.setTitle(c.getString(0));
            m.setArtist(c.getString(1));
            m.setAlbum(c.getString(2));
            m.setFavorite(1==c.getInt(3));
            m.setSampleUrl(c.getString(4));
            m.setLink(c.getString(5));
            m.setCoverUrl(c.getString(6));
            musics.add(m);
            c.moveToNext();
        }
        c.close();
        return musics;
    }
}
