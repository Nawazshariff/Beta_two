package com.example.nawazshariff.beta_two.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.nawazshariff.beta_two.R;

/**
 * Created by nawazshariff on 09-10-2017.
 */

public class TimelineProfile extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_profile);
        initViews();
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initViews() {
        toolbar= (Toolbar) findViewById(R.id.profile_toolbar);
    }
}
