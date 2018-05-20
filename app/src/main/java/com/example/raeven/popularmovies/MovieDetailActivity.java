package com.example.raeven.popularmovies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raeven.popularmovies.Model.MovieModel;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity  {

    private MovieModel myMovieData;
    private ImageView iv_backPoster;
    private ImageView iv_mainPoster;
    private TextView tv_title;
    private TextView tv_releaseDate;
    private TextView tv_movieDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        declareViews();
        myMovieData = (MovieModel) getIntent().getSerializableExtra("myMovieDetails");
        setTitle(myMovieData.getTitle());
        setValues(myMovieData);
    }

    private void declareViews(){
        iv_backPoster = (ImageView) findViewById(R.id.iv_backPoster);
        iv_mainPoster = (ImageView) findViewById(R.id.iv_mainPoster);
        tv_title = (TextView) findViewById(R.id.tv_movieTitle);
        tv_releaseDate = (TextView) findViewById(R.id.tv_releaseDate);
        tv_movieDesc = (TextView) findViewById(R.id.tv_movieDescription);
    }

    private void setValues(MovieModel myMovieData){
        Picasso.with(this).load(myMovieData.getBackPosterLink()).into(iv_backPoster);
        Picasso.with(this).load(myMovieData.getMainPosterLink()).into(iv_mainPoster);
        tv_title.setText(myMovieData.getTitle());
        tv_releaseDate.setText(myMovieData.getReleaseDate());
        tv_movieDesc.setText(myMovieData.getOverview());

    }
}
