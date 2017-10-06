package com.example.nawazshariff.beta_two.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nawazshariff.beta_two.Constants;
import com.example.nawazshariff.beta_two.R;

/**
 * Created by nawazshariff on 06-10-2017.
 */

public class CompleteProfile extends AppCompatActivity {
    SharedPreferences completeProfilePref;
    EditText name,contactNumber,address;
    ImageView idProof;
    Button browseButton,submitDataButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_profile);
        setSharedPreferences();
        initViews();

    }

    private void initViews() {
        name=findViewById(R.id.complete_profile_name);
        contactNumber=findViewById(R.id.complete_profile_number);
        address=findViewById(R.id.complete_profile_addresss);
        idProof=findViewById(R.id.id_proof_image);
        browseButton=findViewById(R.id.browse);
        submitDataButton=findViewById(R.id.submit);

    }

    private void setSharedPreferences() {
        completeProfilePref=getApplicationContext().getSharedPreferences(Constants.COMPLETE_PROFILE,MODE_PRIVATE);
        SharedPreferences.Editor editor=completeProfilePref.edit();
        editor.putBoolean("isProfileComplete",false);
        editor.apply();
    }

    public void submitDataToFirebase(View view)
    {
        Intent intent=new Intent(CompleteProfile.this,Timeline.class);
        startActivity(intent);
        finish();
    }
}
