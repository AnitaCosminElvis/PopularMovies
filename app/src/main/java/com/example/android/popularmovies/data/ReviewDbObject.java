package com.example.android.popularmovies.data;


import android.os.Parcel;
import android.os.Parcelable;

public class ReviewDbObject  implements Parcelable {
    String mId;
    String mAuthor;
    String mContent;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmAuthor() {

        return mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public ReviewDbObject(String mId, String mAuthor, String mContent) {
        this.mId = mId;
        this.mAuthor = mAuthor;
        this.mContent = mContent;
    }

    protected ReviewDbObject(Parcel in) {
        mId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
    }

    public static final Creator<ReviewDbObject> CREATOR = new Creator<ReviewDbObject>() {
        @Override
        public ReviewDbObject createFromParcel(Parcel in) {
            return new ReviewDbObject(in);
        }

        @Override
        public ReviewDbObject[] newArray(int size) {
            return new ReviewDbObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }
}
