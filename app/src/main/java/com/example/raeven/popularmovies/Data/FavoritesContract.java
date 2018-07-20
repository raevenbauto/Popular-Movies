package com.example.raeven.popularmovies.Data;

import android.provider.BaseColumns;

public class FavoritesContract {

    public static final class FavoriteEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_MAIN_POSTER_LINK = "mainPosterLink";
        public static final String COLUMN_BACK_POSTER_LINK = "backPosterLink";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
    }
}
