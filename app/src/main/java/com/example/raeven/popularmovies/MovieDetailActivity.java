package com.example.raeven.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.raeven.popularmovies.Model.MovieModel;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        MovieModel myMovieData = (MovieModel) getIntent().getSerializableExtra("myMovieDetails");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
