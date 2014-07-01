package mydevmind.com.mydeezer.model.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mydevmind.com.mydeezer.model.modelObject.Music;

/**
 * Created by Joan on 01/07/2014.
 * DatabaseManager
 */
public class DatabaseManager extends SQLiteOpenHelper implements IFavoriteRepository, IHistoryRepository {

    private static final String DATABASE_NAME= "mydb.sqlite";
    private static final int CURRENT_DB_VERSION = 1;

    public DatabaseManager(Context context, int version) {
        super(context, DATABASE_NAME, null, CURRENT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table history\n"+
                               "(request \"VARCHAR()\" not null\n"+
                               ");");
        sqLiteDatabase.execSQL("create table favorite\n"+
                               "(identifier \"VARCHAR()\" not null\n"+
                               ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        switch (i){
            case 1:
                //code sql 1->2
            case 2:
                //code sql 2->3
            case 3:
                //code sql 3->4
            default:
        }
    }

    @Override
    public boolean add(Music m) {
        if(isFavorite(m)) return false;
        ContentValues values = new ContentValues();
        values.put("identifier", m.getIdProvider());
        long line= getWritableDatabase().insert("favorite", null, values);
        return line != 0;
    }

    @Override
    public boolean remove(Music m) {
        long line=getWritableDatabase().delete("favorite", "identifier="+m.getIdProvider(), null);
        return line != 0;
    }

    @Override
    public boolean isFavorite(Music m) {
        Cursor c= getReadableDatabase().rawQuery("select identifier from favorite where identifier='"+m.getIdProvider()+"';", null);
        return c.getCount()>0;
    }

    @Override
    public boolean addToHistory(String request) {
        String requestLowerCase= request.toLowerCase();
        if(!existInHistory(requestLowerCase)) {
            ContentValues values = new ContentValues();
            values.put("request", request);
            long line = getWritableDatabase().insert("history", null, values);
            return line != 0;
        }else{
            return  false;
        }
    }

    private boolean existInHistory(String requestLowerCase) {
        Cursor c= getReadableDatabase().rawQuery("select request from history where request='"+requestLowerCase+"';", null);
        return c.getCount()>0;
    }

    @Override
    public ArrayList<String> getRequestMatchingPredicate(String predicate) {
        Cursor c= getReadableDatabase().rawQuery("select request from history where request like '"+predicate.toLowerCase()+"%';", null);
        c.moveToFirst();
        ArrayList<String> result= new ArrayList<String>();
        while (!c.isAfterLast()){
            String tempRequest= c.getString(0);
            result.add(tempRequest);
            c.moveToNext();
        }
        c.close();
        return result;
    }
}
