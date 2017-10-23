package com.example.nawazshariff.beta_two.Adapters;

/**
 * Created by nawazshariff on 18-09-2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nawazshariff.beta_two.Model.Timeline_RecyclerObject;
import com.example.nawazshariff.beta_two.R;

import java.util.List;

public class Timeline_RecyclerViewAdapter extends RecyclerView.Adapter<Timeline_RecyclerViewHolders> {

    private List<Timeline_RecyclerObject> itemList;
    private Context context;

    public Timeline_RecyclerViewAdapter(Context context, List<Timeline_RecyclerObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public Timeline_RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_timeline, null);
        Timeline_RecyclerViewHolders rcv = new Timeline_RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(Timeline_RecyclerViewHolders holder, int position) {
        holder.travel_name.setText(itemList.get(position).getName());
        holder.cost.setText("INR "+itemList.get(position).getCost());
        holder.ratingBar.setRating(itemList.get(position).getStars());
        holder.date.setText(itemList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}