package mydevmind.com.mydeezer.fragments;

import android.app.Fragment;
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
import java.net.URI;

import mydevmind.com.mydeezer.R;
import mydevmind.com.mydeezer.model.BitmapLruCache;
import mydevmind.com.mydeezer.model.modelObject.Music;

/**
 * Created by Joan on 24/06/2014.
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
        mVolleyRequestQueue = Volley.newRequestQueue(v.getContext());
        mVolleyImageLoader = new ImageLoader(mVolleyRequestQueue, new BitmapLruCache());
        mVolleyRequestQueue.start();

        fieldTitleView= (TextView) v.findViewById(R.id.textViewSongTitle);
        fieldAlbumView= (TextView) v.findViewById(R.id.textViewValuedAlbum);
        fieldArtistView= (TextView) v.findViewById(R.id.textViewValuedArtist);

        favYesView= (RadioButton) v.findViewById(R.id.radioButtonFavOui);
        favYesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.setFavorite(true);
                listener.onMusicEdited(music);
            }
        });

        favNoView= (RadioButton) v.findViewById(R.id.radioButtonFavNon);
        favNoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                music.setFavorite(false);
                listener.onMusicEdited(music);
            }
        });


        coverAlbumView= (NetworkImageView) v.findViewById(R.id.imageViewAlbum);

        listenButtonView= (Button) v.findViewById(R.id.buttonListen);
        listenButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        deezerButton= (Button) v.findViewById(R.id.buttonDeezer);
        deezerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchWebBrowser();
            }
        });
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if(this.music == null){
            music= Music.getDefaultMusic();
        }
        refresh();
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
            if (music.getCoverUrl() != "") {
                coverAlbumView.setImageUrl(music.getCoverUrl(), mVolleyImageLoader);
                coverAlbumView.invalidate();
            }
            if(!mediaPlayer.isPlaying()){
               listenButtonView.setText(getString(R.string.song_play_button));
            }else{
               listenButtonView.setText(getString(R.string.song_stop_button));
            }
        }
    }

    private void launchWebBrowser(){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(music.getLink()));
        startActivity(intent);
    }

    public void play(){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getActivity(), Uri.parse(music.getSampleUrl()));
            mediaPlayer.prepare();
            mediaPlayer.start();
            listenButtonView.setText(getString(R.string.song_stop_button));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
