package com.example.nawazshariff.beta_two.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nawazshariff.beta_two.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nawazshariff on 10-10-2017.
 */

public class InfoPage extends AppCompatActivity implements View.OnClickListener {
    TextView umrahTv, hajTv, totalTv;
    EditText noOfAdulltsEt, noOfMinorsEt, boardingEt, arrivingEt;
    ImageButton boardingButton, arrivingButton;
    Button submit;
    private DatePickerFragment datePickerFragment;

    private static String TAG = "Info Page";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_page);
        initViews();
        calenderOnClicks();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(boardingEt.getText().toString()) || TextUtils.isEmpty(arrivingEt.getText().toString()) || TextUtils.isEmpty(noOfAdulltsEt.getText().toString()) || TextUtils.isEmpty(noOfMinorsEt.getText().toString())) {

                    Toast.makeText(InfoPage.this, "One of the Field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    String noOfAdults = noOfAdulltsEt.getText().toString();
                    String noOfMinors = noOfMinorsEt.getText().toString();

                    int total = Integer.parseInt(noOfAdults) + Integer.parseInt(noOfMinors);

                    totalTv.setText(""+total);
                    Intent intent = new Intent(InfoPage.this, Timeline.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void calenderOnClicks() {
        boardingButton.setOnClickListener(this);
        arrivingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.boarding_date_imageButton) {
            datePickerFragment.setFlag(DatePickerFragment.FLAG_BOARDING_DATE);
            datePickerFragment.show(getFragmentManager(), "Date Picker");
        } else if (id == R.id.arrival_date_imageButton) {
            datePickerFragment.setFlag(DatePickerFragment.FLAG_ARRIVING_DATE);
            datePickerFragment.show(getFragmentManager(), "Date Picker");
        }

    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public static final int FLAG_BOARDING_DATE = 0;
        public static final int FLAG_ARRIVING_DATE = 1;

        private int flag = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, this, year, month, day);
            return dpd;
        }

        public void setFlag(int i) {
            flag = i;
        }


        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style and locale
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String formattedDate = df.format(chosenDate);
            if (flag == FLAG_BOARDING_DATE) {
                EditText boardingDateEt = getActivity().findViewById(R.id.boarding_date_et);
                boardingDateEt.setText(formattedDate);
            } else if (flag == FLAG_ARRIVING_DATE) {
                EditText arrivingDateEt = getActivity().findViewById(R.id.arrival_date_et);
                arrivingDateEt.setText(formattedDate);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initViews() {
        umrahTv = (TextView) findViewById(R.id.umrah);
        hajTv = (TextView) findViewById(R.id.haj);
        totalTv = (TextView) findViewById(R.id.print_total_tv);
        boardingButton = (ImageButton) findViewById(R.id.boarding_date_imageButton);
        arrivingButton = (ImageButton) findViewById(R.id.arrival_date_imageButton);
        submit = (Button) findViewById(R.id.submit);
        noOfAdulltsEt = findViewById(R.id.adults_et);
        noOfMinorsEt = findViewById(R.id.minor_et);
        boardingEt = findViewById(R.id.boarding_date_et);
        arrivingEt = findViewById(R.id.arrival_date_et);
        datePickerFragment = new DatePickerFragment();
    }
}

