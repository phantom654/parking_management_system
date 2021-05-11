package com.example.parkingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {

    Button btnPastBookings, btnEditProfile;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
        }

        setContentView(R.layout.activity_userprofile);

        btnPastBookings = findViewById(R.id.btnUpdate);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        btnPastBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPastBookings = new Intent(getApplicationContext(), AllBookingsActivity.class);
                intentPastBookings.putExtra("userId", userId);

                startActivity(intentPastBookings);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEditProfile = new Intent(getApplicationContext(), EditProfile.class);
                intentEditProfile.putExtra("userId", userId);

                startActivity(intentEditProfile);
            }
        });



    }


}
