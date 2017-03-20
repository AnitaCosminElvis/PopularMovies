package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Map;


public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {

    private static final String     BASE_IMAGE_URL_185 = "https://image.tmdb.org/t/p/w184";

    private Map<Long,String>        mMappedUrisById;
    private Long[]                  mIds;
    private LayoutInflater          mInflater;
    private ItemClickListener       mClickListener;

    public GridRecyclerViewAdapter(Context context, Map<Long,String> dataMap) {
        this.mInflater = LayoutInflater.from(context);
        this.mMappedUrisById = dataMap;
        this.mIds = (Long[]) dataMap.keySet().toArray(new Long[dataMap.size()]);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String uriString = mMappedUrisById.get(mIds[position]).toString();
        Uri uri = Uri.parse(BASE_IMAGE_URL_185 + uriString);
        holder.bind(uri);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mIds.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.iv_item_image);
            itemView.setOnClickListener(this);
        }

        void bind(Uri uri) {
            Picasso.with(mInflater.getContext()).load(uri).into(mImage);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public String getUriString(int position) {
        return mMappedUrisById.get(mIds[position]).toString();
    }

    public long getMovieId(int position){
        return mIds[position];
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}