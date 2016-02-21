package com.djinnapp.djinn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

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
            thumb.setScaleType(ImageView.ScaleType.CENTER);
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

    public void moveItem(int fromPosition, int toPosition) {
        final Thumbnail thumbnail = mDataset.remove(fromPosition);
        mDataset.add(toPosition, thumbnail);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void removeItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public void animateTo(List<Thumbnail> thumbnails) {
        applyAndAnimateRemovals(thumbnails);
        applyAndAnimateAdditions(thumbnails);
        applyAndAnimateMovedItems(thumbnails);
    }

    private void applyAndAnimateRemovals(List<Thumbnail> newDataset) {
        for (int i = mDataset.size() - 1; i >= 0; i--) {
            final Thumbnail thumbnail = mDataset.get(i);
            if (!newDataset.contains(thumbnail)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Thumbnail> newDataset) {
        for (int i = 0, count = newDataset.size(); i < count; i++) {
            final Thumbnail thumbnail = newDataset.get(i);
            if (!mDataset.contains(thumbnail)) {
                addItem(thumbnail, i);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Thumbnail> newDataset) {
        for (int toPosition = newDataset.size() - 1; toPosition >= 0; toPosition--) {
            final Thumbnail thumbnail = newDataset.get(toPosition);
            final int fromPosition = mDataset.indexOf(thumbnail);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface ThumbnailClickListener {
        public void onItemClick(int position, View v);
    }
}