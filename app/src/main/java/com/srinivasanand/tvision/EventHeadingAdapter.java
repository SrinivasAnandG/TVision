package com.srinivasanand.tvision;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by srinivasanand on 29/10/17.
 */

public class EventHeadingAdapter extends RecyclerView.Adapter<EventHeadingAdapter.MyViewHolder> {
    private List<EventListValues> EventList;

    public EventHeadingAdapter(List<EventListValues> EventList) {
        this.EventList = EventList;
    }

    @Override
    public EventHeadingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.club_event_details, parent, false);

        return new EventHeadingAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventListValues movie = EventList.get(position);
        holder.names.setText(movie.getHeading());

    }





    @Override
    public int getItemCount() {
        return EventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView names, numbers;

        public MyViewHolder(View itemView) {
            super(itemView);
            names = (TextView) itemView.findViewById(R.id.heading);

        }

    }
}
