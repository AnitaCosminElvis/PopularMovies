package com.example.android.popularmovies.data;


public class MovieDbData {
    long    mMovieId;
    String  mUriImageString;
    String  mTitle;
    String  mPlot;
    double  mUserRating;
    String  mReleaseDate;

    public long getmMovieId() {
        return mMovieId;
    }

    public String getmUriImageString() {
        return mUriImageString;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmPlot() {
        return mPlot;
    }

    public double getmUserRating() {
        return mUserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmMovieId(long mMovieId) {
        this.mMovieId = mMovieId;
    }

    public void setmUriImageString(String mUriImageString) {
        this.mUriImageString = mUriImageString;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmPlot(String mPlot) {
        this.mPlot = mPlot;
    }

    public void setmUserRating(double mUserRating) {
        this.mUserRating = mUserRating;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }
}
