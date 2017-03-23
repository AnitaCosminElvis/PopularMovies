package com.example.android.popularmovies.data;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieDbObject implements Parcelable {
    long    mMovieId;
    String  mUriImageString;
    String  mTitle;
    String  mPlot;
    float   mUserRating;
    String  mReleaseDate;
    int     mType;
    boolean mIsFavourite;

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

    public float getmUserRating() {
        return mUserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public int getType() {
        return mType;
    }

    public boolean getIsFavorite() {
        return mIsFavourite;
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

    public void setmUserRating(float mUserRating) {
        this.mUserRating = mUserRating;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setType(int nType) {
        this.mType = nType;
    }

    public void setIsFavorite(boolean bIsFavorite) {
        this.mIsFavourite = bIsFavorite;
    }

    public MovieDbObject(long mMovieId,
                         String mUriImageString,
                         String mTitle,
                         String mPlot,
                         float mUserRating,
                         String mReleaseDate,
                         int mType,
                         boolean mIsFavourite) {
        this.mMovieId = mMovieId;
        this.mUriImageString = mUriImageString;
        this.mTitle = mTitle;
        this.mPlot = mPlot;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
        this.mType = mType;
        this.mIsFavourite = mIsFavourite;
    }

    public MovieDbObject(){

    }

    public MovieDbObject(Parcel in){
        String[] data = new String[4];
        boolean[] bools = new boolean[1];

        in.readStringArray(data);
        this.mUriImageString = data[0];
        this.mTitle = data[1];
        this.mPlot = data[2];
        this.mReleaseDate = data[3];
        this.mMovieId = in.readLong();
        this.mUserRating = in.readFloat();
        this.mType = in.readInt();
        in.readBooleanArray(bools);
        this.mIsFavourite = bools[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.mUriImageString, this.mTitle, this.mPlot, this.mReleaseDate});
        dest.writeLong(this.mMovieId);
        dest.writeFloat(this.mUserRating);
        dest.writeInt(this.mType);
        dest.writeBooleanArray(new boolean[] {this.mIsFavourite});
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieDbObject> CREATOR = new Parcelable.Creator<MovieDbObject>() {

        @Override
        public MovieDbObject createFromParcel(Parcel in) {
            return new MovieDbObject(in);
        }

        @Override
        public MovieDbObject[] newArray(int size) {
            return new MovieDbObject[size];
        }
    };
}
