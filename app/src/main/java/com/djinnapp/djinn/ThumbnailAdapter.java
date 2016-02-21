package com.djinnapp.djinn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by ian on 20/02/16.
 */
public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailHolder> {

    private static String LOG_TAG = "ThumbnailAdapter";
    private ArrayList<Thumbnail> mDataset;
    private static ThumbnailClickListener thumbnailClickListener;

    public ThumbnailAdapter(ArrayList<Thumbnail> dataset) {
        mDataset = dataset;
    }

    public void setOnItemClickListener(ThumbnailClickListener thumbnailClickListener) {
        this.thumbnailClickListener = thumbnailClickListener;
    }

    public static class ThumbnailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumb;

        public ThumbnailHolder(View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById(R.id.label_imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            thumbnailClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    @Override
    public ThumbnailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_recyclerview_item, parent, false);

        ThumbnailHolder thumbnailHolder = new ThumbnailHolder(view);
        return thumbnailHolder;
    }

    @Override
    public void onBindViewHolder(ThumbnailHolder holder, int position) {
        holder.thumb.setImageBitmap(mDataset.get(position).getThumbnail());
    }

    public void addItem(Thumbnail thumbnail, int index) {
        mDataset.add(thumbnail);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface ThumbnailClickListener {
        public void onItemClick(int position, View v);
    }
}