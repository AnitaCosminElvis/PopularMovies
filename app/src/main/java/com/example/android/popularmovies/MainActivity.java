package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.utils.HttpRequestManager;
import com.example.android.popularmovies.utils.TheMovieDBDataResponseManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements  GridRecyclerViewAdapter.ItemClickListener,
                                                                PrefferedDataLoadedListener,
                                                                LoaderManager.LoaderCallbacks<Cursor> {

    public enum ESortPreference{
        E_TOP_RATED_MOVIE,
        E_MOST_POPULAR_MOVIE,
        E_FAVOURITE_MOVIE,
        E_UNKNOWN_SORT_TYPE
    }

    public static final String      SORT_MOVIE_PREFERENCE   = "SortMoviePreference";
    public static final String      MOVIE_DB_OBJECT         = "MOVIE_DB_OBJECT";
    private static final int        ID_MOVIE_LOADER         = 10001;


    ProgressBar                     mProgressBar;
    SharedPreferences               mSharedPrefs;
    Toast                           mGeneralToast;
    RecyclerView                    mRecyclerView;
    GridRecyclerViewAdapter         mGridAdapter;
    HttpRequestManager              mHttpRequestManager;
    Map<Long,MovieDbObject>         mMovieDetailsByIdMap;
    Cursor                          mCurrentCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                break;
            case R.id.most:
                mSharedPrefs.edit().putInt(SORT_MOVIE_PREFERENCE,ESortPreference.E_MOST_POPULAR_MOVIE.ordinal()).commit();
                mGeneralToast.setText(getResources().getString(R.string.most_popular));
                break;
            case R.id.fav:
                mSharedPrefs.edit().putInt(SORT_MOVIE_PREFERENCE,ESortPreference.E_FAVOURITE_MOVIE.ordinal()).commit();
                mGeneralToast.setText(getResources().getString(R.string.favourite));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        mGeneralToast.setDuration(Toast.LENGTH_SHORT);
        mGeneralToast.show();
        fetchMovieData();

        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        long movieId = mGridAdapter.getMovieId(position);
        MovieDbObject movieData = mMovieDetailsByIdMap.get(movieId);

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
            String jSonString ;
            Long id ;
            String uriString;
            String plotString ;
            String titleString ;
            float userRating ;
            String releaseDate ;
            boolean bIsFavorite = false;

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
                movieData.setType(mSharedPrefs.getInt(SORT_MOVIE_PREFERENCE,0));

                if (null != mCurrentCursor && 0 < mCurrentCursor.getCount()) {
                    for(mCurrentCursor.moveToFirst(); !mCurrentCursor.isAfterLast(); mCurrentCursor.moveToNext()) {
                        if (mCurrentCursor.getString(MoviesContract.MovieEntry.INDEX_MOVIE_ID).equals(String.valueOf(id))) {
                            bIsFavorite = true;
                            break;
                        }
                    }
                }

                movieData.setIsFavorite(bIsFavorite);

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

    public void onRefreshViewByFavouriteDbData() {
        Map<Long,String> dataMap = new HashMap<Long,String>();
        int numberOfColumns = 2;

        if (null == mCurrentCursor){
            return;
        }

        if (0 >= mCurrentCursor.getCount()){
            mGeneralToast.setText(R.string.empty_favorites);
            mGeneralToast.setDuration(Toast.LENGTH_LONG);
            mGeneralToast.show();
            stopProgressBar();
            return;
        }

        for(mCurrentCursor.moveToFirst(); !mCurrentCursor.isAfterLast(); mCurrentCursor.moveToNext()) {
            MovieDbObject movieData = new MovieDbObject();
            movieData.setmMovieId(mCurrentCursor.getLong(MoviesContract.MovieEntry.INDEX_MOVIE_ID));
            movieData.setmPlot(mCurrentCursor.getString(MoviesContract.MovieEntry.INDEX_PLOT));
            movieData.setmReleaseDate(mCurrentCursor.getString(MoviesContract.MovieEntry.INDEX_RELEASE_DATE));
            movieData.setmTitle(mCurrentCursor.getString(MoviesContract.MovieEntry.INDEX_TITLE));
            movieData.setmUriImageString(mCurrentCursor.getString(MoviesContract.MovieEntry.INDEX_URI_IMAGE));
            movieData.setmUserRating(mCurrentCursor.getFloat(MoviesContract.MovieEntry.INDEX_RATING));
            movieData.setType(mCurrentCursor.getType(MoviesContract.MovieEntry.INDEX_MOVIE_TYPE));
            movieData.setIsFavorite(true);

            dataMap.put(movieData.getmMovieId(),movieData.getmUriImageString());
            mMovieDetailsByIdMap.put(movieData.getmMovieId(),movieData);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_MOVIE_LOADER:
                Uri forecastQueryUri = MoviesContract.MovieEntry.CONTENT_URI;
                String sortOrder = MoviesContract.MovieEntry.COLUMN_DATE + " ASC";

                return new CursorLoader(this, forecastQueryUri, null, null, null, sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (null != data && 0 < data.getCount()) {
            mCurrentCursor = data;
            if (2 == mSharedPrefs.getInt(SORT_MOVIE_PREFERENCE,0)){
                fetchMovieData();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (2 == mSharedPrefs.getInt(SORT_MOVIE_PREFERENCE,0)){
            fetchMovieData();
        }
    }

    private void fetchMovieData(){
        startProgressBar();

        if ((null != mRecyclerView) ){
            mRecyclerView.removeAllViewsInLayout();
            mRecyclerView.setAdapter(null);
        }

        mMovieDetailsByIdMap.clear();

        int nValue = mSharedPrefs.getInt(SORT_MOVIE_PREFERENCE,0);

        switch(nValue){
            case 0:{
                mHttpRequestManager = new HttpRequestManager(MainActivity.this, ESortPreference.E_TOP_RATED_MOVIE);
                mHttpRequestManager.execute();
                break;
            }
            case 1:{
                mHttpRequestManager = new HttpRequestManager(MainActivity.this, ESortPreference.E_MOST_POPULAR_MOVIE);
                mHttpRequestManager.execute();
                break;
            }
            case 2:{
                onRefreshViewByFavouriteDbData();
                break;
            }
            default:{
                mHttpRequestManager = new HttpRequestManager(MainActivity.this, ESortPreference.E_TOP_RATED_MOVIE);
                mHttpRequestManager.execute();
                break;
            }
        }

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
