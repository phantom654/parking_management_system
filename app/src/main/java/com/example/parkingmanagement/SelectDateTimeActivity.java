package com.example.parkingmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class SelectDateTimeActivity extends AppCompatActivity {

    TextView tvDate, tvStartTime;
    EditText etDuration;
    Button btnDate, btnStartTime, btnDuration, btnCheckAvailability;

    int YEAR, MONTH, DATE, HOUR, MINUTE;

    String parkingId, userId;String city, building;
    int numberOfRows, numberOfColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_time);

        parkingId = getIntent().getStringExtra("parkingId");
        userId = getIntent().getStringExtra("userId");
        numberOfRows = getIntent().getIntExtra("numberOfRows", 0);
        numberOfColumns = getIntent().getIntExtra("numberOfColumns", 0);
        city = getIntent().getStringExtra("city");
        building=getIntent().getStringExtra("building");

        System.out.println(numberOfColumns * numberOfRows);

        tvDate = findViewById(R.id.tvDate);
        tvStartTime = findViewById(R.id.tvStartTime);

        etDuration = findViewById(R.id.etDuration);

        btnDate = findViewById(R.id.btnDate);
        btnStartTime = findViewById(R.id.btnStartTime);

        btnCheckAvailability = findViewById(R.id.btnCheckAvailability);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

        btnCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int duration = Integer.parseInt(etDuration.getText().toString());

                Intent intent = new Intent(getApplicationContext(), CheckAvailabilityActivity.class);

                intent.putExtra("YEAR", YEAR);
                intent.putExtra("MONTH", MONTH);
                intent.putExtra("DATE", DATE);
                intent.putExtra("HOUR", HOUR);
                intent.putExtra("MINUTE", MINUTE);
                intent.putExtra("DURATION", duration);
                intent.putExtra("userId", userId);
                intent.putExtra("parkingId", parkingId);
                intent.putExtra("numberOfRows", numberOfRows);
                intent.putExtra("numberOfColumns", numberOfColumns);
                intent.putExtra("city", city);
                intent.putExtra("building", building);
                startActivity(intent);


            }
        });

    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();
YEAR=year;
MONTH=month;
DATE=date;
                tvDate.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();


    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        HOUR = calendar.get(Calendar.HOUR);
        MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = hour+":"+minute;
                HOUR=hour;
                MINUTE=minute;
                tvStartTime.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }
}