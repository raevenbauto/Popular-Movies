package com.example.raeven.popularmovies.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FavoritesQuery {

    public static final void insertMovie(SQLiteDatabase mDb, ContentValues values){
        try {
            mDb.beginTransaction();
            //clear the table first
            //go through the list and add one by one
            mDb.insert(FavoritesContract.FavoriteEntry.TABLE_NAME, null, values);
            mDb.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            mDb.endTransaction();
        }
    }

    public static final boolean findMovie(SQLiteDatabase mDb, int movieID){
        String[] columns = {FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID};
        String where = FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID + "=" + movieID;
        Cursor cursor = mDb.query(FavoritesContract.FavoriteEntry.TABLE_NAME,
                    columns,
                    where,
                    null,
                    null,
                    null,
                    null);

        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public static final void removeMovie(SQLiteDatabase mDb, int movieID){
        try {
            mDb.beginTransaction();
            mDb.delete(FavoritesContract.FavoriteEntry.TABLE_NAME, FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID + "=" +
                    movieID, null);
            mDb.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            mDb.endTransaction();
        }
    }

    public static final Cursor getAllFavorites(SQLiteDatabase mDb){

        return mDb.query(
                FavoritesContract.FavoriteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

    }
}
