package com.example.parkingmanagement;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditProfile extends AppCompatActivity {

    EditText etName, etEmail, etAddress, etContactNum, etCurrPass, etVehicleId;
    Button btnUpdate, btnChangePass;

    TextView tvUserId;

    ProgressBar progressBarUpdate;

    String userId;

    String name, email, address, contactNum, currPass, vehicleId, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
        }

        setContentView(R.layout.activity_editprofile);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etContactNum = findViewById(R.id.etContactNum);
        etCurrPass = findViewById(R.id.etCurrPass);
        etVehicleId = findViewById(R.id.etVehicleId);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnChangePass = findViewById(R.id.btnChangePass);

        tvUserId = findViewById(R.id.tvUserId);
        tvUserId.setText(String.format("User ID : %s", userId));

        progressBarUpdate = findViewById(R.id.progressBarUpdate);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentChangePass = new Intent(getApplicationContext(), UpdatePassword.class);
                intentChangePass.putExtra("userId", userId);

                startActivity(intentChangePass);
            }
        });

        new GetAsync().execute();

        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new SetAsync().execute();
            }
        });

    }

    class GetAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarUpdate.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... voids) {

            Connection connection = null;
            Statement statementGetVal = null;
            ResultSet resultSetGetVal = null;

            try {

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection( "jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking?useSSL=false","admin","rajurand");
                statementGetVal = connection.createStatement();

                String queryGetVal = String.format("select * from users WHERE userId = '%s';", userId);
                resultSetGetVal = statementGetVal.executeQuery(queryGetVal);

                while(resultSetGetVal.next()) {
                    name = resultSetGetVal.getString("name");
                    email = resultSetGetVal.getString("email");
                    address = resultSetGetVal.getString("address");
                    contactNum = resultSetGetVal.getString("contactNumber");
                    vehicleId = resultSetGetVal.getString("vehicleId");
                    password = resultSetGetVal.getString(3);
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        etName.setText(name);
                        etEmail.setText(email);
                        etAddress.setText(address);
                        etContactNum.setText(contactNum);
                        etVehicleId.setText(vehicleId);
                    }
                });


                try {

                    resultSetGetVal.close();
                    statementGetVal.close();
                    connection.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            } catch (Exception e) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toastPasswordIncorrect = Toast.makeText(getApplicationContext(), "GetAsync Exception!", Toast.LENGTH_SHORT);
                        toastPasswordIncorrect.show();
                    }
                });


                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBarUpdate.setVisibility(View.INVISIBLE);
        }
    }

    class SetAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarUpdate.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            name = etName.getText().toString();
            email = etEmail.getText().toString();
            address = etAddress.getText().toString();
            contactNum = etContactNum.getText().toString();
            vehicleId = etVehicleId.getText().toString();
            currPass = etCurrPass.getText().toString();

            if(!currPass.equals(password)) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toastPasswordIncorrect = Toast.makeText(getApplicationContext(), "Incorrect Password!", Toast.LENGTH_SHORT);
                        toastPasswordIncorrect.show();
                    }
                });

            } else {

                Connection connection1 = null;
                Statement statementSetVal = null;
                int resultSetVal;

                try {

                    connection1 = DriverManager.getConnection( "jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking","admin","rajurand");
                    statementSetVal = connection1.createStatement();
                    String querySetVal = String.format("UPDATE users " +
                                    "set name = '%s', contactNumber = '%s', address = '%s', vehicleId = '%s', email = '%s' " +
                                    "WHERE userId = '%s';",
                            name, contactNum, address, vehicleId, email, userId);

                    resultSetVal = statementSetVal.executeUpdate(querySetVal);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(resultSetVal == 0) {
                                Toast toastFailure = Toast.makeText(getApplicationContext(), "Something Went Wrong! Please Try Again.", Toast.LENGTH_SHORT);
                                toastFailure.show();
                            } else {
                                Toast toastSuccess = Toast.makeText(getApplicationContext(), "Successfully Updated!", Toast.LENGTH_SHORT);
                                toastSuccess.show();
                            }
                        }
                    });

                    runOnUiThread(new Runnable() {
                        public void run() {

                            etCurrPass.setText(null);
                        }
                    });

                    try {
                        statementSetVal.close();

                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toastPasswordIncorrect = Toast.makeText(getApplicationContext(), "SetAsync Exception!", Toast.LENGTH_SHORT);
                            toastPasswordIncorrect.show();
                        }
                    });
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBarUpdate.setVisibility(View.INVISIBLE);
        }
    }

}


