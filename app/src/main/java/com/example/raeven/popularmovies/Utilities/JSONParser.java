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

        int id = 0;
        String voteAverage, title, mainPosterLink, originalTitle, backPosterLink, overview, releaseDate;
        voteAverage = title = mainPosterLink = originalTitle = backPosterLink = overview = releaseDate = "";
        for (int i = 0; i < movieResultsJSONArray.length(); i++){
            JSONObject movieJSONObject = movieResultsJSONArray.getJSONObject(i);

            if (movieJSONObject.has("id")){
                id = Integer.parseInt(movieJSONObject.getString("id"));
            }

            if (movieJSONObject.has("vote_average")){
                voteAverage = movieJSONObject.getString("vote_average") + "/10";
            }

            if (movieJSONObject.has("title")){
                title = movieJSONObject.getString("title");
            }

            if (movieJSONObject.has("poster_path")){
                mainPosterLink = (STATIC_IMAGE_LINK + movieJSONObject.getString("poster_path"));
            }

            if (movieJSONObject.has("original_title")){
                originalTitle = movieJSONObject.getString("original_title");
            }

            if (movieJSONObject.has("backdrop_path")){
                backPosterLink = (STATIC_BACK_IMAGE_LINK + movieJSONObject.getString("backdrop_path"));
            }

            if (movieJSONObject.has("overview")){
                overview = movieJSONObject.getString("overview");
            }

            if (movieJSONObject.has("release_date")){
                releaseDate = dateConvert(movieJSONObject.getString("release_date"));
            }

            MovieModel movieObject = new MovieModel(id, voteAverage, title, mainPosterLink, originalTitle, backPosterLink,
                                                       overview, releaseDate);

            movieDataList.add(movieObject);
        }

        return movieDataList;
    }

    public static List<TrailerModel> getTrailerJSON(String urlResponse) throws JSONException {
        List<TrailerModel> trailerDataList = new ArrayList<TrailerModel>();

        JSONObject JSONObject = new JSONObject(urlResponse);
        JSONArray trailerResultsJSONArray = JSONObject.getJSONArray("results");
        String key, name;
        key = name = "";

        for (int i = 0; i < trailerResultsJSONArray.length(); i++){
            JSONObject trailerMovieObject = trailerResultsJSONArray.getJSONObject(i);

            if (trailerMovieObject.has("key")){
                key = trailerMovieObject.getString("key");
            }

            if (trailerMovieObject.has("name")){
                name = trailerMovieObject.getString("name");
            }

            TrailerModel trailerData = new TrailerModel(key, name);
            trailerDataList.add(trailerData);
        }

        return trailerDataList;
    }

    public static List<ReviewModel> getReviewJSON(String urlResponse) throws JSONException {
        List<ReviewModel> reviewDataList = new ArrayList<ReviewModel>();

        JSONObject JSONObject = new JSONObject(urlResponse);
        JSONArray reviewResultsJSONArray = JSONObject.getJSONArray("results");
        String author, content;
        author = content = "";

        for (int i = 0; i < reviewResultsJSONArray.length(); i++){
            JSONObject reviewJSONObject = reviewResultsJSONArray.getJSONObject(i);

            if (reviewJSONObject.has("author")){
                author = reviewJSONObject.getString("author");
            }

            if (reviewJSONObject.has("content")){
                content = reviewJSONObject.getString("content");
            }

            ReviewModel reviewData = new ReviewModel(author, content);
            reviewDataList.add(reviewData);
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
