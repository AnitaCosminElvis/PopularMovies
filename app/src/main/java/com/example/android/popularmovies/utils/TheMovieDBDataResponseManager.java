package com.example.android.popularmovies.utils;

import android.net.Uri;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by home on 2/4/2017.
 */

public class TheMovieDBDataResponseManager {

    private static final String TMDB_RESULTS_KEY        = "results";
    private static final String TMDB_ID_KEY             = "id";
    private static final String TMDB_IMG_URI_KEY        = "poster_path";
    private static final String TMDB_DESCRIPTION_KEY    = "overview";
    private static final String TMDB_TITLE_KEY          = "title";
    private static final String TMDB_VOTE_AVG_KEY       = "vote_average";
    private static final String TMDB_RELEASE_DATE_KEY   = "release_date";

    public static  JSONArray getTheMovieDbStringListFromJson(String jsonResponse) throws JSONException {
        JSONObject jsonObj = new JSONObject(jsonResponse);
        JSONArray results = jsonObj.getJSONArray(TMDB_RESULTS_KEY);
        return results;
    }

    public static Long getMovieIdFromTheMovieDbJson(JSONObject jsonObj) throws JSONException {
        return jsonObj.getLong(TMDB_ID_KEY);
    }

    public static String getUriFromTheMovieDbJson(JSONObject jsonObj) throws JSONException {
        return jsonObj.getString(TMDB_IMG_URI_KEY);
    }

    public static String getTitleFromTheMovieDbJson(JSONObject jsonObj) throws JSONException {
        return jsonObj.getString(TMDB_TITLE_KEY);
    }

    public static String getPlotFromTheMovieDbJson(JSONObject jsonObj) throws JSONException {
        return jsonObj.getString(TMDB_DESCRIPTION_KEY);
    }

    public static double getUserRatingFromTheMovieDbJson(JSONObject jsonObj) throws JSONException {
        return jsonObj.getDouble(TMDB_VOTE_AVG_KEY);
    }

    public static String getReleaseDateFromTheMovieDbJson(JSONObject jsonObj) throws JSONException {
        return jsonObj.getString(TMDB_RELEASE_DATE_KEY);
    }
}
