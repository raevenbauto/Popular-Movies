package com.example.raeven.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.raeven.popularmovies.Adapters.MovieAdapter;
import com.example.raeven.popularmovies.Data.AppDatabase;
import com.example.raeven.popularmovies.Loader.MoviesLoader;
import com.example.raeven.popularmovies.Model.MainViewModel;
import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/*

Credits:
    Icons : https://www.flaticon.com/authors/smashicons
 */

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener,
        LoaderManager.LoaderCallbacks{

    private ImageView mMoviePoster;
    private RecyclerView mRecyclerView;
    private static MovieAdapter mMovieAdapter;
    private ProgressBar pb_movieLoading;

    private final String POPULAR_MOVIE_TITLE = "Popular Movies";
    private final String TOP_RATED_MOVIE_TITLE = "Top Rated Movies";
    private final String FAVORITE_MOVIE_TITLE = "Favorite Movies";

    private final String MENU_DISABLE_KEY = "no_menu_disable";
    private final String CURR_LOADER_KEY = "currLoaderKey";

    private static int CURR_LOADER = 0;
    private static int DISABLE_MENU_ITEM = 0;

    private static final int POPULAR_MOVIE_LOADER = 1;
    private static final int TOP_RATED_MOVIE_LOADER = 2;
    private static final int MOVIE_FAVORITE_LOADER = 3;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declareViews();

        int posterWidth = 500;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateBestSpanCount(posterWidth));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mDb = AppDatabase.getInstance(getApplicationContext());

        CURR_LOADER = 1;
        setTitle(POPULAR_MOVIE_TITLE);
        if (checkConnection() && savedInstanceState == null) {
            getSupportLoaderManager().initLoader(POPULAR_MOVIE_LOADER, null, this);
            System.out.println("HELLO");
        }
    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    private boolean checkConnection(){
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Test for connection
        if (connectionManager.getActiveNetworkInfo() != null
                && connectionManager.getActiveNetworkInfo().isAvailable()
                && connectionManager.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        else{
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.cl_mainLayout), "No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            overridePendingTransition( 0, 0);
                            startActivity(getIntent());
                            overridePendingTransition( 0, 0);
                        }
                    });

            snackbar.show();
            mRecyclerView.setAdapter(null);
            return false;
        }
    }

    private void declareViews(){
        mMoviePoster = (ImageView)findViewById(R.id.iv_moviePoster);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_movieList);
        pb_movieLoading =  (ProgressBar)findViewById(R.id.pb_movieLoading);
    }

    private void setUpViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(@Nullable List<MovieModel> movieModels) {
                if (getTitle() == FAVORITE_MOVIE_TITLE) {
                    mMovieAdapter.loadData((ArrayList<MovieModel>) movieModels);
                }
            }
        });
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            int currentLoaderKey = savedInstanceState.getInt(CURR_LOADER_KEY);
            DISABLE_MENU_ITEM = savedInstanceState.getInt(MENU_DISABLE_KEY);

            if (currentLoaderKey == POPULAR_MOVIE_LOADER)
                setTitle(POPULAR_MOVIE_TITLE);
            else if (currentLoaderKey == TOP_RATED_MOVIE_LOADER)
                setTitle(TOP_RATED_MOVIE_TITLE);
            else
                setTitle(FAVORITE_MOVIE_TITLE);

            if (currentLoaderKey != MOVIE_FAVORITE_LOADER) {
                getSupportLoaderManager().initLoader(currentLoaderKey, savedInstanceState, this);
            }

            else {
                setUpViewModel();
            }
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        pb_movieLoading.setVisibility(View.VISIBLE);
        System.out.println("Loader ID: " +  id);
        if (checkConnection()){
            if (id == POPULAR_MOVIE_LOADER || id == TOP_RATED_MOVIE_LOADER) {
                String url = "";
                if (id == POPULAR_MOVIE_LOADER)
                    url = NetworkUtils.createURL(POPULAR_MOVIE_LOADER).toString();

                else
                    url = NetworkUtils.createURL(TOP_RATED_MOVIE_LOADER).toString();

                return new MoviesLoader(this, url);
            }
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        if (loader.getId() == POPULAR_MOVIE_LOADER || loader.getId() == TOP_RATED_MOVIE_LOADER) {
            System.out.println(loader.getId());
            System.out.println("onLoadFinished()");
            mMovieAdapter.loadData((ArrayList<MovieModel>)data);
        }

        getSupportLoaderManager().destroyLoader(loader.getId());
        pb_movieLoading.setVisibility(View.GONE);
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
                setUpViewModel();
                break;
        }


        if (CURR_LOADER != MOVIE_FAVORITE_LOADER){
            getSupportLoaderManager().initLoader(CURR_LOADER, null, this);
        }

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

        if (!checkConnection()){
            menu.findItem(R.id.item_popularMovie).setEnabled(false);
            menu.findItem(R.id.item_topRated).setEnabled(false);
            menu.findItem(R.id.item_favorites).setEnabled(false);
        }

        return true;
    }

}
