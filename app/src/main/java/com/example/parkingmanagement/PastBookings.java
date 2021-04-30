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


    String userId, name, address, password, contactNum, email, vehicleId, slot, parkingId, time, date, duration, paymentId;

    TextView tvPast;
    ListView lvPast;
    ProgressBar progressBarPast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
        }

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

            lvPast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), ShowInvoiceActivity.class);

                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("vehicleId", vehicleId);
                    intent.putExtra("paymentId", paymentId);
                    intent.putExtra("parkingId", parkingId);
                    intent.putExtra("slotId", slot);
                    intent.putExtra("userID", userId);
                    intent.putExtra("date", date);
                    intent.putExtra("duration",duration);

                    startActivity(intent);

                }
            });

        }
    }

}
