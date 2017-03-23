package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TrailerDbObject  implements Parcelable {
    String mId;
    String mKey;
    String mName;

    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmId() {
        return mId;
    }

    public String getmKey() {
        return mKey;
    }

    public String getmName() {
        return mName;
    }

    public TrailerDbObject(String mId, String mKey, String mName) {
        this.mId = mId;
        this.mKey = mKey;
        this.mName = mName;
    }

    protected TrailerDbObject(Parcel in) {
        mId = in.readString();
        mKey = in.readString();
        mName = in.readString();
    }

    public static final Creator<TrailerDbObject> CREATOR = new Creator<TrailerDbObject>() {
        @Override
        public TrailerDbObject createFromParcel(Parcel in) {
            return new TrailerDbObject(in);
        }

        @Override
        public TrailerDbObject[] newArray(int size) {
            return new TrailerDbObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mKey);
        dest.writeString(mName);
    }
}
