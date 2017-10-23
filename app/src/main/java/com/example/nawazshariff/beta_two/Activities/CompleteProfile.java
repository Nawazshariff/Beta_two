package com.example.nawazshariff.beta_two.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nawazshariff.beta_two.Constants;
import com.example.nawazshariff.beta_two.Model.User;
import com.example.nawazshariff.beta_two.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nawazshariff on 06-10-2017.
 */

public class CompleteProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SharedPreferences completeProfilePref;
    EditText name, contactNumber, address;
    Toolbar completeProfileToolBar;
    ImageView idProof;
    Button browseButton, submitDataButton;
    Spinner spinner;
    Uri selectedImage;
    String item, email, encodedEmail;
    String uName, uNumber, uAddress;
    private static String TAG = "Complete Profile";
    private static int RESULT_LOAD_IMAGE = 1;
    DatabaseReference completeProfileRef, CurrentUserRef;
    FirebaseUser CurrentUser;
    StorageReference completeProfileStorage, imageRef;
    ProgressDialog progressDialogForSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_profile);
        retrieveReferences();
        setSharedPreferences();
        initViews();



        //call for listening to click on spinner items
        spinner.setOnItemSelectedListener(this);

        //set Spinner for selecting type of id proof
        setSpinnerForIdProof();

    }

    private void retrieveReferences() {
        completeProfileRef = FirebaseDatabase.getInstance().getReference();
        //reference for Firebase Recycler View
        CurrentUserRef = completeProfileRef.child("users").getRef();

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        email = CurrentUser.getEmail();
        encodedEmail = EncodeEmail(email);

        //reference for storing profile details on firebase
        completeProfileStorage = FirebaseStorage.getInstance().getReference();
    }

    private static String EncodeEmail(String email) {
        return email.replace(".", ",");
    }

    private void setSpinnerForIdProof() {
        //spinner drop down elements
        List<String> items = new ArrayList<String>();
        items.add("Select ID Proof");
        items.add("Adhar Card");
        items.add("Voted Id");

        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


    }

    private void initViews() {
        name = (EditText) findViewById(R.id.complete_profile_name);
        contactNumber = (EditText) findViewById(R.id.complete_profile_number);
        address = (EditText) findViewById(R.id.complete_profile_addresss);
        idProof = (ImageView) findViewById(R.id.id_proof_image);
        completeProfileToolBar = (Toolbar) findViewById(R.id.complete_profile_toolbar);
        browseButton = (Button) findViewById(R.id.browse);
        submitDataButton = (Button) findViewById(R.id.submit);
        spinner = (Spinner) findViewById(R.id.spinner);

        completeProfileToolBar.setTitle("Complete Your Profile");
        setSupportActionBar(completeProfileToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setSharedPreferences() {
        completeProfilePref = getApplicationContext().getSharedPreferences(Constants.COMPLETE_PROFILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = completeProfilePref.edit();
        editor.putBoolean("isProfileComplete", false);
        editor.apply();
    }

    public void browseGallery(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void submitOnClick(View view) {


        if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(address.getText().toString()) || TextUtils.isEmpty(contactNumber.getText().toString()) || selectedImage == null ||item=="Select ID Proof") {
            Toast.makeText(CompleteProfile.this, "One of the field is empty", Toast.LENGTH_SHORT).show();
        } else {
            uName = name.getText().toString();
            uNumber = contactNumber.getText().toString();
            uAddress = address.getText().toString();
            submitDataToFireBase(uName, uNumber, uAddress, selectedImage);
        }


    }

    private void submitDataToFireBase(String uName, String uNumber, String uAddress, Uri image) {
        if (CurrentUser != null) {

            User user = new User(uAddress, uNumber, uName, item);
            CurrentUserRef.child(encodedEmail).setValue(user);
            imageRef = completeProfileStorage.child("images").child(email + "images.jpg");
            upLoadImageToFireBase(image);
            Toast.makeText(getApplicationContext(), "Data Uploaded ", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CompleteProfile.this, InfoPage.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(CompleteProfile.this, "unable to upload data", Toast.LENGTH_SHORT).show();
        }
    }

    private void upLoadImageToFireBase(Uri image) {
        imageRef.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Error uploading image", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //double progress=(100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = null;
            try {
                bitmap = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                Log.d(TAG, "error in getting bitmap from uri" + e.getMessage());
            }
            idProof.setImageBitmap(bitmap);

        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
        if(!item.contains("Select ID Proof"))
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }
}

