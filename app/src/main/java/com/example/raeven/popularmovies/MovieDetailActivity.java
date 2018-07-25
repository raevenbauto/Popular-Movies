package com.example.raeven.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raeven.popularmovies.Adapters.ReviewsAdapter;
import com.example.raeven.popularmovies.Adapters.TrailerAdapter;
import com.example.raeven.popularmovies.Data.FavoritesContract;
import com.example.raeven.popularmovies.Data.FavoritesDBHelper;
import com.example.raeven.popularmovies.Data.FavoritesQuery;
import com.example.raeven.popularmovies.Loader.ReviewsLoader;
import com.example.raeven.popularmovies.Loader.TrailerLoader;
import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Model.ReviewModel;
import com.example.raeven.popularmovies.Model.TrailerModel;
import com.example.raeven.popularmovies.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/*
    Credits to Maxim Basinski for the Star image.


 */
public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerOnClickListener, LoaderManager.LoaderCallbacks, ReviewsAdapter.TrailerOnClickListener {

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

    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewsRecyclerView;

    private static TrailerAdapter mTrailerAdapter;
    private static ReviewsAdapter mReviewsAdapter;


    private static final int MOVIE_TRAILER_LOADER = 4;
    private static final int MOVIE_REVIEW_LOADER = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        declareViews();

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(false);

        GridLayoutManager reviewsLayoutManager = new GridLayoutManager(this, 1);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        mReviewsRecyclerView.setHasFixedSize(false);


        myMovieData = (MovieModel) getIntent().getSerializableExtra("myMovieDetails");
        setTitle(myMovieData.getTitle());
        setValues(myMovieData);

        getSupportLoaderManager().initLoader(MOVIE_TRAILER_LOADER, null,  this);
        getSupportLoaderManager().initLoader(MOVIE_REVIEW_LOADER, null,  this);

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
        tv_title = (TextView) findViewById(R.id.tv_trailerWord);
        tv_releaseDate = (TextView) findViewById(R.id.tv_releaseDate);
        tv_movieDesc = (TextView) findViewById(R.id.tv_movieDescription);
        tv_voteAverage = (TextView) findViewById(R.id.tv_voteAverage);
        bt_favorite = (Button) findViewById(R.id.bt_favorite);
        bt_unfavorite = (Button) findViewById(R.id.bt_unfavorite);
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);

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

    @Override
    public void trailerOnClick(TrailerModel trailersObject) {

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailersObject.getTrailerKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailersObject.getTrailerKey()));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

    @Override
    public void reviewOnClick(ReviewModel reviewsObject) {

    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        URL trailerURL = NetworkUtils.createTrailerURL(Integer.toString(myMovieData.getId()));
        URL reviewsURL = NetworkUtils.createReviewsURL(Integer.toString(myMovieData.getId()));
        if (id == MOVIE_TRAILER_LOADER)
            return new TrailerLoader(this, trailerURL.toString());

        else if (id == MOVIE_REVIEW_LOADER)
            return new ReviewsLoader(this, reviewsURL.toString());
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        //These two should be added on the Loader.
        if (loader.getId() == MOVIE_TRAILER_LOADER){
            mTrailerAdapter = new TrailerAdapter(this, (ArrayList<TrailerModel>) data);
            mTrailerRecyclerView.setAdapter(mTrailerAdapter);
            mTrailerAdapter.loadData();
        }

        else if (loader.getId() == MOVIE_REVIEW_LOADER){
            mReviewsAdapter = new ReviewsAdapter(this, (ArrayList<ReviewModel>) data);
            mReviewsRecyclerView.setAdapter(mReviewsAdapter);
            mReviewsAdapter.loadData();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }


}
