package com.example.android.popularmovies.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
import org.json.JSONObject;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class HttpRequestManager extends AsyncTask<Void, Void, JSONArray> {

    private static final String     TAG = HttpRequestManager.class.getSimpleName();

    private static final String     BASE_POPULAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String     BASE_TOP_MOVIES_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static final String     API_KEY = "dummyApiKey";

    MainActivity.ESortPreference    mSortPreference;
    PrefferedDataLoadedListener     mListener;

    public HttpRequestManager(PrefferedDataLoadedListener listener,
                              MainActivity.ESortPreference eSortPreference) {
        mSortPreference = eSortPreference;
        mListener = listener;
    }

    public void setPreferenceType(MainActivity.ESortPreference eSortPreference){
        mSortPreference = eSortPreference;
    }

    public URL createUrl() {
        Log.v(TAG, "Creating URI ...");

        String urlString = null;

        switch (mSortPreference){
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
    protected JSONArray doInBackground(Void... params) {
        JSONArray data = null;
        try {
            String jsonResponse = requestDataFromHttpUrl(createUrl());

            data = TheMovieDBDataResponseManager.getTheMovieDbStringListFromJson(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(JSONArray data) {
            mListener.onPrefferedDataWasFetched(data);
    }
}