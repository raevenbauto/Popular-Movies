package com.example.raeven.popularmovies.Loader;

import android.graphics.Movie;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.raeven.popularmovies.Data.FavoritesContract;
import com.example.raeven.popularmovies.Data.FavoritesQuery;
import com.example.raeven.popularmovies.MainActivity;
import com.example.raeven.popularmovies.Model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesLoader extends AsyncTaskLoader<List<MovieModel>> {

    private List<MovieModel> favoriteMovies;
    private int favoriteMoviesCount;
    private SQLiteDatabase mDb;
    private Context context;

    public FavoritesLoader(Context context, SQLiteDatabase mDb) {
        super(context);
        this.context = context;
        this.mDb = mDb;
    }

    @Override
    protected void onStartLoading() {
        if (favoriteMovies == null || FavoritesQuery.getAllFavorites(mDb).getCount() != favoriteMoviesCount){
            forceLoad();
        }

        else
            super.deliverResult(favoriteMovies);
    }

    @Override
    public List<MovieModel> loadInBackground() {
        Cursor favoriteCursor = FavoritesQuery.getAllFavorites(mDb);
        favoriteMoviesCount = favoriteCursor.getCount();
        List<MovieModel> holdFavoriteMovies = new ArrayList<MovieModel>();

        while (favoriteCursor.moveToNext()){

            int id = favoriteCursor.getInt(favoriteCursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID));
            String title = favoriteCursor.getString(favoriteCursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE));
            String mainPosterLink = favoriteCursor.getString(favoriteCursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_MAIN_POSTER_LINK));
            String originalTitle = favoriteCursor.getString(favoriteCursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE));
            String backPosterLink = favoriteCursor.getString(favoriteCursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_BACK_POSTER_LINK));
            String overview = favoriteCursor.getString(favoriteCursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW));
            String releaseDate = favoriteCursor.getString(favoriteCursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATE));
            String voteAverage = favoriteCursor.getString(favoriteCursor.getColumnIndex(FavoritesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE));

            MovieModel favoriteMovie = new MovieModel(id, voteAverage, title, mainPosterLink, originalTitle, backPosterLink, overview, releaseDate);
            holdFavoriteMovies.add(favoriteMovie);
        }



        return holdFavoriteMovies;
    }

    @Override
    public void deliverResult(List<MovieModel> data) {
        favoriteMovies = data;
        super.deliverResult(data);
    }
}
