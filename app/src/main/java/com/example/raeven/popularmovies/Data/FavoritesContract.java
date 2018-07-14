package com.example.raeven.popularmovies.Data;

import android.provider.BaseColumns;

public class FavoritesContract {

    public static final class FavoriteEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
    }
}
