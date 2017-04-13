package mydevmind.com.mydeezer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import mydevmind.com.mydeezer.Repository.DatabaseManager;
import mydevmind.com.mydeezer.model.DownloadJsonTask;
import mydevmind.com.mydeezer.model.IOnFavoriteChange;
import mydevmind.com.mydeezer.Repository.ManageFavorites;
import mydevmind.com.mydeezer.model.Music;
import mydevmind.com.mydeezer.model.MusicAdapter;


public class MusicListActivity extends Activity {

    private static final int ACTION_SELECT = 1;
    private static final int ACTION_FAV_ON = 2;
    private static final int ACTION_FAV_OFF = 3;

    private static final String DEEZER_SEARCH_URL = "http://api.deezer.com/search?q=";

    private ListView listViewMusics;
    private ArrayList<Music> musics;
    private TextView searchText;
    private ImageButton searchButton;

    //private RequestQueue mVolleyRequestQueue;
    private ImageLoader mVolleyImageLoader;
    private MusicAdapter adapter;

    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        db = new DatabaseManager(this, 1);
        // On initialise notre Thread-Pool et notre ImageLoader
        //mVolleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //mVolleyImageLoader = new ImageLoader(mVolleyRequestQueue, new BitmapLruCache());
        //mVolleyRequestQueue.start();

        //musics = Music.getAllMusics(25);
        musics = new ArrayList<Music>();
        //musics.add(Music.getDefaultMusic());

        listViewMusics = (ListView) findViewById(R.id.listMainSearch);
        searchText = (TextView) findViewById(R.id.editTextSearch);

        adapter= new MusicAdapter(this, musics);

        listViewMusics.setAdapter(adapter);
        listViewMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onMusicSelected(musics.get(position));
                goToMusic(position);
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

        searchButton = (ImageButton) findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMusics(searchText.getText().toString());
            }
        });

        /*ManageFavorites.setListener(new IOnFavoriteChange() {
            @Override
            public void onFavoriteChange(Music m, boolean isFavorite) {
                musics.get(musics.indexOf(m)).setFavorite(isFavorite);
                ((BaseAdapter) listViewMusics.getAdapter()).notifyDataSetChanged();
            }
        });*/
    }

    /*public RequestQueue getVolleyRequestQueue() {
        return mVolleyRequestQueue;
    }*/

    public ImageLoader getVolleyImageLoader() {
        return mVolleyImageLoader;
    }

    @Override
    protected void onStop() {
        //mVolleyRequestQueue.cancelAll(this);
        //mVolleyRequestQueue.stop();
        super.onStop();
    }

    public void onMusicSelected(Music m){
        Toast.makeText(this, m.getAlbum(), Toast.LENGTH_SHORT).show();
    }

    public void goToMusic(int position){
        Intent musicIntent= new Intent(getApplicationContext(), MusicActivity.class);
        musicIntent.putExtra("musicSelected", musics.get(position));
        startActivity(musicIntent);
    }


    public void searchMusics(String textSearch){
       // On va créer une Request pour Volley.
        // JsonArrayRequest hérite de Request et transforme automatiquement les données reçues en un JSONArray
        String url= DEEZER_SEARCH_URL+textSearch.replaceAll(" ", "%20");
        Log.d("query JSON to search : ", url);
        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    // Ce code est appelé quand la requête réussi. Étant ici dans le thread principal, on va pouvoir mettre à jour notre Adapter
                    adapter.updateMembers(jsonObject);
                }
            }, new Response.ErrorListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Le code suivant est appelé lorsque Volley n'a pas réussi à récupérer le résultat de la requête
                Log.d("Error while getting JSON: ", volleyError.getMessage());
            }
        });
        request.setTag(this);
        // On ajoute la Request au RequestQueue pour la lancer
        mVolleyRequestQueue.add(request);*/
        // Afficher le spinner (busy message)
        final ProgressDialog spinner = new ProgressDialog(this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        spinner.setTitle("Loading songs of " + textSearch);
        spinner.setMessage("Find songs in progress...");
        spinner.setCancelable(false);
        spinner.show();

        DownloadJsonTask jsonTask = new DownloadJsonTask(this, listViewMusics, musics, spinner);
        jsonTask.execute(url);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case ACTION_SELECT:
                goToMusic(info.position);
                break;
            case ACTION_FAV_ON:
                musics.get(info.position).setFavorite(true);
                //ManageFavorites.add(this, musics.get(info.position));
                db.add(musics.get(info.position));
                Toast.makeText(this, getString(R.string.main_ctx_favoris_toaston), Toast.LENGTH_SHORT).show();
                break;
            case ACTION_FAV_OFF:
                //ManageFavorites.remove(this, musics.get(info.position));
                db.remove(musics.get(info.position));
                musics.get(info.position).setFavorite(false);

                Toast.makeText(this, getString(R.string.main_ctx_favoris_toastoff), Toast.LENGTH_SHORT).show();
                break;
        }
        ((BaseAdapter) listViewMusics.getAdapter()).notifyDataSetChanged();
        return super.onContextItemSelected(item);
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
        if (id == R.id.action_favorites) {
            //musics = ManageFavorites.load(this);
            musics= db.getAll();
            adapter= new MusicAdapter(this, musics);
            listViewMusics.setAdapter(adapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
