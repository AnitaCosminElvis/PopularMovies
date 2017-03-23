package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.data.ReviewDbObject;

import java.util.ArrayList;


public class ReviewsAdapter extends ArrayAdapter<ReviewDbObject> {

    public ReviewsAdapter(Context context, ArrayList<ReviewDbObject> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewDbObject review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_row, parent, false);
        }

        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tv_author);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);

        tvAuthor.setText(review.getmAuthor());
        tvContent.setText(review.getmContent());

        return convertView;
    }
}