package com.example.raeven.popularmovies.Loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.raeven.popularmovies.Data.AppDatabase;
import com.example.raeven.popularmovies.Data.AppExecutors;
import com.example.raeven.popularmovies.Model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesLoader extends AsyncTaskLoader<List<MovieModel>> {

    private List<MovieModel> favoriteMovies;
    private int favoriteMoviesCount;
    private AppDatabase mDb;
    private Context context;

    public FavoritesLoader(Context context, AppDatabase mDb) {
        super(context);
        this.context = context;
        this.mDb = mDb;
    }

    @Override
    protected void onStartLoading() {
        List<MovieModel> dbData = new ArrayList<MovieModel>();
        AppExecutors.getInstance().diskIO().execute(new Runnable(){

            @Override
            public void run() {
                List<MovieModel> dbData = mDb.movieDao().loadAllFavorites();
            }
        });

        if (favoriteMovies == null || dbData.size() != favoriteMoviesCount){
            forceLoad();
        }

        else
            super.deliverResult(favoriteMovies);
    }

    @Override
    public List<MovieModel> loadInBackground() {
        List<MovieModel> holdFavoriteMovies = new ArrayList<MovieModel>();
        holdFavoriteMovies = mDb.movieDao().loadAllFavorites();
        favoriteMoviesCount = holdFavoriteMovies.size();

        return holdFavoriteMovies;
    }

    @Override
    public void deliverResult(List<MovieModel> data) {
        favoriteMovies = data;
        super.deliverResult(data);
    }
}
