package com.example.raeven.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.raeven.popularmovies.Data.FavoritesContract;
import com.example.raeven.popularmovies.Data.FavoritesDBHelper;
import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Utilities.JSONParser;
import com.example.raeven.popularmovies.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*

Credits:
    Icons : https://www.flaticon.com/authors/smashicons
 */

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<MovieModel>>{

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
    private Bundle urlBundle = new Bundle();
    private SQLiteDatabase mDb;


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

        FavoritesDBHelper favHelper = FavoritesDBHelper.getInstance(this);
        mDb = favHelper.getWritableDatabase();
        /*
        insertFakeData(mDb);
        Cursor cursor = getAllGuests();
        System.out.println("NUM OF COLUMNS: " + cursor.getCount());
        */

        if (savedInstanceState != null){
            String title = savedInstanceState.getString(TITLE_EXTRA_KEY);
            setTitle(title);

            int menuID = savedInstanceState.getInt(MENU_DISABLE_KEY);
            DISABLE_MENU_ITEM = menuID;

            getSupportLoaderManager().restartLoader(MOVIE_SEARCH_LOADER, savedInstanceState, this);
        }

        else {
            POPULAR_URL = NetworkUtils.createURL(1).toString();
            setTitle(POPULAR_MOVIE_TITLE);

            urlBundle.putString(URL_EXTRA_KEY, POPULAR_URL);
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(MOVIE_SEARCH_LOADER, urlBundle,  this);
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
                urlBundle.putString(URL_EXTRA_KEY, POPULAR_URL);
                getSupportLoaderManager().restartLoader(MOVIE_SEARCH_LOADER, urlBundle, this);
                setTitle(POPULAR_MOVIE_TITLE);
                item.setEnabled(false);
                return true;

            case R.id.item_topRated:
                TOP_RATED_URL = NetworkUtils.createURL(2).toString();
                urlBundle.putString(URL_EXTRA_KEY, TOP_RATED_URL);
                getSupportLoaderManager().restartLoader(MOVIE_SEARCH_LOADER, urlBundle, this);
                setTitle(TOP_RATED_MOVIE_TITLE);
                item.setEnabled(false);
                return true;

            case R.id.item_favorites:
                //Add favorite codes here
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
    public Loader<ArrayList<MovieModel>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieModel>>(this) {

            @Override
            protected void onStartLoading() {
                if (args == null)
                    return;

                //Set loading indicator to visible.

                forceLoad();
            }

            @Override
            public ArrayList<MovieModel> loadInBackground() {
                String movieStringURL = args.getString(URL_EXTRA_KEY);

                if (movieStringURL == null)
                    return null;

                try {
                    URL movieURL = new URL(movieStringURL);
                    String urlResponse = NetworkUtils.getHttpResponse(movieURL);
                    ArrayList<MovieModel> movieModels = new ArrayList<MovieModel>();
                    movieModels = JSONParser.getJSONData(urlResponse);
                    return movieModels;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> data) {
        //Set loading indicator to invisible.

        if (data.size() <= 0) {
            //Show error Message
        }

        else{
            mMovieAdapter.loadData(data, mDb);

        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {

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

/*
    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID, "91111111");
        cv.put(FavoritesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, "The Grudge");
        list.add(cv);

        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (FavoritesContract.FavoriteEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(FavoritesContract.FavoriteEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }

    private Cursor getAllGuests() {
        // COMPLETED (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
        return mDb.query(
                FavoritesContract.FavoriteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
    */
}
