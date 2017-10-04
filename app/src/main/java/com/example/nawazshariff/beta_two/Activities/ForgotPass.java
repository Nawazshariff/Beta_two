package com.example.nawazshariff.beta_two.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nawazshariff.beta_two.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by nawazshariff on 16-09-2017.
 */

public class ForgotPass extends AppCompatActivity implements View.OnClickListener {
    EditText r_email;
    Button reset;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpass);
        r_email=findViewById(R.id.reset_email_et);
        reset=findViewById(R.id.reset_button);
        firebaseAuth = FirebaseAuth.getInstance();
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String emailAddress = r_email.getText().toString();
        if(TextUtils.isEmpty(emailAddress))
        {
            Toast.makeText(ForgotPass.this,"Please enter your email",Toast.LENGTH_SHORT).show();
        }
        else {

            firebaseAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Resent mail", "Email sent.");
                                Intent intent = new Intent(ForgotPass.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }
    }
}
