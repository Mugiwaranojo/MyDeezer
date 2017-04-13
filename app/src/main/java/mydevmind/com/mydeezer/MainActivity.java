package mydevmind.com.mydeezer;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import mydevmind.com.mydeezer.Repository.DatabaseManager;
import mydevmind.com.mydeezer.fragments.MusicFragment;
import mydevmind.com.mydeezer.fragments.MusicListFragment;
import mydevmind.com.mydeezer.model.IOnMusicSelected;
import mydevmind.com.mydeezer.model.Music;
import mydevmind.com.mydeezer.model.MusicAdapter;

/**
 * Created by Mugiwara on 13/04/2017.
 */

public class MainActivity extends AppCompatActivity implements IOnMusicSelected{

    MusicListFragment list;
    MusicFragment detail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if(findViewById(R.id.frameLayout)!=null){ //design telephone
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            list = new MusicListFragment();
            detail = new MusicFragment();

            getFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, list)
                    .commit();
        }else{  //design tablette
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            list= (MusicListFragment) getFragmentManager().findFragmentById(R.id.fragment);
            detail= (MusicFragment) getFragmentManager().findFragmentById(R.id.fragment2);
            getFragmentManager().beginTransaction()
                    .hide(detail)
                    .commit();
        }
        list.setOnMusicSelectedListener(this);
        detail.setOnFavoriteChangeListener(list);
    }

    @Override
    public void onMusicSelected(Music music) {
        if(findViewById(R.id.frameLayout)!=null){
            if(detail.isAdded()){
                getFragmentManager().beginTransaction()
                        .hide(list)
                        .show(detail)
                        .commit();
            }else {
                getFragmentManager().beginTransaction()
                        .hide(list)
                        .add(R.id.frameLayout, detail)
                        .commit();
            }
        }else{
            getFragmentManager().beginTransaction()
                    .show(detail)
                    .commit();
        }
        detail.setSelectedMusic(music);
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.frameLayout)!=null && detail.isAdded()) {
            getFragmentManager().beginTransaction()
                    .hide(detail)
                    .show(list)
                    .commit();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_favorites){
            list.setAllFavorisInList();
            if(findViewById(R.id.frameLayout)!=null && detail.isAdded()) {
                getFragmentManager().beginTransaction()
                        .hide(detail)
                        .show(list)
                        .commit();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
