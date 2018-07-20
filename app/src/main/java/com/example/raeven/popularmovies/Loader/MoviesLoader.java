package com.example.raeven.popularmovies.Loader;

import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Utilities.JSONParser;
import com.example.raeven.popularmovies.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MoviesLoader extends AsyncTaskLoader<List<MovieModel>> {

    private String urlPath;
    private List<MovieModel> movieModels;

    public MoviesLoader(Context context, String urlPath) {
        super(context);
        this.urlPath = urlPath;
    }

    @Override
    protected void onStartLoading() {
        if (movieModels == null)
            forceLoad();

        else
            super.deliverResult(movieModels);

    }

    @Override
    public List<MovieModel> loadInBackground() {
        String movieStringURL = urlPath;

        if (movieStringURL == null)
            return null;

        try {
            URL movieURL = new URL(movieStringURL);
            String urlResponse = NetworkUtils.getHttpResponse(movieURL);
            movieModels = JSONParser.getJSONData(urlResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieModels;
    }

    @Override
    public void deliverResult(List<MovieModel> data) {
        movieModels = data;
        super.deliverResult(data);
    }

}
