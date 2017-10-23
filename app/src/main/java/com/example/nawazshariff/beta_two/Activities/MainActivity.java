package com.example.nawazshariff.beta_two.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nawazshariff.beta_two.Constants;
import com.example.nawazshariff.beta_two.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText password, email;
    Button register;
    TextView login, forgotpass;
    String Pass, Email;
    private static String AUTH_EXCEPTION = "FireBaseAuthException";
    private static String TAG = "Main activity";
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private ProgressDialog progressDialog;
    Boolean signUpModeAcive = true;
    FirebaseAuth.AuthStateListener authStateListener;
    SharedPreferences mainActivityPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        mainActivityPref = getApplicationContext().getSharedPreferences(Constants.COMPLETE_PROFILE, MODE_PRIVATE);
        initViews();

        login.setOnClickListener(this);
        authenticationCheck();
        forgotPassword();


    }

    private void forgotPassword() {
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Toast.makeText(MainActivity.this,"clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ForgotPass.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void authenticationCheck() {
        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.e(TAG, "register : user signed in");
                    Intent intent = new Intent(MainActivity.this, Timeline.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, "register : user signed out");
                }
            }
        };
    }

    private void initViews() {
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        login = (TextView) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        forgotpass = (TextView) findViewById(R.id.forgotPass);
        progressDialog = new ProgressDialog(this);
        forgotpass.setVisibility(View.GONE);


    }


    public void registerUser() {
        //getting email and password from edit texts
        Email = email.getText().toString().trim();
        Pass = password.getText().toString().trim();
        //checking if email and passwords are empty
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Pass)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        //if the email and password are not empty
        //displaying a progress dialog
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        //creating a new user
        createUserInFireBase();

    }

    private void createUserInFireBase() {
        firebaseAuth.createUserWithEmailAndPassword(Email, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            //display some message here
                            Toast.makeText(MainActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                            sendEmailVerificationToUser();

                        } else {
                            //display some message here
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Log.e(AUTH_EXCEPTION, e.getMessage());
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Log.e(AUTH_EXCEPTION, e.getMessage());
                            } catch (FirebaseAuthUserCollisionException e) {
                                Log.e(AUTH_EXCEPTION, e.getMessage());
                            } catch (Exception e) {
                                Log.e(AUTH_EXCEPTION, e.getMessage());
                            }
                            Toast.makeText(MainActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login) {
            if (signUpModeAcive) {
                signUpModeAcive = false;
                login.setText("Register");
                register.setText("Login");
                forgotpass.setVisibility(View.VISIBLE);
            } else {
                signUpModeAcive = true;
                login.setText("Login");
                register.setText("Register");
                forgotpass.setVisibility(View.GONE);
            }

        }
    }

    public void clickToReg(View v) {
        if (signUpModeAcive) {
            registerUser();
        } else {
            //getting email and password from edit texts
            loginUser();

        }
    }

    private void loginUser() {
        Email = email.getText().toString().trim();
        Pass = password.getText().toString().trim();
        //checking if email and passwords are empty
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Pass)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Verifying.....");
        progressDialog.show();
        signInWithEmailAndPassword();

    }

    private void signInWithEmailAndPassword() {
        firebaseAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(MainActivity.this, "Login failed : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    checkIfEmailVerified();
                }
                progressDialog.cancel();
            }
        });
    }

    public void sendEmailVerificationToUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this,
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "Please click on the link sent to mail and login", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("email", "sendEmailVerification", task.getException());
                        Toast.makeText(MainActivity.this,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;

        if (user.isEmailVerified()) {
            // user is verified, so you can finish this activity or send user to activity which you want.

            Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            if (mainActivityPref.getBoolean("isProfileComplete", true)) {
                intent = new Intent(MainActivity.this, CompleteProfile.class);
            } else {
                intent = new Intent(MainActivity.this, InfoPage.class);
            }

            startActivity(intent);
            finish();
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(MainActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
