package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.android.popularmovies.data.ReviewDbObject;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {

    ArrayList<ReviewDbObject>   mReviews;
    ReviewsAdapter              mAdapter;
    ListView                    mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();
        mReviews = intent.getParcelableArrayListExtra(MovieDetailsActivity.REVIEW_DB_OBJECT);

        mAdapter = new ReviewsAdapter(this, mReviews);

        mListView = (ListView) findViewById(R.id.lv_reviews);

        mListView.setAdapter(mAdapter);

    }
}
