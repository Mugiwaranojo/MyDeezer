package mydevmind.com.mydeezer.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mydevmind.com.mydeezer.model.Music;

/**
 * Created by Mugiwara on 12/04/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "mydb.sqlite";
    private static final int CURRENT_DB_VERSION = 1;

    private static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context){
        if(instance==null){
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, CURRENT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       sqLiteDatabase.execSQL("create table favorite "+
                "(title TEXT, artist TEXT, album TEXT, isFavorite INTEGER, "+
                "sampleUrl TEXT, link TEXT, coverUrl TEXT);");
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
}
