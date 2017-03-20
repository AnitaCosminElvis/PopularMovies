package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cosmina on 3/20/2017.
 */

public class PopularMoviesDbHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE =  "CREATE TABLE " + PopularMoviesContract.MovieEntry.TABLE_NAME + " (" +
                        PopularMoviesContract.MovieEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PopularMoviesContract.MovieEntry.COLUMN_DATE       + " INTEGER NOT NULL, "                  +
                        PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL,"                     +
                        " UNIQUE (" + PopularMoviesContract.MovieEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    /**
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMoviesContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
