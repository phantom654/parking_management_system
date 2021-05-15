package com.example.parkingmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AllBookingsActivity extends AppCompatActivity {

    RecyclerView rvAllBookings;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBarGetBookings;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bookings);

        rvAllBookings = findViewById(R.id.rvAllBookings);
        rvAllBookings.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvAllBookings.setLayoutManager(layoutManager);



        progressBarGetBookings = findViewById(R.id.progressBarGetBookings);

        userId = getIntent().getStringExtra("userId");

        System.out.println(userId);

        new GetBookings().execute();


    }

    class GetBookings extends AsyncTask<Void, Void, ArrayList<Bookings>>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarGetBookings.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Bookings> doInBackground(Void... voids) {

            ArrayList<Bookings> bookings=new ArrayList<>();
            String host = "jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6412050";
            String userName = "sql6412050";
            String password = "LD4RKuInVq";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(host,userName,password);
                Statement statementLogin = connection.createStatement();



                String queryCities = String.format(
                        "select `reservations`.`startDateTime`, `reservations`.`endDateTime`, `reservations`.`innoiceId`, `parkingLot`.`cityName`, `parkingLot`.`buildingName`, `parkingLot`.`parkingId`, `reservations`.`rowId`, `reservations`.`columnId`\n" +
                                "from `reservations`\n" +
                                "inner join `parkingLot`\n" +
                                "on `reservations`.`parkingId` = `parkingLot`.`parkingId`\n" +
                                "where `userId`=%s",userId);
                ResultSet resultSet = statementLogin.executeQuery(queryCities);

                Date currentDate = new Date();
                Long currentDateTime = currentDate.getTime();

//                System.out.println(Calendar.getInstance().getTime().toString());
                System.out.println(currentDateTime);

                while(resultSet.next())
                {

                    Date startDateTime = new Date(resultSet.getLong(1));
                    Date endDateTime = new Date(resultSet.getLong(2));
                    String innoiceID = resultSet.getString(3);
                    String cityName = resultSet.getString(4);
                    String buildingName = resultSet.getString(5);
                    String parkingId = resultSet.getString(6);
                    int rowId = resultSet.getInt(7);
                    int columnId = resultSet.getInt(8);

                    System.out.println(startDateTime);
                    System.out.println(currentDate);

                    String status;
                    if(startDateTime.getTime()>currentDateTime)
                    {
                        //future
                        status="future";
                    }
                    else if(startDateTime.getTime()<=currentDateTime && endDateTime.getTime()>=currentDateTime)
                    {
                        //active
                        status="active";
                    }
                    else
                    {
                        //past
                        status="past";
                    }
                    int duration = (int) ((endDateTime.getTime() - startDateTime.getTime())/60000);

                    bookings.add(new Bookings(buildingName, cityName, startDateTime.toString(), Integer.toString(duration)+" min", status, innoiceID, userId, parkingId, Integer.toString(rowId)+":"+Integer.toString(columnId)));

                }



            } catch (Exception e) {
                e.printStackTrace();
            }

            return bookings;
        }

        @Override
        protected void onPostExecute(ArrayList<Bookings> bookings) {
            super.onPostExecute(bookings);

            progressBarGetBookings.setVisibility(View.GONE);

            ArrayList<Bookings> tbookings = new ArrayList<>();

            for(int i=0;i<bookings.size();i++)
            {
                if(bookings.get(i).getStatus().equals("active"))tbookings.add(bookings.get(i));
            }
            for(int i=0;i<bookings.size();i++)
            {
                if(bookings.get(i).getStatus().equals("future"))tbookings.add(bookings.get(i));
            }
            for(int i=0;i<bookings.size();i++)
            {
                if(bookings.get(i).getStatus().equals("past"))tbookings.add(bookings.get(i));
            }

            mAdapter = new BookingsAdapter(getApplicationContext(), tbookings);

            rvAllBookings.setAdapter(mAdapter);

        }
    }
}