package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE =  "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_NAME + " ("    +
                MoviesContract.MovieEntry._ID                       + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                MoviesContract.MovieEntry.COLUMN_DATE               + " INTEGER NOT NULL,"                      +
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID           + " VARCHAR2 NOT NULL,"                     +
                MoviesContract.MovieEntry.COLUMN_MOVIE_URI_IMAGE    + " VARCHAR2 NOT NULL,"                     +
                MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE        + " VARCHAR2 NOT NULL,"                     +
                MoviesContract.MovieEntry.COLUMN_MOVIE_PLOT         + " VARCHAR2 NOT NULL,"                     +
                MoviesContract.MovieEntry.COLUMN_MOVIE_RATING       + " REAL NOT NULL,"                         +
                MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " VARCHAR2 NOT NULL,"                     +
                MoviesContract.MovieEntry.COLUMN_MOVIE_TYPE         + " SMALLINT NOT NULL,"                     +
                " UNIQUE (" + MoviesContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
