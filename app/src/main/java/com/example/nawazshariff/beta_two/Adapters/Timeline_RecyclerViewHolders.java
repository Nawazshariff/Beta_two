package com.example.nawazshariff.beta_two.Adapters;

/**
 * Created by nawazshariff on 18-09-2017.
 */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nawazshariff.beta_two.Activities.Timeline;
import com.example.nawazshariff.beta_two.Activities.Timeline_facility;
import com.example.nawazshariff.beta_two.R;


public class Timeline_RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView travel_name,date;
    public  TextView cost;
    public RatingBar ratingBar;

    public Timeline_RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        travel_name = (TextView)itemView.findViewById(R.id.travel_name);
        cost=itemView.findViewById(R.id.cost_travel);
        ratingBar=itemView.findViewById(R.id.ratingBar);
        date=itemView.findViewById(R.id.date_tv);
        LayerDrawable stars= (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
       // Toast.makeText(view.getContext(), "Clicked travel Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(view.getContext(), Timeline_facility.class);
        intent.putExtra("position",getPosition());
        view.getContext().startActivity(intent);

    }
}