package com.example.raeven.popularmovies.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.graphics.Movie;

import com.example.raeven.popularmovies.Model.MovieModel;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY id DESC")
    LiveData<List<MovieModel>> loadAllFavorites();

    @Insert
    void insertMovie(MovieModel movieModel);

    @Query("DELETE FROM movie WHERE movieID = :movieID")
    void deleteMovie(int movieID);

    @Query("SELECT * FROM movie WHERE movieID = :movieID")
    LiveData<MovieModel> checkMovie(int movieID);
}
