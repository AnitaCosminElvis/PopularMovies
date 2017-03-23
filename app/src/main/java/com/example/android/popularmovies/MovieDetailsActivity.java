package com.example.android.popularmovies;

import android.R.drawable;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieDbObject;
import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.ReviewDbObject;
import com.example.android.popularmovies.data.TrailerDbObject;
import com.example.android.popularmovies.utils.HttpRequestManager;
import com.example.android.popularmovies.utils.TheMovieDBDataResponseManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.android.popularmovies.utils.HttpRequestManager.REQUEST_MOVIES;
import static com.example.android.popularmovies.utils.HttpRequestManager.REQUEST_REVIEWS;
import static com.example.android.popularmovies.utils.HttpRequestManager.REQUEST_TRAILERS;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener, PrefferedDataLoadedListener {

    private static final String BASE_IMAGE_URL_184  = "https://image.tmdb.org/t/p/w184";
    public static final String  TRAILER_DB_OBJECT   = "TRAILER_DB_OBJECT";
    public static final String  REVIEW_DB_OBJECT    = "REVIEW_DB_OBJECT";

    TextView                        mTitle;
    TextView                        mPlot;
    RatingBar                       mRating;
    TextView                        mReleaseDate;
    ImageView                       mMoviePoster;
    ImageButton                     mMarkAsFavouriteBtn;
    MovieDbObject                   mMovieDbObject;
    Map<String,ReviewDbObject>      mReviewsByIdMap;
    Map<String,TrailerDbObject>     mTrailersByIdMap;
    HttpRequestManager              mTrailersRequestsManager;
    HttpRequestManager              mReviewsRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mRating = (RatingBar) findViewById(R.id.ratingBar);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date_2);
        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster);
        mMarkAsFavouriteBtn = (ImageButton) findViewById(R.id.ib_favorites);

        Intent intent = getIntent();
        mMovieDbObject = intent.getParcelableExtra(MainActivity.MOVIE_DB_OBJECT);

        mTrailersRequestsManager = new HttpRequestManager(this, MainActivity.ESortPreference.E_UNKNOWN_SORT_TYPE , mMovieDbObject.getmMovieId());
        mTrailersRequestsManager.execute(REQUEST_TRAILERS);
        mReviewsRequestManager = new HttpRequestManager(this, MainActivity.ESortPreference.E_UNKNOWN_SORT_TYPE, mMovieDbObject.getmMovieId());
        mReviewsRequestManager.execute(REQUEST_REVIEWS);

        Uri uri = Uri.parse(BASE_IMAGE_URL_184 + mMovieDbObject.getmUriImageString());
        Context context = this.getApplicationContext();

        mTitle.setText(mMovieDbObject.getmTitle());
        mPlot.setText(mMovieDbObject.getmPlot());

        mRating.setMax(5);
        mRating.setNumStars(5);
        mRating.setStepSize(0.1f);
        mRating.setRating(mMovieDbObject.getmUserRating()/2f);
        mRating.setFocusableInTouchMode(false);
        mRating.setClickable(false);
        mRating.setIsIndicator(true);

        mReleaseDate.setText(mMovieDbObject.getmReleaseDate());

        Picasso.with(context).load(uri).into(mMoviePoster);

        if (false == mMovieDbObject.getIsFavorite()) {
            mMarkAsFavouriteBtn.setBackgroundResource(drawable.btn_star_big_off);
        }else{
            mMarkAsFavouriteBtn.setBackgroundResource(drawable.btn_star_big_on);
        }
        mMarkAsFavouriteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){


        switch (view.getId()) {
            case R.id.ib_favorites: {
                if (false == mMovieDbObject.getIsFavorite()) {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(MoviesContract.MovieEntry.COLUMN_DATE, System.currentTimeMillis());
                    contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, mMovieDbObject.getmMovieId());
                    contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_PLOT, mMovieDbObject.getmPlot());
                    contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_RATING, mMovieDbObject.getmUserRating());
                    contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mMovieDbObject.getmReleaseDate());
                    contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, mMovieDbObject.getmTitle());
                    contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_TYPE, mMovieDbObject.getmTitle());
                    contentValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_URI_IMAGE, mMovieDbObject.getmUriImageString());

                    ContentResolver contResolver = getContentResolver();

                    int nCount = contResolver.bulkInsert(MoviesContract.MovieEntry.CONTENT_URI,
                            new ContentValues[]{contentValues});

                    mMarkAsFavouriteBtn.setBackgroundResource(drawable.star_big_on);
                    mMovieDbObject.setIsFavorite(true);
                } else {
                    String selectionClause = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = ?";
                    String[] selectionArgs = {String.valueOf(mMovieDbObject.getmMovieId())};

                    int nCount = getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI,
                            selectionClause,
                            selectionArgs);

                    mMarkAsFavouriteBtn.setBackgroundResource(drawable.star_big_off);
                    mMovieDbObject.setIsFavorite(false);
                }
            }
            case R.id.ib_trailers:{
                Intent intent = new Intent(this, TrailersActivity.class);
                TrailerDbObject trailerObj = mTrailersByIdMap.get(String.valueOf(mMovieDbObject.getmMovieId()));
                intent.putExtra(TRAILER_DB_OBJECT,trailerObj);
                startActivity(intent);
            }
            case R.id.ib_reviews:{
                Intent intent = new Intent(this, ReviewsActivity.class);
                ReviewDbObject reviewObj = mReviewsByIdMap.get(String.valueOf(mMovieDbObject.getmMovieId()));
                intent.putExtra(REVIEW_DB_OBJECT,reviewObj);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onReviewsDataWasFetched(JSONArray data) {
        if (null == data){
            return;
        }

        if (false == mReviewsByIdMap.isEmpty()){
            return;
        }

        for (int index = 0; index < data.length(); index++) {
            JSONObject jsonObj = null;
            String jSonString;
            String id;
            String author;
            String content;

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
            } finally {
                try {
                    id = TheMovieDBDataResponseManager.getReviewIdFromTheDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    author = TheMovieDBDataResponseManager.getReviewAuthorFromTheDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    content = TheMovieDBDataResponseManager.getReviewContentFromTheDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                ReviewDbObject reviewData = new ReviewDbObject(id, author, content);

                mReviewsByIdMap.put(id, reviewData);
            }
        }
    }

    @Override
    public void onPrefferedDataWasFetched(JSONArray data) {

    }

    @Override
    public void onTrailersDataWasFetched(JSONArray data) {

        if (null == data) {
            return;
        }

        if (false == mTrailersByIdMap.isEmpty()){
            return;
        }

        for (int index = 0; index < data.length(); index++) {
            JSONObject jsonObj = null;
            String jSonString;
            String id;
            String key;
            String name;

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
            } finally {
                try {
                    id = TheMovieDBDataResponseManager.getVideoIdFromTheTrailerDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    key = TheMovieDBDataResponseManager.getVideoKeyFromTheTrailerDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    name = TheMovieDBDataResponseManager.getVideoNameFromTheTrailerDbJson(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }

                TrailerDbObject trailerData = new TrailerDbObject(id, key, name);

                mTrailersByIdMap.put(id, trailerData);
            }
        }
    }
}
