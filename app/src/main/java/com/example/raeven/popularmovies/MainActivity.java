package com.example.raeven.popularmovies;

import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Utilities.JSONParser;
import com.example.raeven.popularmovies.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener {

    private ImageView mMoviePoster;
    private RecyclerView mRecyclerView;
    private static MovieAdapter mMovieAdapter;

    private final String POPULAR_MOVIE_TITLE = "Popular Movies";
    private final String TOP_RATED_MOVIE_TITLE = "Top Rated Movies";


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
        new MovieJSONQuery().execute(NetworkUtils.createURL(1));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        menu.findItem(R.id.item_popularMovie).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.item_popularMovie:
                new MovieJSONQuery().execute(NetworkUtils.createURL(1));
                setTitle(POPULAR_MOVIE_TITLE);
                item.setEnabled(false);
                return true;

            case R.id.item_topRated:
                new MovieJSONQuery().execute(NetworkUtils.createURL(2));
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
                //e.printStackTrace();
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
}
