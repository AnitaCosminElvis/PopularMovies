package com.example.android.popularmovies.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by home on 2/6/2017.
 */


import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.PrefferedDataLoadedListener;

import org.json.JSONArray;


public final class HttpRequestManager extends AsyncTask<Integer, Void, JSONArray> {

    private static final String     TAG = HttpRequestManager.class.getSimpleName();

    private static final String     BASE_MOVIES_URL = "https://api.themoviedb.org/3/movie/";
    private static final String     BASE_POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String     BASE_TOP_MOVIES_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static final String     VIDEOS_PATH = "/videos?api_key=";
    private static final String     REVIEWS_PATH = "/reviews?api_key=";


    private static final String     API_KEY = "dummyApiKey";

    public static final int         REQUEST_MOVIES      = 0;
    public static final int         REQUEST_TRAILERS    = 1;
    public static final int         REQUEST_REVIEWS     = 2;

    MainActivity.ESortPreference    mSortPreference;
    PrefferedDataLoadedListener     mListener;
    long                            mMovieId;
    Integer                         mRequestType;

    public HttpRequestManager(PrefferedDataLoadedListener listener,
                              MainActivity.ESortPreference eSortPreference,
                              long nMovieId) {
        mSortPreference = eSortPreference;
        mListener = listener;
        mMovieId = nMovieId;
    }


    public void setPreferenceType(MainActivity.ESortPreference eSortPreference){
        mSortPreference = eSortPreference;
    }

    public URL createUrl(int nSelect) {
        Log.v(TAG, "Creating URI ...");

        String urlString = null;

        switch ( nSelect ) {
            case REQUEST_MOVIES: {
                switch (mSortPreference) {
                    case E_MOST_POPULAR_MOVIE:
                        urlString = BASE_POPULAR_MOVIES_URL + API_KEY;
                        break;
                    case E_TOP_RATED_MOVIE:
                        urlString = BASE_TOP_MOVIES_URL + API_KEY;
                        break;
                    case E_UNKNOWN_SORT_TYPE:
                    default:
                        break;
                }
                break;
            }
            case REQUEST_TRAILERS: {
                urlString = BASE_MOVIES_URL + String.valueOf(mMovieId) + VIDEOS_PATH + API_KEY;
                break;
            }
            case REQUEST_REVIEWS: {
                urlString = BASE_MOVIES_URL + String.valueOf(mMovieId) + REVIEWS_PATH + API_KEY;
                break;
            }
        }

        Uri builtUri = Uri.parse(urlString);

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Created URI " + url);

        return url;
    }

    public static String requestDataFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return null;
    }

    @Override
    protected JSONArray doInBackground(Integer... params) {
        JSONArray data = null;
        URL url = null;

        mRequestType = params[0];

        switch (mRequestType) {
            case REQUEST_MOVIES: {
                try {
                    url = createUrl(REQUEST_MOVIES);
                    String jsonResponse = requestDataFromHttpUrl(url);

                    data = TheMovieDBDataResponseManager.getTheMovieDbStringListFromJson(jsonResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case REQUEST_TRAILERS: {
                try {
                    url = createUrl(REQUEST_TRAILERS);
                    String jsonResponse = requestDataFromHttpUrl(url);

                    data = TheMovieDBDataResponseManager.getTheTrailersDbStringListFromJson(jsonResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case REQUEST_REVIEWS: {
                try {
                    url = createUrl(REQUEST_REVIEWS);
                    String jsonResponse = requestDataFromHttpUrl(url);

                    data = TheMovieDBDataResponseManager.getTheReviewsDbStringListFromJson(jsonResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        return data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(JSONArray data) {
        switch (mRequestType) {
            case REQUEST_MOVIES: {
                mListener.onPrefferedDataWasFetched(data);
                break;
            }
            case REQUEST_TRAILERS: {
                mListener.onTrailersDataWasFetched(data);
                break;
            }
            case REQUEST_REVIEWS: {
                mListener.onReviewsDataWasFetched(data);
                break;
            }
        }
    }
}