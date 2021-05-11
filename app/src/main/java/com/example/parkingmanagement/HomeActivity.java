package com.example.parkingmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnUserProfile = findViewById(R.id.btnUserProfile);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 10);
        }

        Button btnNewBooking = findViewById(R.id.btnNewBooking);

        String userId = getIntent().getStringExtra("userId");

        System.out.println(userId);

        btnNewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectCityActivity.class);

                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });


        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUserProfile = new Intent(getApplicationContext(), UserProfile.class);
                intentUserProfile.putExtra("userId", userId);

                startActivity(intentUserProfile);
            }
        });

        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("loggedIn", false).apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);

            }
        });

    }

    class GetCities extends AsyncTask<Void, Void, List<String>>{

        @Override
        protected List<String> doInBackground(Void... voids) {

            List <String> cities = new ArrayList<String>();

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking", "admin", "rajurand");
                Statement statementLogin = connection.createStatement();

                String queryCities = String.format("select distinct(cityName) from parkingLot");
                ResultSet resultSetCities = statementLogin.executeQuery(queryCities);

                while(resultSetCities.next())
                {
                    String city = resultSetCities.getString(1);
                    cities.add(city);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return cities;
        }
    }
}