package com.example.raeven.popularmovies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raeven.popularmovies.Model.MovieModel;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private MovieModel myMovieData;
    private ImageView iv_backPoster;
    private ImageView iv_mainPoster;
    private TextView tv_title;
    private TextView tv_releaseDate;
    private TextView tv_movieDesc;
    private ObservableScrollView osv_movieDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        declareViews();
        myMovieData = (MovieModel) getIntent().getSerializableExtra("myMovieDetails");

        setValues(myMovieData);
    }

    private void declareViews(){
        iv_backPoster = (ImageView) findViewById(R.id.iv_backPoster);
        iv_mainPoster = (ImageView) findViewById(R.id.iv_mainPoster);
        tv_title = (TextView) findViewById(R.id.tv_movieTitle);
        tv_releaseDate = (TextView) findViewById(R.id.tv_releaseDate);
        tv_movieDesc = (TextView) findViewById(R.id.tv_movieDescription);
        osv_movieDetails = (ObservableScrollView) findViewById(R.id.osv_movieDetails);
        osv_movieDetails.setScrollViewCallbacks(this);
    }

    private void setValues(MovieModel myMovieData){
        Picasso.with(this).load(myMovieData.getBackPosterLink()).into(iv_backPoster);
        Picasso.with(this).load(myMovieData.getMainPosterLink()).into(iv_mainPoster);
        tv_title.setText(myMovieData.getTitle());
        tv_releaseDate.setText(myMovieData.getReleaseDate());
        tv_movieDesc.setText(myMovieData.getOverview());

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
                iv_backPoster.setVisibility(View.INVISIBLE);
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
                iv_backPoster.setVisibility(View.VISIBLE);

            }
        }
    }

}
