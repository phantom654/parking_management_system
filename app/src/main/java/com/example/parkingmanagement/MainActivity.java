package com.example.parkingmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        if(sharedPreferences.getBoolean("loggedIn", false)){
            String userId = sharedPreferences.getString("userId", "null");
            Intent intentHome = new Intent(getApplicationContext(), HomeActivity.class);

            intentHome.putExtra("userId", userId);
            startActivity(intentHome);
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }



    }
}