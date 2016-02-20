package com.djinnapp.djinn;

import android.graphics.Picture;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nacho on 20/02/2016.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.viewHolder>{

    private List<IPic> Pictures;

    public RVAdapter (List<IPic> Pictures) {
        this.Pictures = Pictures;
    }

    @Override
    public RVAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.piclayout, null);
        // create ViewHolder
        return new viewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        IPic item = Pictures.get(position);
        holder.Pic.setImageBitmap(null);
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        ImageView Pic;
        viewHolder(View itemView) {
            super(itemView);
            Pic = (ImageView)itemView.findViewById(R.id.iv);
        }
    }


}
