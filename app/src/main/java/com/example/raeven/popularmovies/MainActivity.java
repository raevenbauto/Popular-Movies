package com.example.raeven.popularmovies;

import android.content.Context;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.raeven.popularmovies.Data.FavoritesDBHelper;
import com.example.raeven.popularmovies.Loader.FavoritesLoader;
import com.example.raeven.popularmovies.Loader.MoviesLoader;
import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Utilities.NetworkUtils;

import java.util.ArrayList;

/*

Credits:
    Icons : https://www.flaticon.com/authors/smashicons
 */

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener,
        LoaderManager.LoaderCallbacks{

    private ImageView mMoviePoster;
    private RecyclerView mRecyclerView;
    private static MovieAdapter mMovieAdapter;

    private final String POPULAR_MOVIE_TITLE = "Popular Movies";
    private final String TOP_RATED_MOVIE_TITLE = "Top Rated Movies";
    private final String FAVORITE_MOVIE_TITLE = "Favorite Movies";

    private final String MENU_DISABLE_KEY = "no_menu_disable";
    private final String CURR_LOADER_KEY = "currLoaderKey";
    private final String FAVORITE_MOVIES_KEY = "favorite_mov";


    private static int CURR_LOADER = 0;
    private static int DISABLE_MENU_ITEM = 0;

    private static final int POPULAR_MOVIE_LOADER = 1;
    private static final int TOP_RATED_MOVIE_LOADER = 2;
    private static final int MOVIE_FAVORITE_LOADER = 3;

    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declareViews();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        FavoritesDBHelper favHelper = FavoritesDBHelper.getInstance(this);
        mDb = favHelper.getWritableDatabase();
        System.out.println("OnCreate");

        if (savedInstanceState == null){
            CURR_LOADER = 1;
            setTitle(POPULAR_MOVIE_TITLE);
            getSupportLoaderManager().initLoader(POPULAR_MOVIE_LOADER, null,  this);
        }

    }

    private void declareViews(){
        mMoviePoster = (ImageView)findViewById(R.id.iv_moviePoster);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_movieList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            DISABLE_MENU_ITEM = savedInstanceState.getInt(MENU_DISABLE_KEY);
            int currentLoaderKey = savedInstanceState.getInt(CURR_LOADER_KEY);
            getSupportLoaderManager().initLoader(currentLoaderKey, savedInstanceState, this);

            if (currentLoaderKey == POPULAR_MOVIE_LOADER)
                setTitle(POPULAR_MOVIE_TITLE);
            else if (currentLoaderKey == TOP_RATED_MOVIE_LOADER)
                setTitle(TOP_RATED_MOVIE_TITLE);
            else
                setTitle(FAVORITE_MOVIE_TITLE);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        System.out.println("Loader ID: " +  id);
        if (id == POPULAR_MOVIE_LOADER || id == TOP_RATED_MOVIE_LOADER) {
            String url = "";
            if (id == POPULAR_MOVIE_LOADER)
                url = NetworkUtils.createURL(1).toString();

            else
                url = NetworkUtils.createURL(2).toString();

            return new MoviesLoader(this, url);
        }
        else if (id == MOVIE_FAVORITE_LOADER) {
            return new FavoritesLoader(this, mDb);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (loader.getId() == POPULAR_MOVIE_LOADER || loader.getId() == TOP_RATED_MOVIE_LOADER || loader.getId() == MOVIE_FAVORITE_LOADER) {
            System.out.println(loader.getId());
            System.out.println("onLoadFinished()");
            mMovieAdapter = new MovieAdapter(this, (ArrayList<MovieModel>) data);
            mRecyclerView.setAdapter(mMovieAdapter);
            mMovieAdapter.loadData((ArrayList<MovieModel>) data, mDb);
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {
        mRecyclerView.setAdapter(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (getTitle().toString() == POPULAR_MOVIE_TITLE) {
            DISABLE_MENU_ITEM = R.id.item_popularMovie;
        }

        else if (getTitle().toString() == TOP_RATED_MOVIE_TITLE) {
            DISABLE_MENU_ITEM = R.id.item_topRated;
        }

        else if (getTitle().toString() == FAVORITE_MOVIE_TITLE) {
            DISABLE_MENU_ITEM = R.id.item_favorites;
        }

        outState.putInt(MENU_DISABLE_KEY, DISABLE_MENU_ITEM);
        outState.putInt(CURR_LOADER_KEY, CURR_LOADER);
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
                CURR_LOADER = POPULAR_MOVIE_LOADER;
                setTitle(POPULAR_MOVIE_TITLE);
                item.setEnabled(false);
                break;


            case R.id.item_topRated:
                CURR_LOADER = TOP_RATED_MOVIE_LOADER;
                setTitle(TOP_RATED_MOVIE_TITLE);
                item.setEnabled(false);
                break;

            case R.id.item_favorites:
                CURR_LOADER = MOVIE_FAVORITE_LOADER;
                setTitle(FAVORITE_MOVIE_TITLE);
                item.setEnabled(false);
                break;
        }
        getSupportLoaderManager().destroyLoader(MOVIE_FAVORITE_LOADER);
        getSupportLoaderManager().initLoader(CURR_LOADER, null, this);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getTitle() == POPULAR_MOVIE_TITLE){
            menu.findItem(R.id.item_topRated).setEnabled(true);
            menu.findItem(R.id.item_favorites).setEnabled(true);
        }

        else if (getTitle() == TOP_RATED_MOVIE_TITLE){
            menu.findItem(R.id.item_popularMovie).setEnabled(true);
            menu.findItem(R.id.item_favorites).setEnabled(true);
        }

        else if (getTitle() == FAVORITE_MOVIE_TITLE){
            menu.findItem(R.id.item_popularMovie).setEnabled(true);
            menu.findItem(R.id.item_topRated).setEnabled(true);
        }

        return true;
    }



    @Override
    public void movieOnClick(MovieModel movieDetailsObject) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("myMovieDetails", movieDetailsObject);
        startActivity(intentToStartDetailActivity);
    }

}
