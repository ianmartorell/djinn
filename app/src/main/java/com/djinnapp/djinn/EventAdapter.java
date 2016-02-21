package com.djinnapp.djinn;

import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import java.util.ArrayList;

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
        mDataset.add(event);
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

    public String getItemMongoId(int position) {
        return mDataset.get(position).getId();
    }

    public interface EventClickListener {
        public void onItemClick(int position, View v);
    }
}