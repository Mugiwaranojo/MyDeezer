package mydevmind.com.mydeezer.model.fetcher;

import java.util.ArrayList;

import mydevmind.com.mydeezer.model.modelObject.Music;

/**
 * Created by Fitec on 27/06/2014.
 * Dummy provider
 */
public class DummyMusicFetcher implements MusicFetcher {


    @Override
    public void fetchMusicsForArtist(String artistName, OnMusicFetcherResultListener listener) {

        ArrayList<Music> allMusics= new ArrayList<Music>();
        for(int i=0; i<30; i++){
            int track= 1+i;
            allMusics.add(i,new Music("test"+i, "track - "+track, "Unknown Artist", "Unknown Album", 30, false, "", "", "https://i.scdn.co/image/de86ae567463a2d3577cafbcd76bd248e376c027"));
        }
        listener.onMusicFetcherResult(allMusics, null);
    }

    @Override
    public void setOnConnectionResultListener(OnConnectionResultListener listener) {

    }

    @Override
    public void stop() {

    }
}
