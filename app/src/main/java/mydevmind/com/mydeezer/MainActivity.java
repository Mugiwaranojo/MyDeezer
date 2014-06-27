package mydevmind.com.mydeezer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import mydevmind.com.mydeezer.fragments.MusicFragment;
import mydevmind.com.mydeezer.fragments.MusicListFragment;
import mydevmind.com.mydeezer.fragments.OnMusicEditedListener;
import mydevmind.com.mydeezer.fragments.OnMusicSelectedListener;
import mydevmind.com.mydeezer.model.modelObject.Music;

public class MainActivity extends Activity implements OnMusicSelectedListener, OnMusicEditedListener {

    MusicListFragment list;
    MusicFragment detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.frameLayout)!=null){ //design telephone
            list = new MusicListFragment();
            detail = new MusicFragment();

            getFragmentManager().beginTransaction()
            .add(R.id.frameLayout, list)
            .commit();
        }else{  //design tablette
            list= (MusicListFragment) getFragmentManager().findFragmentById(R.id.musicListFragment);
            detail= (MusicFragment) getFragmentManager().findFragmentById(R.id.musicFragment);
        }
        list.setOnMusicSelectedListener(this);
        list.setOnMusicEditedListener(this);
        detail.setOnMusicEditedListener(this);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
