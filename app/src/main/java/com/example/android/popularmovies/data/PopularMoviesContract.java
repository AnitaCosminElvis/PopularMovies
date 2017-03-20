package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class PopularMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movies";

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
        public static final String COLUMN_MOVIE_URI_TRAILER = "uri_video";



        long    mMovieId;
        String  mUriImageString;
        String  mTitle;
        String  mPlot;
        double  mUserRating;
        String  mReleaseDate;



        /**
         *
         * @return Uri to query details about a single weather entry
         */
        public static Uri buildMovieUri() {
            return CONTENT_URI.buildUpon().build();
        }

        /**
         * Returns just the selection part of the weather query from a normalized today value.
         * This is used to get a weather forecast from today's date. To make this easy to use
         * in compound selection, we embed today's date as an argument in the query.
         *
         * @return The selection part of the weather query for today onwards
         */
        public static String getSqlSelect() {
            return PopularMoviesContract.MovieEntry.TABLE_NAME;
        }
    }
}
