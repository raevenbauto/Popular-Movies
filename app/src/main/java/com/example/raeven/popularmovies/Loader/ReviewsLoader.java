package com.example.raeven.popularmovies.Loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Model.ReviewModel;
import com.example.raeven.popularmovies.Model.TrailerModel;
import com.example.raeven.popularmovies.Utilities.JSONParser;
import com.example.raeven.popularmovies.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ReviewsLoader extends AsyncTaskLoader<List<ReviewModel>> {

    private String urlPath;
    private List<ReviewModel> reviewsModels;

    public ReviewsLoader(Context context, String urlPath) {
        super(context);
        this.urlPath = urlPath;
    }

    @Override
    protected void onStartLoading() {
        if (reviewsModels == null)
            forceLoad();

        else
            super.deliverResult(reviewsModels);

    }

    @Override
    public List<ReviewModel> loadInBackground() {
        String movieStringURL = urlPath;
        System.out.println(urlPath);
        if (movieStringURL == null)
            return null;

        try {
            URL movieURL = new URL(movieStringURL);
            String urlResponse = NetworkUtils.getHttpResponse(movieURL);
            reviewsModels = JSONParser.getReviewJSON(urlResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewsModels;
    }

    @Override
    public void deliverResult(List<ReviewModel> data) {
        reviewsModels = data;
        super.deliverResult(data);
    }

}
