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

import com.example.android.popularmovies.data.MovieDbObject;
import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String BASE_IMAGE_URL_184 = "https://image.tmdb.org/t/p/w184";

    TextView        mTitle;
    TextView        mPlot;
    RatingBar       mRating;
    TextView        mReleaseDate;
    ImageView       mMoviePoster;
    ImageButton     mMarkAsFavouriteBtn;
    MovieDbObject   mMovieDbObject;

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
        mMarkAsFavouriteBtn = (ImageButton) findViewById(R.id.imageButton);

        Intent intent = getIntent();
        mMovieDbObject = intent.getParcelableExtra(MainActivity.MOVIE_DB_OBJECT);

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
            case R.id.imageButton: {
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
        }
    }
}
