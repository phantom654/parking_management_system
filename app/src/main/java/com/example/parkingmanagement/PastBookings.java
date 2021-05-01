package com.example.parkingmanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PastBookings extends AppCompatActivity {


    String userId, name, email, vehicleId;

    TextView tvPast;
    ListView lvPast;
    ProgressBar progressBarPast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        userId = getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        vehicleId = getIntent().getStringExtra("vehicleId");

        setContentView(R.layout.activity_pastbookings);

        tvPast = findViewById(R.id.tvPast);
        lvPast = findViewById(R.id.lvPast);
        progressBarPast = findViewById(R.id.progressBarPast);

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        lvPast.setAdapter(citiesAdapter);

        new GetPast().execute();

    }

    class GetPast extends AsyncTask<Void, Void, ArrayList<String>>{

        ArrayAdapter<String> citiesAdapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            citiesAdapter= (ArrayAdapter<String>) lvPast.getAdapter();
            progressBarPast.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            ArrayList <String> pastBookings = new ArrayList<String>();

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking", "admin", "rajurand");
                Statement statementLogin = connection.createStatement();

                String queryCities = String.format("select startDateTime,endDateTime from reservations where userId = '%s'", userId);
                ResultSet resultSet = statementLogin.executeQuery(queryCities);

                Date tempCurrentDateTime = Calendar.getInstance().getTime();
                Long currentDateTime = tempCurrentDateTime.getTime();
                while(resultSet.next())
                {
                    Long startDateTime = resultSet.getLong(1);
                    Long endDateTime = resultSet.getLong(2);

                    if(currentDateTime > endDateTime)
                    pastBookings.add("Start : " + startDateTime + ", End: " + endDateTime);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return pastBookings;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            progressBarPast.setVisibility(View.GONE);

            citiesAdapter.addAll(strings);

        }
    }

}
