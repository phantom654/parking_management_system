package com.example.parkingmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class CheckAvailabilityActivity extends AppCompatActivity {

    int year, month, date, hour, minute, duration;
    String userId, parkingId;
    int numberOfRows;
    int numberOfColumns;

    ProgressBar progressBarCheck;
    GridLayout grid;
    Button btnBook;
    TextView tvHeading;

    GridLayout scrollGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_availability);

        progressBarCheck = findViewById(R.id.progressBarCheck);
        grid = findViewById(R.id.grid);
        //scrollGrid = findViewById(R.id.grid);
        btnBook = findViewById(R.id.btnBook);

        tvHeading = findViewById(R.id.tvHeading);

        year = getIntent().getIntExtra("YEAR", 0);
        month = getIntent().getIntExtra("MONTH", 0);
        date = getIntent().getIntExtra("DATE", 0);
        hour = getIntent().getIntExtra("hour", 0);
        minute = getIntent().getIntExtra("minute", 0);
        duration = getIntent().getIntExtra("DURATION", 0);
        numberOfRows = getIntent().getIntExtra("numberOfRows", 0);
        numberOfColumns = getIntent().getIntExtra("numberOfColumns", 0);

        userId = getIntent().getStringExtra("userId");
        parkingId = getIntent().getStringExtra("parkingId");


        new buildGrid().execute();

    }

    class buildGrid extends AsyncTask<Void, Void, ArrayList<Pair<Long, Long>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarCheck.setVisibility(View.VISIBLE);
            //scrollGrid.setVisibility(View.GONE);
            grid.setVisibility(View.GONE);
            btnBook.setVisibility(View.GONE);

        }

        @Override
        protected ArrayList<Pair<Long, Long>> doInBackground(Void... voids) {

            ArrayList<Pair<Long, Long>> reservations = new ArrayList<>();

            Date startDate = new Date(year, month, date, hour, minute);
            Date endDate = new Date(startDate.getTime() + duration * 60 * 1000);

            Long selectedStartTime = startDate.getTime();
            Long selectedEndTime = endDate.getTime();

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking", "admin", "rajurand");
                Statement statementLogin = connection.createStatement();

                String checkAvailability = String.format("select startDateTime,endDateTime,rowId,columnId from reservations where parkingId='%s'", parkingId);
                ResultSet resultCheckAvailability = statementLogin.executeQuery(checkAvailability);

                while (resultCheckAvailability.next()) {
                    Long startTime = resultCheckAvailability.getLong(1);
                    Long endTime = resultCheckAvailability.getLong(2);
                    int rowId = resultCheckAvailability.getInt(3);
                    int columnId = resultCheckAvailability.getInt(4);

                    if (Math.max(selectedStartTime, startTime) < Math.min(endTime, selectedEndTime)) {
                        reservations.add(new Pair(rowId, columnId));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return reservations;
        }

        @Override
        protected void onPostExecute(ArrayList<Pair<Long, Long>> reservations) {
            super.onPostExecute(reservations);

            progressBarCheck.setVisibility(View.GONE);
           // scrollGrid.setVisibility(View.VISIBLE);
//            btnBook.setVisibility(View.VISIBLE);

            grid.setVisibility(View.VISIBLE);

            grid.setRowCount(1000);//numberOfRows);
            grid.setColumnCount(4);//numberOfColumns);
            grid.setPadding(5, 5, 5, 5);

            final int[] selectedRow = new int[1];
            final int[] selectedColumn = new int[1];

            Button[][] slots = new Button[numberOfRows][numberOfColumns];
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    Button slot = new Button(getApplicationContext());
                    String ID = "X: " + i + " Y: " + j;
                    slot.setText(ID);
                    if (reservations.contains(new Pair(i, j))) {

                        slot.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.grid_view_red, null));
                        //slot.setBackgroundColor(Color.RED);
                        slot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast toastPasswordIncorrect = Toast.makeText(getApplicationContext(), "The slot is already occupied!", Toast.LENGTH_SHORT);
                                        toastPasswordIncorrect.show();
                                    }
                                });

//                                btnBook.setVisibility(View.VISIBLE);
//                                btnBook.setText("unavailable");

                            }
                        });
                    } else {

                        slot.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.grid_view_green, null));
                        //slot.setBackgroundColor(Color.GREEN);
                        String slotId = String.valueOf(i) + "," + String.valueOf(j);
                        int finalI = i;
                        int finalJ = j;
                        slot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                btnBook.setVisibility(View.VISIBLE);
//                                btnBook.setText("Book " + slotId);
//                                selectedRow[0] = finalI;
//                                selectedColumn[0] = finalJ;

                                    Intent intentPay = new Intent(getApplicationContext(), Payment.class);

                                    intentPay.putExtra("userId", userId);
                                    intentPay.putExtra("finalI", finalI);
                                    intentPay.putExtra("finalJ", finalJ);
                                    intentPay.putExtra("duration", duration);

                                    startActivity(intentPay);
                            }
                        });
                    }
//                    slot.setPadding(8, 8,8,8);
                   // slot.setText("X: " + );
                    grid.addView(slot);

                }
            }

            btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (btnBook.getText().toString().equals("unavailable")) {

                    } else {

                    }

                }
            });


        }
    }
}