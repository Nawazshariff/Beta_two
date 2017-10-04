package com.example.nawazshariff.beta_two.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.nawazshariff.beta_two.Adapters.Timeline_RecyclerViewAdapter;
import com.example.nawazshariff.beta_two.Model.Timeline_RecyclerObject;
import com.example.nawazshariff.beta_two.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public ArrayList<Timeline_RecyclerObject> sortItems,actualItems;
    public ArrayList<Timeline_RecyclerObject> currentItems=new ArrayList<Timeline_RecyclerObject>();
    Timeline_RecyclerViewAdapter rcAdapter;
    RecyclerView rView;

    DatabaseReference timelineRef;
    public static String TAG = "Timeline";
    ActionBarDrawerToggle actionBarDrawerToggle;
    int recyclerSize;
    Handler handle;


    CharSequence[] values = {" sort by price(lower to higher) ", "sort by price(higher to lower)", " sort by name "};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        timelineRef = FirebaseDatabase.getInstance().getReference();

        setTitle(null);
        initViews();

        currentItems= (ArrayList<Timeline_RecyclerObject>) getIntent().getSerializableExtra("items");



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

        Log.d(TAG,"data from firebase"+currentItems.size());
        lLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rView.setLayoutManager(lLayout);

        rcAdapter = new Timeline_RecyclerViewAdapter(Timeline.this, currentItems);
        rView.setAdapter(rcAdapter);

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

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        Log.i("profile", "clicked");
                        Intent intent1 = new Intent(Timeline.this, ProfileTimeline.class);
                        startActivity(intent1);

                        return true;

                    case R.id.logout:
                        Log.i("Log out", "clicked");
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Timeline.this, MainActivity.class);
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);

        //Initializing NavigationView
        navigationView = findViewById(R.id.navigation_view);

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = findViewById(R.id.drawer);
        lLayout = new LinearLayoutManager(Timeline.this);
        rView = findViewById(R.id.recycler_view);

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
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }
}
