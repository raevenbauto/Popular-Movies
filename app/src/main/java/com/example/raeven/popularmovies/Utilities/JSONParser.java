package com.example.raeven.popularmovies.Utilities;

import android.util.Log;

import com.example.raeven.popularmovies.Model.MovieModel;
import com.example.raeven.popularmovies.Model.ReviewModel;
import com.example.raeven.popularmovies.Model.TrailerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    private static final String STATIC_IMAGE_LINK = "http://image.tmdb.org/t/p/w780/";
    private static final String STATIC_BACK_IMAGE_LINK = "http://image.tmdb.org/t/p/w780/";


    public static ArrayList<MovieModel> getJSONData(String urlResponse) throws JSONException {
        ArrayList<MovieModel> movieDataList = new ArrayList<MovieModel>();

        JSONObject JSONObject = new JSONObject(urlResponse);
        JSONArray movieResultsJSONArray = JSONObject.getJSONArray("results");

        for (int i = 0; i < movieResultsJSONArray.length(); i++){
            JSONObject movieJSONObject = movieResultsJSONArray.getJSONObject(i);

            int id = Integer.parseInt(movieJSONObject.getString("id"));
            String voteAverage = movieJSONObject.getString("vote_average") + "/10";
            String title = movieJSONObject.getString("title");
            String mainPosterLink = (STATIC_IMAGE_LINK + movieJSONObject.getString("poster_path"));
            String originalTitle = movieJSONObject.getString("original_title");
            String backPosterLink = (STATIC_BACK_IMAGE_LINK + movieJSONObject.getString("backdrop_path"));
            String overview = movieJSONObject.getString("overview");
            String releaseDate = dateConvert(movieJSONObject.getString("release_date"));

            MovieModel movieObject = new MovieModel(id, voteAverage, title, mainPosterLink, originalTitle, backPosterLink,
                                                       overview, releaseDate);

            movieDataList.add(movieObject);
        }

        return movieDataList;
    }

    public static List<TrailerModel> getTrailerJSON(String urlResponse) throws JSONException {
        List<TrailerModel> trailerDataList = new ArrayList<TrailerModel>();

        JSONObject JSONObject = new JSONObject(urlResponse);
        JSONArray movieResultsJSONArray = JSONObject.getJSONArray("results");

        for (int i = 0; i < movieResultsJSONArray.length(); i++){
            JSONObject movieJSONObject = movieResultsJSONArray.getJSONObject(i);

            String key = movieJSONObject.getString("key");
            String name = movieJSONObject.getString("name");

            TrailerModel trailerData = new TrailerModel(key, name);

            trailerDataList.add(trailerData);
        }

        return trailerDataList;
    }

    public static List<ReviewModel> getReviewJSON(String urlResponse) throws JSONException {
        List<ReviewModel> reviewDataList = new ArrayList<ReviewModel>();

        JSONObject JSONObject = new JSONObject(urlResponse);
        JSONArray movieResultsJSONArray = JSONObject.getJSONArray("results");

        for (int i = 0; i < movieResultsJSONArray.length(); i++){
            JSONObject movieJSONObject = movieResultsJSONArray.getJSONObject(i);

            String author = movieJSONObject.getString("author");
            String content = movieJSONObject.getString("content");

            ReviewModel trailerData = new ReviewModel(author, content);

            reviewDataList.add(trailerData);
        }

        return reviewDataList;
    }

    private static String dateConvert(String releaseDate){
        String convertedDate = "";
        String[] splitDate = releaseDate.split("-");;

        switch(Integer.parseInt(splitDate[1])){
            case 1:
                convertedDate = "Jan";
                break;

            case 2:
                convertedDate = "Feb";
                break;

            case 3:
                convertedDate = "Mar";
                break;

            case 4:
                convertedDate = "Apr";
                break;

            case 5:
                convertedDate = "May";
                break;

            case 6:
                convertedDate = "June";
                break;

            case 7:
                convertedDate = "July";
                break;

            case 8:
                convertedDate = "Aug";
                break;

            case 9:
                convertedDate = "Sept";
                break;

            case 10:
                convertedDate = "Oct";
                break;

            case 11:
                convertedDate = "Nov";
                break;

            case 12:
                convertedDate = "Dec";
                break;
        }

        convertedDate = convertedDate.concat(" " + splitDate[2] + ", "+ splitDate[0]);
        return convertedDate;
    }
}
