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
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String BASE_IMAGE_URL_184 = "https://image.tmdb.org/t/p/w184";

    TextView    mTitle;
    TextView    mPlot;
    TextView    mRating;
    TextView    mReleaseDate;
    ImageView   mMoviePoster;
    long        mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mTitle = (TextView) findViewById(R.id.tv_movie_title);
        mPlot = (TextView) findViewById(R.id.tv_plot);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster);

        Intent intent = getIntent();

        mMovieId = intent.getLongExtra(MainActivity.MOVIE_DETAILS_ID,0);
        String uriString = intent.getStringExtra(MainActivity.MOVIE_DETAILS_URI);
        String plotString = intent.getStringExtra(MainActivity.MOVIE_DETAILS_PLOT);
        String titleString = intent.getStringExtra(MainActivity.MOVIE_DETAILS_TITLE);
        double userRating = intent.getDoubleExtra(MainActivity.MOVIE_DETAILS_RATING,0);
        String releaseDate = intent.getStringExtra(MainActivity.MOVIE_DETAILS_DATE);

        Uri uri = Uri.parse(BASE_IMAGE_URL_184 + uriString);
        Context context = this.getApplicationContext();


        mTitle.setText(titleString);
        mPlot.setText(plotString);

        String strRatingFormat = String.format(getResources().getString(R.string.rating), String.valueOf(userRating));
        mRating.setText(String.valueOf(strRatingFormat));

        String strRelDateFormat = String.format(getResources().getString(R.string.releaseDate), releaseDate);
        mReleaseDate.setText(strRelDateFormat);

        Picasso.with(context).load(uri).into(mMoviePoster);

    }
}
