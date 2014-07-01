package mydevmind.com.mydeezer;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import mydevmind.com.mydeezer.fragments.MusicFragment;
import mydevmind.com.mydeezer.fragments.MusicListFragment;
import mydevmind.com.mydeezer.fragments.OnMusicEditedListener;
import mydevmind.com.mydeezer.fragments.OnMusicSelectedListener;
import mydevmind.com.mydeezer.model.Repository.DatabaseManager;
import mydevmind.com.mydeezer.model.modelObject.Music;

public class MainActivity extends Activity implements OnMusicSelectedListener, OnMusicEditedListener {

    MusicListFragment list;
    MusicFragment detail;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManager(this, 1);

        if(findViewById(R.id.frameLayout)!=null){ //design telephone
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            list = new MusicListFragment();
            detail = new MusicFragment();

            getFragmentManager().beginTransaction()
            .add(R.id.frameLayout, list)
            .commit();
        }else{  //design tablette
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            list= (MusicListFragment) getFragmentManager().findFragmentById(R.id.musicListFragment);
            detail= (MusicFragment) getFragmentManager().findFragmentById(R.id.musicFragment);
            getFragmentManager().beginTransaction()
                    .hide(detail)
                    .commit();
        }
        list.setOnMusicSelectedListener(this);
        list.setOnMusicEditedListener(this);
        list.setDatabaseManager(db);
        detail.setOnMusicEditedListener(this);
        detail.setDatabaseManager(db);
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
        detail.refresh();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onMusicEdited(Music music) {
        if(findViewById(R.id.frameLayout)==null) { //design tablet
            list.refresh();
            if(detail.getMusic().equals(music)){
                detail.refresh();
            }
        }
    }
}
