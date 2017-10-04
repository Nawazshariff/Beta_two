package com.example.nawazshariff.beta_two.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.nawazshariff.beta_two.Model.Timeline_BusObject;
import com.example.nawazshariff.beta_two.Model.Timeline_RecyclerObject;
import com.example.nawazshariff.beta_two.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nawazshariff on 27-09-2017.
 */

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference timelineRef;
    public ArrayList<Timeline_RecyclerObject> currentItems = new ArrayList<Timeline_RecyclerObject>();

    private static String TAG = "splash screen";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        timelineRef = FirebaseDatabase.getInstance().getReference();

        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.e(TAG, "onAuthStateChanged : user signed in " + user.getUid());
                    delay(user);

                } else {
                    Log.e(TAG, "onAuthStateChanged : user signed out");
                    delay(user);

                }
            }
        };
        //get data from firebase
        getDataFromFirebase(timelineRef);
    }

    private void delay(final FirebaseUser user) {


        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Intent mIntent;
                    if (user != null) {
                        //TODO update this according to state
                        Log.d(TAG, "obtained items " + currentItems.size());
                        mIntent = new Intent(SplashScreen.this, Timeline.class);
                        mIntent.putExtra("items", (Serializable) currentItems);

                    } else {
                        mIntent = new Intent(SplashScreen.this, MainActivity.class);
                    }
                    startActivity(mIntent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        mThread.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(authStateListener);
        }

    }

    public void getDataFromFirebase(DatabaseReference timelineRef) {

        timelineRef.child("bus_data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currentSnap : dataSnapshot.getChildren()) {
                    Timeline_BusObject busObject = currentSnap.getValue(Timeline_BusObject.class);
                    Log.d(TAG, "getting data" + busObject.getName() + " " + busObject.getCost() + " " + busObject.getStars());
                    currentItems.add(new Timeline_RecyclerObject(busObject.getCost(), busObject.getName(), busObject.getStars()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
