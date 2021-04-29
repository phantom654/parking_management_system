package com.example.parkingmanagement;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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

public class UpdatePassword extends AppCompatActivity {

    String userId, password, currPass;
    String newPass = null, retypePass = null;

    Button btnUpdate;
    TextView tvUserId;
    ProgressBar progressBarUpdate;

    Connection connection = null;
    Statement statementGet = null, statementUpdate = null;
    ResultSet resultSet = null;

    EditText etNewPass, etRetypePass, etCurrPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
        }

        setContentView(R.layout.activity_updatepassword);

        etNewPass = findViewById(R.id.etNewPass);
        etRetypePass = findViewById(R.id.etRetypePass);
        etCurrPass = findViewById(R.id.etCurrPassChange);

        btnUpdate = findViewById(R.id.btnUpdatePass);
        tvUserId = findViewById(R.id.tvUserId);
        progressBarUpdate = findViewById(R.id.progressBarUpdate);

        tvUserId.setText(String.format("User ID : %s", userId));

        new GetAsync().execute();
    }

    class GetAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarUpdate.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Class.forName("com.mysql.jdbc.Driver");

                connection = DriverManager.getConnection("jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking?useSSL=false", "admin", "rajurand");
                statementGet = connection.createStatement();

                String queryUserExists = String.format("select * from users WHERE userId='%s'", userId);
                resultSet = statementGet.executeQuery(queryUserExists);

                resultSet.first();
                password = resultSet.getString(3);

                connection.close();
                statementGet.close();
                resultSet.close();

                btnUpdate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        newPass = etNewPass.getText().toString();
                        retypePass = etRetypePass.getText().toString();
                        currPass = etCurrPass.getText().toString();

                        if (TextUtils.isEmpty(newPass) || TextUtils.isEmpty(retypePass)) {

                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast toastNullNewPass = Toast.makeText(getApplicationContext(), "New Password fields cannot be empty!", Toast.LENGTH_SHORT);
                                    toastNullNewPass.show();
                                }
                            });

                        } else if (TextUtils.isEmpty(password)) {

                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast toastNullCurrPass = Toast.makeText(getApplicationContext(), "Enter current password!", Toast.LENGTH_SHORT);
                                    toastNullCurrPass.show();
                                }
                            });

                        } else if (!newPass.equals(retypePass)) {

                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast toastNotEqual = Toast.makeText(getApplicationContext(), "Enter same password in New Password Fields!", Toast.LENGTH_SHORT);
                                    toastNotEqual.show();
                                }
                            });

                        } else {

                            if (!currPass.equals(password)) {

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast toastPasswordIncorrect = Toast.makeText(getApplicationContext(), "Incorrect Password!", Toast.LENGTH_SHORT);
                                        toastPasswordIncorrect.show();
                                    }
                                });
                            } else {
                                new UpdateAsync().execute();
                            }
                        }
                    }

                });


            } catch (Exception e) {
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

    class UpdateAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarUpdate.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                connection = DriverManager.getConnection("jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking?useSSL=false", "admin", "rajurand");

                statementUpdate = connection.createStatement();

                String queryUpdatePassword = String.format("UPDATE users " +
                                "set password = '%s'" +
                                "WHERE userId = '%s';",
                        newPass, userId);

                int result = statementUpdate.executeUpdate(queryUpdatePassword);

                runOnUiThread(new Runnable() {
                    public void run() {
                        if (result == 0) {
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

                connection.close();
                statementUpdate.close();


            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
