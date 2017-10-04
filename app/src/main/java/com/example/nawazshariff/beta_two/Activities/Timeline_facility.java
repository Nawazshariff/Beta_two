package com.example.nawazshariff.beta_two.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.nawazshariff.beta_two.R;

/**
 * Created by nawazshariff on 27-09-2017.
 */

public class Timeline_facility extends AppCompatActivity {
    private static String TAG="timeline facility";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_facility_activity);
        Intent intent=getIntent();
        Log.d(TAG,"facility for bus "+intent.getIntExtra("position",0));
    }
}
