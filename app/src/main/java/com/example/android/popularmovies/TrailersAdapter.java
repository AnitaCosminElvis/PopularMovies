package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.android.popularmovies.data.TrailerDbObject;


public class TrailersAdapter extends ArrayAdapter<TrailerDbObject> {

    public TrailersAdapter(Context context, ArrayList<TrailerDbObject> trailers) {
        super(context, 0, trailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrailerDbObject trailer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_row, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_trailer_name);

        tvName.setText(trailer.getmName());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}