package com.example.raeven.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDBHelper extends SQLiteOpenHelper {

    private static FavoritesDBHelper mInstance = null;
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public static FavoritesDBHelper getInstance(Context ctx){
        if (mInstance == null){
            mInstance = new FavoritesDBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    private FavoritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoritesContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoritesContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL " +
                "); ";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
        System.out.println("Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
