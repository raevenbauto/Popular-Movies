package com.example.raeven.popularmovies.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
/*
    Note to Reviewer:
        I mostly reference most of the functions of this class to the previously created Sunshine.
        I hope this is okay. :)
 */
public class NetworkUtils {

    //http://api.themoviedb.org/3/movie/popular?api_key=
    public static final String STATIC_URL = "http://api.themoviedb.org";
    public static final String STATIC_NUM = "3";
    public static final String STATIC_MOVIE = "movie";
    public static final String STATIC_POPULAR = "popular";
    public static final String STATIC_TOP_RATED = "top_rated";
    public static final String PARAM_API_KEY_QUERY = "api_key";
    public static final String PARAM_API_KEY = "3c308b4b3b69940fdeecb1a8561ef61b";

    public static URL createURL(int sortType){
        URL url = null;
        String sortHolder = null;
        if (sortType == 1){
            sortHolder = STATIC_POPULAR;
        }

        else if (sortType == 2){
            sortHolder = STATIC_TOP_RATED;
        }

        Uri uri = Uri.parse(STATIC_URL).buildUpon()
                .appendPath(STATIC_NUM)
                .appendEncodedPath(STATIC_MOVIE)
                .appendEncodedPath(sortHolder)
                .appendQueryParameter(PARAM_API_KEY_QUERY, PARAM_API_KEY)
                .build();

        try{
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getHttpResponse(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try{
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasNext = scanner.hasNext();
            if (hasNext){
                return scanner.next();
            }

            else
                return null;
        }

        finally {
            httpURLConnection.disconnect();
        }
    }

}
