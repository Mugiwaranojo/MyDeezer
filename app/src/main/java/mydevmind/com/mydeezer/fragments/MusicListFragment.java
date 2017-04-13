package mydevmind.com.mydeezer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mydevmind.com.mydeezer.R;
import mydevmind.com.mydeezer.Repository.DatabaseManager;
import mydevmind.com.mydeezer.model.DownloadJsonTask;
import mydevmind.com.mydeezer.model.IOnFavoriteChange;
import mydevmind.com.mydeezer.model.IOnMusicSelected;
import mydevmind.com.mydeezer.model.Music;
import mydevmind.com.mydeezer.model.MusicAdapter;


public class MusicListFragment extends Fragment implements IOnFavoriteChange {

    private static final int ACTION_SELECT = 1;
    private static final int ACTION_FAV_ON = 2;
    private static final int ACTION_FAV_OFF = 3;

    private static final String DEEZER_SEARCH_URL = "http://api.deezer.com/search?q=";

    private ListView listViewMusics;
    private ArrayList<Music> musics;
    private TextView searchText;
    private ImageButton searchButton;

    private IOnMusicSelected onMusicSelectedListener;

    public void setOnMusicSelectedListener(IOnMusicSelected onMusicSelectedListener) {
        this.onMusicSelectedListener = onMusicSelectedListener;
    }

    private MusicAdapter adapter;

    private DatabaseManager db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.layout_music_list, null);

        db = DatabaseManager.getInstance(getActivity());
        musics = new ArrayList<Music>();

        listViewMusics = (ListView) v.findViewById(R.id.listMainSearch);
        searchText = (TextView) v.findViewById(R.id.editTextSearch);

        adapter= new MusicAdapter(getActivity(), musics);

        listViewMusics.setAdapter(adapter);
        listViewMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onMusicSelectedListener.onMusicSelected(musics.get(position));
            }
        });

        listViewMusics.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(Menu.NONE, ACTION_SELECT, 0, getString(R.string.main_ctx_select));
                AdapterView.AdapterContextMenuInfo adapterContext = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
                contextMenu.setHeaderTitle(musics.get(adapterContext.position).getTitle());
                if (musics.get(adapterContext.position).isFavorite()) {
                    contextMenu.add(Menu.NONE, ACTION_FAV_OFF, 1, getString(R.string.main_ctx_favoris_off));
                } else {
                    contextMenu.add(Menu.NONE, ACTION_FAV_ON, 1, getString(R.string.main_ctx_favoris_on));
                }
            }
        });

        searchButton = (ImageButton) v.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMusics(searchText.getText().toString());
            }
        });

        return v;
    }

    public void searchMusics(String textSearch){
        String url= DEEZER_SEARCH_URL+textSearch.replaceAll(" ", "%20");
        Log.d("query JSON to search : ", url);

        // Afficher le spinner (busy message)
        final ProgressDialog spinner = new ProgressDialog(getActivity());
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        spinner.setTitle("Loading songs of " + textSearch);
        spinner.setMessage("Find songs in progress...");
        spinner.setCancelable(false);
        spinner.show();

        DownloadJsonTask jsonTask = new DownloadJsonTask(getActivity(), listViewMusics, musics, spinner);
        jsonTask.execute(url);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case ACTION_SELECT:
                onMusicSelectedListener.onMusicSelected(musics.get(info.position));
                break;
            case ACTION_FAV_ON:
                musics.get(info.position).setFavorite(true);
                db.add(musics.get(info.position));
                Toast.makeText(getActivity(), getString(R.string.main_ctx_favoris_toaston), Toast.LENGTH_SHORT).show();
                break;
            case ACTION_FAV_OFF:
                 db.remove(musics.get(info.position));
                musics.get(info.position).setFavorite(false);
                Toast.makeText(getActivity(), getString(R.string.main_ctx_favoris_toastoff), Toast.LENGTH_SHORT).show();
                break;
        }
        ((BaseAdapter) listViewMusics.getAdapter()).notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    @Override
    public void onFavoriteChange(Music m, boolean isFavorite) {
        musics.get(musics.indexOf(m)).setFavorite(isFavorite);
        ((BaseAdapter) listViewMusics.getAdapter()).notifyDataSetChanged();
    }

    public void setAllFavorisInList(){
        this.musics = db.getAll();
        adapter= new MusicAdapter(getActivity(), musics);
        listViewMusics.setAdapter(adapter);
        ((BaseAdapter) listViewMusics.getAdapter()).notifyDataSetChanged();
    }
}
