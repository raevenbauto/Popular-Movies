package com.example.raeven.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raeven.popularmovies.Data.FavoritesContract;
import com.example.raeven.popularmovies.Data.FavoritesDBHelper;
import com.example.raeven.popularmovies.Data.FavoritesQuery;
import com.example.raeven.popularmovies.Model.MovieModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/*
    Credits to Maxim Basinski for the Star image. 


 */
public class MovieDetailActivity extends AppCompatActivity  {

    private MovieModel myMovieData;
    private ImageView iv_backPoster;
    private ImageView iv_mainPoster;
    private TextView tv_title;
    private TextView tv_releaseDate;
    private TextView tv_movieDesc;
    private TextView tv_voteAverage;
    private Button bt_favorite;
    private Button bt_unfavorite;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        declareViews();
        myMovieData = (MovieModel) getIntent().getSerializableExtra("myMovieDetails");
        setTitle(myMovieData.getTitle());
        setValues(myMovieData);

        FavoritesDBHelper favHelper = FavoritesDBHelper.getInstance(this);
        mDb = favHelper.getWritableDatabase();

        final int movieID = myMovieData.getId();
        if (FavoritesQuery.findMovie(mDb, movieID)){
            hideFavoriteButton();
        }

        bt_favorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addToFavorites(mDb);
                hideFavoriteButton();
            }
        });

        bt_unfavorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFromFavorites(mDb, movieID);
                hideUnfavoriteButton();
            }
        });
    }

    private void declareViews(){
        iv_backPoster = (ImageView) findViewById(R.id.iv_backPoster);
        iv_mainPoster = (ImageView) findViewById(R.id.iv_mainPoster);
        tv_title = (TextView) findViewById(R.id.tv_movieTitle);
        tv_releaseDate = (TextView) findViewById(R.id.tv_releaseDate);
        tv_movieDesc = (TextView) findViewById(R.id.tv_movieDescription);
        tv_voteAverage = (TextView) findViewById(R.id.tv_voteAverage);
        bt_favorite = (Button) findViewById(R.id.bt_favorite);
        bt_unfavorite = (Button) findViewById(R.id.bt_unfavorite);

    }

    private void setValues(MovieModel myMovieData){
        Picasso.with(this).load(myMovieData.getBackPosterLink()).into(iv_backPoster);
        Picasso.with(this).load(myMovieData.getMainPosterLink()).into(iv_mainPoster);
        tv_title.setText(myMovieData.getTitle());
        tv_releaseDate.setText(myMovieData.getReleaseDate());
        tv_movieDesc.setText(myMovieData.getOverview());
        tv_voteAverage.setText(myMovieData.getVoteAverage());
    }

    private void hideFavoriteButton(){
        bt_favorite.setVisibility(View.GONE);
        bt_unfavorite.setVisibility(View.VISIBLE);
    }

    private void hideUnfavoriteButton(){
        bt_unfavorite.setVisibility(View.GONE);
        bt_favorite.setVisibility(View.VISIBLE);
    }

    private void addToFavorites(SQLiteDatabase mDb) {

        // Create a new map of values, where column names are the keys

        ContentValues values = new ContentValues();
        values.put(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID, myMovieData.getId());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, myMovieData.getOriginalTitle());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_MAIN_POSTER_LINK, myMovieData.getMainPosterLink());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_BACK_POSTER_LINK, myMovieData.getBackPosterLink());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW, myMovieData.getOverview());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATE, myMovieData.getReleaseDate());
        values.put(FavoritesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, myMovieData.getVoteAverage());

        FavoritesQuery.insertMovie(mDb, values);
    }

    private void removeFromFavorites(SQLiteDatabase mDb, int movieID){
        FavoritesQuery.removeMovie(mDb, movieID);
    }

}
