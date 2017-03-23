package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.android.popularmovies.data.TrailerDbObject;

import java.util.ArrayList;

public class TrailersActivity extends AppCompatActivity {

    public static  final String YOUTUBE_PATH = "http://www.youtube.com/watch?v=";

    ArrayList<TrailerDbObject>  mTrailers;
    TrailersAdapter             mAdapter;
    ListView                    mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        Intent intent = getIntent();
        mTrailers = intent.getParcelableArrayListExtra(MovieDetailsActivity.TRAILER_DB_OBJECT);

        mAdapter = new TrailersAdapter(this, mTrailers);

        mListView = (ListView) findViewById(R.id.lv_trailers);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            TrailerDbObject item = (TrailerDbObject) parent.getItemAtPosition(position);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_PATH + item.getmKey())));
            }
        });
    }
}


