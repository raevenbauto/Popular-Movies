package com.example.raeven.popularmovies.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.graphics.Movie;
import android.support.annotation.NonNull;

import com.example.raeven.popularmovies.Data.AppDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel{

    private LiveData<List<MovieModel>> favoriteMovies;
    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favoriteMovies = database.movieDao().loadAllFavorites();
    }

    public LiveData<List<MovieModel>> getFavorites(){
        return favoriteMovies;
    }
}
