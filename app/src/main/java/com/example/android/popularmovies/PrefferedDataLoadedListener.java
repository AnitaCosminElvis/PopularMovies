package com.example.android.popularmovies;

import org.json.JSONArray;

public interface PrefferedDataLoadedListener{
    void onPrefferedDataWasFetched(JSONArray data);

    void onTrailersDataWasFetched(JSONArray data);

    void onReviewsDataWasFetched(JSONArray data);
}

