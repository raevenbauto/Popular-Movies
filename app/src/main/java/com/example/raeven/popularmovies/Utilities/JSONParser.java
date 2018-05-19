package com.example.raeven.popularmovies.Utilities;

import android.util.Log;

import com.example.raeven.popularmovies.Model.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {

    public static final String STATIC_IMAGE_LINK = "http://image.tmdb.org/t/p/w780/";
    public static final String STATIC_BACK_IMAGE_LINK = "http://image.tmdb.org/t/p/w780/";


    public static ArrayList<MovieModel> getJSONData(String urlResponse) throws JSONException {
        ArrayList<MovieModel> movieDataList = new ArrayList<MovieModel>();

        JSONObject JSONObject = new JSONObject(urlResponse);
        JSONArray movieResultsJSONArray = JSONObject.getJSONArray("results");

        for (int i = 0; i < movieResultsJSONArray.length(); i++){
            JSONObject movieJSONObject = movieResultsJSONArray.getJSONObject(i);

            int id = Integer.parseInt(movieJSONObject.getString("id"));
            double voteAverage = Double.parseDouble(movieJSONObject.getString("vote_average"));
            String title = movieJSONObject.getString("title");
            String mainPosterLink = (STATIC_IMAGE_LINK + movieJSONObject.getString("poster_path"));
            String originalTitle = movieJSONObject.getString("original_title");
            String backPosterLink = (STATIC_BACK_IMAGE_LINK + movieJSONObject.getString("backdrop_path"));
            String overview = movieJSONObject.getString("overview");
            String releaseDate = movieJSONObject.getString("release_date");

            MovieModel movieObject = new MovieModel(id, voteAverage, title, mainPosterLink, originalTitle, backPosterLink,
                                                       overview, releaseDate);

            movieDataList.add(movieObject);
        }

        return movieDataList;
    }
}
