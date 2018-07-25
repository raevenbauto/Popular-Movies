package com.example.raeven.popularmovies.Loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Model.TrailerModel;
import com.example.raeven.popularmovies.Utilities.JSONParser;
import com.example.raeven.popularmovies.Utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TrailerLoader extends AsyncTaskLoader<List<TrailerModel>> {

    private String urlPath;
    private List<TrailerModel> trailerModels;

    public TrailerLoader(Context context, String urlPath) {
        super(context);
        this.urlPath = urlPath;
    }

    @Override
    protected void onStartLoading() {
        if (trailerModels == null)
            forceLoad();

        else
            super.deliverResult(trailerModels);

    }

    @Override
    public List<TrailerModel> loadInBackground() {
        String movieStringURL = urlPath;
        System.out.println(urlPath);
        if (movieStringURL == null)
            return null;

        try {
            URL movieURL = new URL(movieStringURL);
            String urlResponse = NetworkUtils.getHttpResponse(movieURL);
            trailerModels = JSONParser.getTrailerJSON(urlResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerModels;
    }

    @Override
    public void deliverResult(List<TrailerModel> data) {
        trailerModels = data;
        super.deliverResult(data);
    }

}
