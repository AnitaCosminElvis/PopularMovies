package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieDbObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String BASE_IMAGE_URL_184 = "https://image.tmdb.org/t/p/w184";

    TextView        mTitle;
    TextView        mPlot;
    RatingBar       mRating;
    TextView        mReleaseDate;
    ImageView       mMoviePoster;
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
    }
}
