package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "data";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_URI_IMAGE = "uri_image";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_PLOT = "plot";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_TYPE = "movie_type";

        public static final int INDEX_ID            = 0;
        public static final int INDEX_DATE          = 1;
        public static final int INDEX_MOVIE_ID      = 2;
        public static final int INDEX_URI_IMAGE     = 3;
        public static final int INDEX_TITLE         = 4;
        public static final int INDEX_PLOT          = 5;
        public static final int INDEX_RATING        = 6;
        public static final int INDEX_RELEASE_DATE  = 7;
        public static final int INDEX_MOVIE_TYPE    = 8;

    }
}
