package com.example.nawazshariff.beta_two.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.Handler;
import android.os.Message;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nawazshariff.beta_two.Adapters.Timeline_RecyclerViewAdapter;
import com.example.nawazshariff.beta_two.Constants;
import com.example.nawazshariff.beta_two.Model.Timeline_RecyclerObject;
import com.example.nawazshariff.beta_two.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by nawazshariff on 16-09-2017.
 */

public class Timeline extends AppCompatActivity {
    private LinearLayoutManager lLayout;
    ProgressDialog progressDialog;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    AlertDialog alertDialog1;
    public ArrayList<Timeline_RecyclerObject> sortItems;
    public ArrayList<Timeline_RecyclerObject> currentItems = new ArrayList<Timeline_RecyclerObject>();
    Timeline_RecyclerViewAdapter rcAdapter;
    RecyclerView rView;

    DatabaseReference timelineRef;
    public static String TAG = "Timeline";
    ActionBarDrawerToggle actionBarDrawerToggle;
    Handler handle;


    CharSequence[] values = {" sort by price(lower to higher) ", "sort by price(higher to lower)", " sort by name "};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        timelineRef = FirebaseDatabase.getInstance().getReference();

        setTitle(null);
        initViews();

        //get data from firebase
        getDataFromFirebase(timelineRef);

        setProgressBar();
        setHandleForProgressBar();

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        setNavigationItemSelector();

        //checking if drawer is opened or closed
        checkStatusOfDrawer();

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        setRecyclerView();


    }

    public void getDataFromFirebase(DatabaseReference timelineRef) {

        timelineRef.child(Constants.BUS_ROOT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currentSnap : dataSnapshot.getChildren()) {
                    Timeline_RecyclerObject timelineObject = currentSnap.getValue(Timeline_RecyclerObject.class);
                    Log.d(TAG, "getting data" + timelineObject.getName() + " " + timelineObject.getCost() + " " + timelineObject.getStars());
                    currentItems.add(new Timeline_RecyclerObject(timelineObject.getCost(),timelineObject.getDate(), timelineObject.getName(), timelineObject.getStars()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "error in retrieving data", databaseError.toException());
            }
        });


    }

    private void setHandleForProgressBar() {
        handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(10);
            }
        };
    }

    private void setProgressBar() {
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading data ......Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progressDialog.getProgress() <= progressDialog
                            .getMax()) {
                        Thread.sleep(200);
                        handle.sendMessage(handle.obtainMessage());

                        if (progressDialog.getProgress() == progressDialog
                                .getMax()) {
                            progressDialog.dismiss();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setRecyclerView() {

        rView.setLayoutManager(lLayout);
        FirebaseRecyclerAdapter<Timeline_RecyclerObject, RecyclerViewHolder> adapter = new FirebaseRecyclerAdapter<Timeline_RecyclerObject, RecyclerViewHolder>(
                Timeline_RecyclerObject.class, R.layout.card_timeline, RecyclerViewHolder.class, timelineRef.child("bus_data").getRef()) {
            @Override
            protected void populateViewHolder(RecyclerViewHolder viewHolder, Timeline_RecyclerObject model, int position) {
                viewHolder.travel_name.setText(model.getName());
                viewHolder.cost.setText("RS" + model.getCost());
                viewHolder.ratingBar.setRating(model.getStars());
                viewHolder.date_tv.setText(model.getDate());
            }
        };
        rView.setAdapter(adapter);

    }


    private void checkStatusOfDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
    }

    private void setNavigationItemSelector() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                Intent intent;
                //Closing drawer on item click
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        Log.i("profile", "clicked");
                      //  Toast.makeText(Timeline.this,"profile was clicked",Toast.LENGTH_SHORT).show();
                         intent=new Intent(Timeline.this,TimelineProfile.class);
                        startActivity(intent);
                        return true;
                    case R.id.notifications:
                        Toast.makeText(Timeline.this,"notification was clicked",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.help:
                        Toast.makeText(Timeline.this,"help was clicked",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.history:
                        Toast.makeText(Timeline.this,"history was clicked",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.logout:
                        Log.i("Log out", "clicked");
                        FirebaseAuth.getInstance().signOut();
                         intent = new Intent(Timeline.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }

                return false;
            }
        });
    }


    private void initViews() {
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        lLayout = new LinearLayoutManager(Timeline.this);
        rView = (RecyclerView) findViewById(R.id.recycler_view);

    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView travel_name;
        public TextView cost;
        public RatingBar ratingBar;
        public TextView date_tv;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            travel_name = itemView.findViewById(R.id.travel_name);
            cost = itemView.findViewById(R.id.cost_travel);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            date_tv=itemView.findViewById(R.id.date_tv);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "Clicked travel Position = " + getPosition(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(view.getContext(), Timeline_facility.class);
                    intent.putExtra("position",getPosition());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort) {
            CreateAlertDialogWithRadioButtonGroup();
        }
        return super.onOptionsItemSelected(item);


    }

    public void CreateAlertDialogWithRadioButtonGroup() {


        AlertDialog.Builder builder = new AlertDialog.Builder(Timeline.this);

        builder.setTitle("Select Your Choice");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                onClickRadioButton(item);
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    private void onClickRadioButton(int item) {
        switch (item) {
            case 0:
                // Toast.makeText(Timeline.this,"lower to higher",Toast.LENGTH_SHORT).show();
                sortItems = currentItems;
                Collections.sort(sortItems, new Comparator<Timeline_RecyclerObject>() {
                    @Override
                    public int compare(Timeline_RecyclerObject timeline_recyclerObject, Timeline_RecyclerObject t1) {
                        if (timeline_recyclerObject.getCost() == t1.getCost())
                            return 0;
                        else if (timeline_recyclerObject.getCost() < t1.getCost())
                            return -1;
                        else
                            return 1;
                    }
                });
                rcAdapter = new Timeline_RecyclerViewAdapter(Timeline.this, sortItems);
                rView.setAdapter(rcAdapter);

                break;
            case 1:
                // Toast.makeText(Timeline.this,"lower to higher",Toast.LENGTH_SHORT).show();
                sortItems = currentItems;
                Collections.sort(sortItems, new Comparator<Timeline_RecyclerObject>() {
                    @Override
                    public int compare(Timeline_RecyclerObject timeline_recyclerObject, Timeline_RecyclerObject t1) {
                        if (t1.getCost() == timeline_recyclerObject.getCost())
                            return 0;
                        else if (t1.getCost() < timeline_recyclerObject.getCost())
                            return -1;
                        else
                            return 1;
                    }
                });
                rcAdapter = new Timeline_RecyclerViewAdapter(Timeline.this, sortItems);
                rView.setAdapter(rcAdapter);

                break;
            case 2:
                sortItems = currentItems;
                Collections.sort(sortItems, new Comparator<Timeline_RecyclerObject>() {
                    @Override
                    public int compare(Timeline_RecyclerObject timeline_recyclerObject, Timeline_RecyclerObject t1) {
                        return timeline_recyclerObject.getName().compareTo(t1.getName());
                    }
                });
                rcAdapter = new Timeline_RecyclerViewAdapter(Timeline.this, sortItems);
                rView.setAdapter(rcAdapter);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
