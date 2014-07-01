package mydevmind.com.mydeezer.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import mydevmind.com.mydeezer.R;
import mydevmind.com.mydeezer.model.Repository.DatabaseManager;
import mydevmind.com.mydeezer.model.fetcher.DeezerMusicFetcher;
import mydevmind.com.mydeezer.model.fetcher.MusicFetcher;
import mydevmind.com.mydeezer.model.fetcher.OnConnectionResultListener;
import mydevmind.com.mydeezer.model.fetcher.OnMusicFetcherResultListener;
import mydevmind.com.mydeezer.model.modelObject.Music;


public class MusicListFragment extends Fragment implements OnConnectionResultListener {

    private static final int ACTION_SELECT = 1234;
    private static final int ACTION_FAV_ON = 1235;
    private static final int ACTION_FAV_OFF = 1236;

    private ListView listViewMusics;
    private TextView searchText;

    private ArrayList<Music> musics;
    private MusicAdapter adapter;
    private MusicFetcher fetcher;

    private DatabaseManager db;

    public void setDatabaseManager(DatabaseManager db){
        this.db= db;
    }


    //listeners
    private OnMusicSelectedListener onMusicSelectedListener;
    private OnMusicEditedListener onMusicEditedListener;

    public void setOnMusicSelectedListener(OnMusicSelectedListener onMusicSelectedListener) {
        this.onMusicSelectedListener = onMusicSelectedListener;
    }

    public void setOnMusicEditedListener(OnMusicEditedListener onMusicEditedListener) {
        this.onMusicEditedListener = onMusicEditedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_music_list, null);

        db= new DatabaseManager(getActivity(), 1);

        //list des musics
        musics = new ArrayList<Music>();
        fetcher = new DeezerMusicFetcher(getActivity().getBaseContext());
        fetcher.setOnConnectionResultListener(this);

        listViewMusics = (ListView) v.findViewById(R.id.listMainSearch);
        searchText = (TextView) v.findViewById(R.id.editTextSearch);
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(!searchText.getText().toString().equals("")) {
                    final ArrayList<String> listQueries = db.getRequestMatchingPredicate(searchText.getText().toString());
                    for(int n=0; n<listQueries.size(); n++){
                        Log.d("history registred "+n, listQueries.get(n));
                    }
                    ArrayAdapter<String> adapterPredicate = new ArrayAdapter<String>(getActivity(), R.layout.search_list, listQueries);
                    listViewMusics.setAdapter(adapterPredicate);
                    listViewMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String request=listQueries.get(i);
                            searchText.setText(request);
                            loadMusicsAsync();
                        }
                    });
                }
                return false;
            }
        });

        adapter= new MusicAdapter(getActivity(), musics);
        listViewMusics.setAdapter(adapter);
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

        ImageButton searchButton = (ImageButton) v.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.clearFocus();
                loadMusicsAsync();
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                searchText.clearFocus();
                loadMusicsAsync();
                return true;
            }
        });

        return v;
    }

    @Override
    public void onConnectionResult(JSONObject result, Exception e) {
        if(e==null){
            adapter.updateMembers(result, db);
        }else{
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    private void loadMusicsAsync() {
        String artist= searchText.getText().toString();
        db.addToHistory(artist);
        //afficher spinner;
        final ProgressDialog spinner = new ProgressDialog(getActivity());
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        spinner.setTitle(getString(R.string.find_spinner_title));
        spinner.setMessage(getString(R.string.find_spinner_text));
        //spinner.setCancelable(false);
        spinner.show();

        musics.clear();
        listViewMusics.setAdapter(adapter);
        listViewMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onMusicSelected(musics.get(position));
            }
        });

        fetcher.fetchMusicsForArtist(artist, new OnMusicFetcherResultListener() {
            @Override
            public void onMusicFetcherResult(ArrayList<Music> results, Exception e) {
                //suppression spinner
                spinner.dismiss();
                //mise à jours de la liste
                if(e==null){
                    musics= results;
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        refresh();
    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fetcher.stop();
    }

    public void onMusicSelected(Music m){
        if(onMusicSelectedListener!=null)
            onMusicSelectedListener.onMusicSelected(m);
    }

    public void refresh(){
        listViewMusics.invalidateViews();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Music musicSelected= this.musics.get(info.position);
        switch (item.getItemId()){
            case ACTION_SELECT:
                onMusicSelected(musicSelected);
                break;
            case ACTION_FAV_ON:
                musicSelected.setFavorite(true);
                onMusicEditedListener.onMusicEdited(musicSelected);
                db.add(musicSelected);
                refresh();
                break;
            case ACTION_FAV_OFF:
                musicSelected.setFavorite(false);
                onMusicEditedListener.onMusicEdited(musicSelected);
                db.remove(musicSelected);
                refresh();
                break;
        }
        return super.onContextItemSelected(item);
    }


}
