package mydevmind.com.mydeezer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

import mydevmind.com.mydeezer.Repository.DatabaseManager;
import mydevmind.com.mydeezer.model.BitmapLruCache;
import mydevmind.com.mydeezer.Repository.ManageFavorites;
import mydevmind.com.mydeezer.model.Music;

/**
 * Created by Joan on 24/06/2014.
 */
public class MusicActivity extends Activity {

    TextView fieldArtistView, fieldTitleView, fieldAlbumView;
    RadioButton favYesView, favNoView;
    NetworkImageView coverAlbumView;
    Button  listenButtonView, deezerButton;

    private RequestQueue mVolleyRequestQueue;
    private ImageLoader mVolleyImageLoader;
    private Music tempM;

    private DatabaseManager db;

    private MediaPlayer player = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        db = new DatabaseManager(this, 1);

        // On initialise notre Thread-Pool et notre ImageLoader
        mVolleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mVolleyImageLoader = new ImageLoader(mVolleyRequestQueue, new BitmapLruCache());
        mVolleyRequestQueue.start();

        fieldTitleView= (TextView) findViewById(R.id.textViewSongTitle);
        fieldAlbumView= (TextView) findViewById(R.id.textViewValuedAlbum);
        fieldArtistView= (TextView) findViewById(R.id.textViewValuedArtist);

        favYesView= (RadioButton) findViewById(R.id.radioButtonFavOui);
        favYesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempM.setFavorite(true);
                //ManageFavorites.add(getApplicationContext(), tempM);
                db.add(tempM);
            }
        });

        favNoView= (RadioButton) findViewById(R.id.radioButtonFavNon);
        favNoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempM.setFavorite(false);
                //ManageFavorites.remove(getApplicationContext(), tempM);
                db.remove(tempM);
            }
        });

        coverAlbumView= (NetworkImageView) findViewById(R.id.imageViewAlbum);

        listenButtonView= (Button) findViewById(R.id.buttonListen);
        listenButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        deezerButton= (Button) findViewById(R.id.buttonDeezer);
        deezerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchWebBrowser();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent myIntent= getIntent();
        Music music= (Music) myIntent.getSerializableExtra("musicSelected");
        setSelectedMusic(music);
    }

    @Override
    protected void onStop() {
        mVolleyRequestQueue.cancelAll(this);
        mVolleyRequestQueue.stop();
        super.onStop();
    }

    public void setSelectedMusic(Music m){
        tempM= m;
        fieldTitleView.setText(m.getTitle());
        fieldAlbumView.setText(m.getAlbum());
        fieldArtistView.setText(m.getArtist());
        if(m.isFavorite()){
            favYesView.setChecked(true);
        }else{
            favNoView.setChecked(true);
        }
        if(m.getCoverUrl()!="") {
            coverAlbumView.setImageUrl(m.getCoverUrl(), mVolleyImageLoader);
            coverAlbumView.invalidate();
        }
    }

    private void launchWebBrowser(){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(tempM.getLink()));
        startActivity(intent);
    }

    public void play(){
        try {
            if(!player.isPlaying()) {
                player.reset();
                player.setDataSource(this, Uri.parse(this.tempM.getSampleUrl()));
                player.prepare();
                player.start();
            }else{
                player.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
