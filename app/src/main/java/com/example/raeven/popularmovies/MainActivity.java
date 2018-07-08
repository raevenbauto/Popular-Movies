package com.example.raeven.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;

import android.content.Loader;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Utilities.JSONParser;
import com.example.raeven.popularmovies.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener, LoaderManager.LoaderCallbacks<String> {

    private ImageView mMoviePoster;
    private RecyclerView mRecyclerView;
    private static MovieAdapter mMovieAdapter;

    private final String POPULAR_MOVIE_TITLE = "Popular Movies";
    private final String TOP_RATED_MOVIE_TITLE = "Top Rated Movies";

    private final String URL_EXTRA_KEY = "url";
    private final String TITLE_EXTRA_KEY = "title";
    private final String MENU_DISABLE_KEY = "menu_disable";


    private static String POPULAR_URL = "";
    private static String TOP_RATED_URL = "";
    private static int DISABLE_MENU_ITEM = 0;

    private static final int MOVIE_SEARCH_LOADER = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declareViews();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if (savedInstanceState != null){
            String title = savedInstanceState.getString(TITLE_EXTRA_KEY);
            setTitle(title);

            String url = savedInstanceState.getString(URL_EXTRA_KEY);

            try {
                new MovieJSONQuery().execute(new URL (url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            int menuID = savedInstanceState.getInt(MENU_DISABLE_KEY);
            DISABLE_MENU_ITEM = menuID;
        }

        else {
            POPULAR_URL = NetworkUtils.createURL(1).toString();
            setTitle(POPULAR_MOVIE_TITLE);

            try {
                new MovieJSONQuery().execute(new URL (POPULAR_URL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        if (DISABLE_MENU_ITEM == 0) {
            menu.findItem(R.id.item_popularMovie).setEnabled(false);
        }

        else{
            menu.findItem(DISABLE_MENU_ITEM).setEnabled(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.item_popularMovie:
                POPULAR_URL = NetworkUtils.createURL(1).toString();
                try {
                    new MovieJSONQuery().execute(new URL (POPULAR_URL));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                setTitle(POPULAR_MOVIE_TITLE);
                item.setEnabled(false);
                return true;

            case R.id.item_topRated:
                TOP_RATED_URL = NetworkUtils.createURL(2).toString();
                try {
                    new MovieJSONQuery().execute(new URL(TOP_RATED_URL));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                setTitle(TOP_RATED_MOVIE_TITLE);
                item.setEnabled(false);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getTitle() == POPULAR_MOVIE_TITLE){
            menu.findItem(R.id.item_topRated).setEnabled(true);
        }

        else if (getTitle() == TOP_RATED_MOVIE_TITLE){
            menu.findItem(R.id.item_popularMovie).setEnabled(true);
        }

        return true;
    }

    private void declareViews(){
        mMoviePoster = (ImageView)findViewById(R.id.iv_moviePoster);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_movieList);
    }

    @Override
    public void movieOnClick(MovieModel movieDetailsObject) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("myMovieDetails", movieDetailsObject);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                if (args == null)
                    return;

                //Set loading indicator to visible.
                
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public static class MovieJSONQuery extends AsyncTask<URL, Void, ArrayList<MovieModel>>{

        @Override
        protected ArrayList<MovieModel> doInBackground(URL... urls) {
            URL movieURL = urls[0];
            String urlResponse = null;
            ArrayList<MovieModel> movieData = new ArrayList<MovieModel>();

            try{
                urlResponse = NetworkUtils.getHttpResponse(movieURL);
                movieData = JSONParser.getJSONData(urlResponse);
            }

            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movieData;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> s) {
            if (s.size() != 0){
                //Show Views
                mMovieAdapter.loadData(s);
            }

            else{
                // Show error messages or hide Views.
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String currURL = "";
        if (getTitle().toString() == POPULAR_MOVIE_TITLE) {
            currURL = POPULAR_URL;
            DISABLE_MENU_ITEM = R.id.item_popularMovie;
        }

        else if (getTitle().toString() == TOP_RATED_MOVIE_TITLE) {
            currURL = TOP_RATED_URL;
            DISABLE_MENU_ITEM = R.id.item_topRated;
        }

        outState.putString(TITLE_EXTRA_KEY, getTitle().toString());
        outState.putString(URL_EXTRA_KEY, currURL);
        outState.putInt(MENU_DISABLE_KEY, DISABLE_MENU_ITEM);
    }
}
