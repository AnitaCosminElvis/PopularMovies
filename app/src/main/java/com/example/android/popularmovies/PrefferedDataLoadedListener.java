package com.example.android.popularmovies;

import org.json.JSONArray;

public interface PrefferedDataLoadedListener{
    void onPrefferedDataWasFetched(JSONArray data);
}
