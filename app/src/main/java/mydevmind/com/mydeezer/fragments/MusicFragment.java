package mydevmind.com.mydeezer.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;

import mydevmind.com.mydeezer.R;
import mydevmind.com.mydeezer.Repository.FavoriteRepository;
import mydevmind.com.mydeezer.fetcher.DownloadImagesTask;
import mydevmind.com.mydeezer.model.Music;

/**
 * Created by Joan on 24/06/2014.
 */
public class MusicFragment extends Fragment {

    TextView fieldArtistView, fieldTitleView, fieldAlbumView;
    RadioButton favYesView, favNoView;
    ImageView coverAlbumView;
    Button  listenButtonView, deezerButton;

    private IOnFavoriteChange onFavoriteChangeListener;

    public void setOnFavoriteChangeListener(IOnFavoriteChange onFavoriteChangeListener) {
        this.onFavoriteChangeListener = onFavoriteChangeListener;
    }

    private Music tempM;

    private FavoriteRepository favoriteRepository;

    private MediaPlayer player = new MediaPlayer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.layout_music, null);

        favoriteRepository = FavoriteRepository.getInstance(getActivity());

        fieldTitleView= (TextView)  v.findViewById(R.id.textViewSongTitle);
        fieldAlbumView= (TextView)  v.findViewById(R.id.textViewValuedAlbum);
        fieldArtistView= (TextView) v.findViewById(R.id.textViewValuedArtist);

        favYesView= (RadioButton) v.findViewById(R.id.radioButtonFavOui);
        favYesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempM.setFavorite(true);
                favoriteRepository.add(tempM);
                onFavoriteChangeListener.onFavoriteChange(tempM, true);

            }
        });

        favNoView= (RadioButton) v.findViewById(R.id.radioButtonFavNon);
        favNoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempM.setFavorite(false);
                favoriteRepository.remove(tempM);
                onFavoriteChangeListener.onFavoriteChange(tempM, false);
            }
        });

        coverAlbumView= (ImageView) v.findViewById(R.id.imageViewAlbum);

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
        refresh();
        return v;
    }


    public void setSelectedMusic(Music m){
        tempM= m;
        if(fieldTitleView!=null) refresh();
    }

    public void refresh(){
        fieldTitleView.setText(tempM.getTitle());
        fieldAlbumView.setText(tempM.getAlbum());
        fieldArtistView.setText(tempM.getArtist());
        if(tempM.isFavorite()){
            favYesView.setChecked(true);
        }else{
            favNoView.setChecked(true);
        }
        if(!tempM.getCoverUrl().equals("")) {
            coverAlbumView.setImageResource(android.R.drawable.ic_menu_gallery);
            DownloadImagesTask imagesTask= new DownloadImagesTask(coverAlbumView);
            imagesTask.execute(tempM.getCoverUrl());
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
                player.setDataSource(getActivity(), Uri.parse(this.tempM.getSampleUrl()));
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
