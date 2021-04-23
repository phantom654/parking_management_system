package com.example.parkingmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;

    ProgressBar progressBarLogin;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);


        if(sharedPreferences.getBoolean("loggedIn", false)){
            String userId = sharedPreferences.getString("userId", "null");
            Intent intentHome = new Intent(getApplicationContext(), HomeActivity.class);

            intentHome.putExtra("userId", userId);
            startActivity(intentHome);
        }
        else
        {
            etEmail=findViewById(R.id.etEmail);
            etPassword=findViewById(R.id.etPassword);

            btnLogin=findViewById(R.id.btnLogin);

            progressBarLogin=findViewById(R.id.progressBarLogin);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String[] params = {etEmail.getText().toString(), etPassword.getText().toString()};

                    new Login().execute(params);

                }
            });

        }
    }

    class Login extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarLogin.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {

            String email = strings[0], password = strings[1];

            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection( "jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking","admin","rajurand");
                Statement statementLogin = connection.createStatement();

                String queryUserExists = String.format("select * from users WHERE email='%s'",email);
                ResultSet resultSetUserExists = statementLogin.executeQuery(queryUserExists);

                if(resultSetUserExists.next()==false)
                {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast tostUserNotFound = Toast.makeText(getApplicationContext(), "User not found!", Toast.LENGTH_SHORT);
                            tostUserNotFound.show();
                        }
                    });

                }
                else
                {
//                    System.out.println(resultSetUserExists.getString((3)));
//                    System.out.println(password);
                    if(!resultSetUserExists.getString(3).equals(password))
                    {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast tostPasswordIncorrect = Toast.makeText(getApplicationContext(), "Incorrect Password!", Toast.LENGTH_SHORT);
                                tostPasswordIncorrect.show();
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast tostUserFound = Toast.makeText(getApplicationContext(), "User found!", Toast.LENGTH_SHORT);
                                tostUserFound.show();
                            }
                        });

                        String userId=resultSetUserExists.getString(1);
                        sharedPreferences.edit().putBoolean("loggedIn",true).apply();
                        sharedPreferences.edit().putString("userId",userId).apply();

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBarLogin.setVisibility(View.INVISIBLE);
        }
    }
}