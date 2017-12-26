package com.srinivasanand.tvision;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by srinivasanand on 23/09/17.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private List<ContactsListValues> ContactList;

    public ContactsAdapter(List<ContactsListValues> ContactLists)
    {
        this.ContactList=ContactLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_recycler_view, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ContactsListValues movie = ContactList.get(position);
        holder.names.setText(movie.getName());
        holder.numbers.setText(movie.getNumber());


    }



    @Override
    public int getItemCount() {
        return ContactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView names,numbers;
        public MyViewHolder(View itemView) {
            super(itemView);
            names=(TextView)itemView.findViewById(R.id.name);
            numbers=(TextView)itemView.findViewById(R.id.number);
        }
    }
}
