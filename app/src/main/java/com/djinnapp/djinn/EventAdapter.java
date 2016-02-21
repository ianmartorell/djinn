package com.djinnapp.djinn;

import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ian on 20/02/16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {

    private static String LOG_TAG = "EventAdapter";
    private ArrayList<Event> mDataset;
    private static EventClickListener eventClickListener;

    public EventAdapter(ArrayList<Event> dataset) {
        mDataset = dataset;
    }

    public void setOnItemClickListener(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    public static class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView label;
        TextView date;

        public EventHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label_textView);
            date = (TextView) itemView.findViewById(R.id.date_textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            eventClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_recyclerview_item, parent, false);

        EventHolder eventHolder = new EventHolder(view);
        return eventHolder;
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getName());
    }

    public void addItem(Event event, int index) {
        mDataset.add(index, event);
        notifyItemInserted(index);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Event event = mDataset.remove(fromPosition);
        mDataset.add(toPosition, event);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Event removeItem(int index) {
        final Event event = mDataset.remove(index);
        notifyItemRemoved(index);
        return event;
    }

    public void animateTo(List<com.djinnapp.djinn.Event> events) {
        applyAndAnimateRemovals(events);
        applyAndAnimateAdditions(events);
        applyAndAnimateMovedItems(events);
    }

    private void applyAndAnimateRemovals(List<Event> newDataset) {
        for (int i = mDataset.size() - 1; i >= 0; i--) {
            final Event event = mDataset.get(i);
            if (!newDataset.contains(event)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Event> newDataset) {
        for (int i = 0, count = newDataset.size(); i < count; i++) {
            final Event event = newDataset.get(i);
            if (!mDataset.contains(event)) {
                addItem(event, i);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Event> newDataset) {
        for (int toPosition = newDataset.size() - 1; toPosition >= 0; toPosition--) {
            final Event event = newDataset.get(toPosition);
            final int fromPosition = mDataset.indexOf(event);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public ArrayList<Event> getDataset() {
        return mDataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public String getItemMongoId(int position) {
        return mDataset.get(position).getId();
    }

    public interface EventClickListener {
        public void onItemClick(int position, View v);
    }
}