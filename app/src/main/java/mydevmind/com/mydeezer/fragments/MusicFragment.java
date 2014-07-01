package mydevmind.com.mydeezer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

import mydevmind.com.mydeezer.R;
import mydevmind.com.mydeezer.model.Repository.DatabaseManager;
import mydevmind.com.mydeezer.model.fetcher.BitmapLruCache;
import mydevmind.com.mydeezer.model.modelObject.Music;

/**
 * Created by Joan on 24/06/2014.
 * Class fragment du details des musics
 */
public class MusicFragment extends Fragment{


    TextView fieldArtistView, fieldTitleView, fieldAlbumView;
    RadioButton favYesView, favNoView;
    NetworkImageView coverAlbumView;
    Button  listenButtonView, deezerButton;

    private RequestQueue mVolleyRequestQueue;
    private ImageLoader mVolleyImageLoader;

    private Music music;
    private final static MediaPlayer mediaPlayer = new MediaPlayer();

    private  ProgressDialog spinner;

    private DatabaseManager db;

    public void setDatabaseManager(DatabaseManager db){
        this.db= db;
    }

    private OnMusicEditedListener listener;

    public void setOnMusicEditedListener(OnMusicEditedListener listener) {
        this.listener = listener;
    }

    public Music getMusic() {
        return music;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_music, null);

        // On initialise notre Thread-Pool et notre ImageLoader
        mVolleyRequestQueue = Volley.newRequestQueue(v != null ? v.getContext() : null);
        mVolleyImageLoader = new ImageLoader(mVolleyRequestQueue, new BitmapLruCache());
        mVolleyRequestQueue.start();

        fieldTitleView= (TextView) (v != null ? v.findViewById(R.id.textViewSongTitle) : null);
        fieldAlbumView= (TextView) (v != null ? v.findViewById(R.id.textViewValuedAlbum) : null);
        fieldArtistView= (TextView) (v != null ?v.findViewById(R.id.textViewValuedArtist) : null);

        favYesView= (RadioButton) (v != null ? v.findViewById(R.id.radioButtonFavOui) : null);
        favYesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.setFavorite(true);
                listener.onMusicEdited(music);
                db.add(music);

            }
        });

        favNoView= (RadioButton) (v != null ? v.findViewById(R.id.radioButtonFavNon) : null);
        favNoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.setFavorite(false);
                listener.onMusicEdited(music);
                db.remove(music);
            }
        });


        coverAlbumView= (NetworkImageView) (v != null ? v.findViewById(R.id.imageViewAlbum) : null);

        listenButtonView= (Button) (v != null ? v.findViewById(R.id.buttonListen) : null);
        listenButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        deezerButton= (Button) (v != null ? v.findViewById(R.id.buttonDeezer) : null);
        deezerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchWebBrowser();
            }
        });

        spinner = new ProgressDialog(getActivity());

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
                spinner.dismiss();
            }
        });
        if(this.music != null){
            refresh();
        }
        return v;
    }

    @Override
    public void onDetach() {
        mVolleyRequestQueue.cancelAll(this);
        mVolleyRequestQueue.stop();
        super.onDetach();
    }

    public void setSelectedMusic(Music m){
        music=m;
    }

    public void refresh(){
        if(fieldTitleView!=null) {
            fieldTitleView.setText(music.getTitle());
            fieldAlbumView.setText(music.getAlbum());
            fieldArtistView.setText(music.getArtist());
            if (music.isFavorite()) {
                favYesView.setChecked(true);
            } else {
                favNoView.setChecked(true);
            }
            if (!music.getCoverUrl().equals("")) {
                coverAlbumView.setImageUrl(music.getCoverUrl(), mVolleyImageLoader);
                coverAlbumView.invalidate();
            }
            listenButtonView.setText(getString(R.string.song_play_button));
        }
    }

    private void launchWebBrowser(){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(music.getLink()));
        startActivity(intent);
    }

    public void play(){
        if(listenButtonView.getText().equals(getString(R.string.song_play_button))) {
            //afficher spinner;

            spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            spinner.setTitle("Chargement en cours...");
            spinner.setMessage("Patientez, ceci peut prendre quelques secondes");
            //spinner.setCancelable(false);
            spinner.show();

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getActivity(), Uri.parse(music.getSampleUrl()));
                mediaPlayer.prepareAsync();
                //mediaPlayer.start();
                listenButtonView.setText(getString(R.string.song_stop_button));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else {
            mediaPlayer.stop();
            listenButtonView.setText(getString(R.string.song_play_button));
        }
    }
}
