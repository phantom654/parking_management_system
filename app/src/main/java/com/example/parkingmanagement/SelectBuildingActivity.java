package com.example.parkingmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SelectBuildingActivity extends AppCompatActivity {
    ProgressBar progressBarGetBuildings;

    ListView lvBuildings;
    String city, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_building);

        progressBarGetBuildings=findViewById(R.id.progressBarGetBuildings);
        lvBuildings=findViewById(R.id.lvBuildings);

        city = getIntent().getStringExtra("city");
        userId = getIntent().getStringExtra("userId");

        ArrayAdapter<String> buildingsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        lvBuildings.setAdapter(buildingsAdapter);


        new GetBuildings().execute();


    }

    class GetBuildings extends AsyncTask<Void, Void, ArrayList <Pair<Pair<String,String>,Pair<Integer, Integer>>>> {

        ArrayAdapter<String> buildingsAdapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buildingsAdapter= (ArrayAdapter<String>) lvBuildings.getAdapter();
            progressBarGetBuildings.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList <Pair<Pair<String,String>,Pair<Integer, Integer>>> doInBackground(Void... voids) {

            ArrayList <Pair<Pair<String,String>,Pair<Integer, Integer>>> buildings_id = new ArrayList<Pair<Pair<String,String>,Pair<Integer, Integer>>>();

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking", "admin", "rajurand");
                Statement statementLogin = connection.createStatement();

                String queryBuildings = String.format("select buildingName,parkingId,numberOfRows,numberOfColumns from parkingLot where cityName='%s'", city);
                ResultSet resultSetBuildings = statementLogin.executeQuery(queryBuildings);

                while(resultSetBuildings.next())
                {
                    String building = resultSetBuildings.getString(1);
                    String parkingId = resultSetBuildings.getString(2);
                    int numberOfRows = resultSetBuildings.getInt(3);
                    int numberOfColumns = resultSetBuildings.getInt(4);
                    buildings_id.add(new Pair(new Pair(building,parkingId), new Pair(numberOfRows, numberOfColumns)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return buildings_id;
        }

        @Override
        protected void onPostExecute(ArrayList <Pair<Pair<String,String>,Pair<Integer, Integer>>> strings) {
            super.onPostExecute(strings);
            progressBarGetBuildings.setVisibility(View.GONE);

            ArrayList <String> buildings = new ArrayList<String>();

            for(int i=0;i<strings.size();i++)buildings.add(strings.get(i).first.first);

            buildingsAdapter.addAll(buildings);

            lvBuildings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), SelectDateTimeActivity.class);

                    intent.putExtra("parkingId", strings.get(position).first.second);
                    intent.putExtra("userId", userId);
                    intent.putExtra("numberOfRows", strings.get(position).second.first);
                    intent.putExtra("numberOfColumns", strings.get(position).second.second);
                    intent.putExtra("city", city);
                    intent.putExtra("building", strings.get(position).first.first);

                    startActivity(intent);

                }
            });

        }
    }
}