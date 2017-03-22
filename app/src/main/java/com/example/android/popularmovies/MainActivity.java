package com.example.android.popularmovies;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieDbObject;
import com.example.android.popularmovies.data.MoviesProvider;
import com.example.android.popularmovies.utils.HttpRequestManager;
import com.example.android.popularmovies.utils.TheMovieDBDataResponseManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GridRecyclerViewAdapter.ItemClickListener, PrefferedDataLoadedListener {

    public enum ESortPreference{
        E_TOP_RATED_MOVIE,
        E_MOST_POPULAR_MOVIE,
        E_FAVOURITE_MOVIE,
        E_UNKNOWN_SORT_TYPE
    }

    public static final String      SORT_MOVIE_PREFERENCE   = "SortMoviePreference";
    public static final String      MOVIE_DB_OBJECT         = "MOVIE_DB_OBJECT";

    ProgressBar                     mProgressBar;
    SharedPreferences               mSharedPrefs;
    Toast                           mGeneralToast;
    RecyclerView                    mRecyclerView;
    GridRecyclerViewAdapter         mGridAdapter;
    HttpRequestManager              mHttpRequestManager;
    MoviesProvider                  mMoviesProvider;
    Map<Long,MovieDbObject>         mMovieDetailsByIdMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mMovieDetailsByIdMap = new HashMap<Long,MovieDbObject>();

        if (savedInstanceState == null) {
            mSharedPrefs.edit().putInt(SORT_MOVIE_PREFERENCE,0);
        } else {
            mSharedPrefs.edit().putInt(SORT_MOVIE_PREFERENCE,savedInstanceState.getInt(SORT_MOVIE_PREFERENCE,0));
        }

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_grid_images);

        mGeneralToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        fetchMovieData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.top:
                mSharedPrefs.edit().putInt(SORT_MOVIE_PREFERENCE,ESortPreference.E_TOP_RATED_MOVIE.ordinal()).commit();
                mGeneralToast.setText(getResources().getString(R.string.top_rated));
                mGeneralToast.setDuration(Toast.LENGTH_SHORT);
                mGeneralToast.show();
                fetchMovieData();
                return true;
            case R.id.most:
                mSharedPrefs.edit().putInt(SORT_MOVIE_PREFERENCE,ESortPreference.E_MOST_POPULAR_MOVIE.ordinal()).commit();
                mGeneralToast.setText(getResources().getString(R.string.most_popular));
                mGeneralToast.setDuration(Toast.LENGTH_SHORT);
                mGeneralToast.show();
                fetchMovieData();
            case R.id.fav:
                mSharedPrefs.edit().putInt(SORT_MOVIE_PREFERENCE,ESortPreference.E_FAVOURITE_MOVIE.ordinal()).commit();
                mGeneralToast.setText(getResources().getString(R.string.favourite));
                mGeneralToast.setDuration(Toast.LENGTH_SHORT);
                mGeneralToast.show();
                fetchMovieData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        long movieId = mGridAdapter.getMovieId(position);
        MovieDbObject movieData = mMovieDetailsByIdMap.get(movieId);
        String uriString = movieData.getmUriImageString();
        String plotString = movieData.getmPlot();
        String titleString = movieData.getmTitle();
        double userRating = movieData.getmUserRating();
        String releaseDate = movieData.getmReleaseDate();

        intent.putExtra(MOVIE_DB_OBJECT,movieData);

        startActivity(intent);
    }

    @Override
    public void onPrefferedDataWasFetched(JSONArray data) {
        Map<Long,String> dataMap = new HashMap<Long,String>();
        int numberOfColumns = 2;

        if (null == data){
            mGeneralToast.setText(R.string.network_error);
            mGeneralToast.setDuration(Toast.LENGTH_LONG);
            mGeneralToast.show();
            stopProgressBar();
            return;
        }

        for (int index = 0; index < data.length(); index++){
            JSONObject jsonObj = null;
            String jSonString = null;
            Long id = Long.valueOf(0);
            String uriString;
            String plotString = null;
            String titleString = null;
            float userRating = 0;
            String releaseDate = null;

            try {
                jSonString = data.getString(index);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            try {
                jsonObj = new JSONObject(jSonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                try {
                    id = TheMovieDBDataResponseManager.getMovieIdFromTheMovieDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    uriString = TheMovieDBDataResponseManager.getUriFromTheMovieDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    plotString = TheMovieDBDataResponseManager.getPlotFromTheMovieDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    releaseDate = TheMovieDBDataResponseManager.getReleaseDateFromTheMovieDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    titleString = TheMovieDBDataResponseManager.getTitleFromTheMovieDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    userRating = (float) TheMovieDBDataResponseManager.getUserRatingFromTheMovieDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                MovieDbObject movieData = new MovieDbObject();
                movieData.setmMovieId(id);
                movieData.setmPlot(plotString);
                movieData.setmReleaseDate(releaseDate);
                movieData.setmTitle(titleString);
                movieData.setmUriImageString(uriString);
                movieData.setmUserRating(userRating);

                dataMap.put(id,uriString);
                mMovieDetailsByIdMap.put(id,movieData);
            }
        }

        stopProgressBar();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mGridAdapter = new GridRecyclerViewAdapter(this, dataMap);
        mGridAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mGridAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(SORT_MOVIE_PREFERENCE, mSharedPrefs.getInt(SORT_MOVIE_PREFERENCE,0));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSharedPrefs.edit().putInt(SORT_MOVIE_PREFERENCE,savedInstanceState.getInt(SORT_MOVIE_PREFERENCE));
    }

    private void fetchMovieData(){
        startProgressBar();

        if ((null != mRecyclerView) ){
            mRecyclerView.removeAllViewsInLayout();
        }

        switch(mSharedPrefs.getInt(SORT_MOVIE_PREFERENCE,0)){
            case 0:{
                mHttpRequestManager = new HttpRequestManager(MainActivity.this, ESortPreference.E_TOP_RATED_MOVIE);
                break;
            }
            case 1:{
                mHttpRequestManager = new HttpRequestManager(MainActivity.this, ESortPreference.E_MOST_POPULAR_MOVIE);
                break;
            }
            case 2:{
                //mMoviesProvider.query();
                break;
            }
            default:{
                mHttpRequestManager = new HttpRequestManager(MainActivity.this, ESortPreference.E_TOP_RATED_MOVIE);
                break;
            }
        }
        mHttpRequestManager.execute();
    }

    private void startProgressBar(){
        if (null != mProgressBar){
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void stopProgressBar(){
        if (null != mProgressBar){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
