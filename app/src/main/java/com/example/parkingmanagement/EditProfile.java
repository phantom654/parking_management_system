package com.example.parkingmanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class EditProfile extends AppCompatActivity {

    EditText etName, etEmail, etAddress, etContactNum, etCurrPass, etVehicleId;
    Button btnUpdate;

    TextView tvUserId;

    ProgressBar progressBarUpdate;

//    SharedPreferences sharedPreferences;

    String userId;

    String name, email, address, contactNum, currPass, vehicleId;

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

        tvUserId = findViewById(R.id.tvUserId);

        tvUserId.setText(String.format("User ID : %s", userId));
        etName.setText(userId);

        progressBarUpdate = findViewById(R.id.progressBarUpdate);

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection( "jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking","admin","rajurand");
            Statement statementSetVal = connection.createStatement();

            String queryUserExists = String.format("select * from users WHERE userId='%s'", userId);
            ResultSet resultSet = statementSetVal.executeQuery(queryUserExists);

//            etName.setText(resultSet.getString(2));

            etEmail.setText(resultSet.getString(7));
            etAddress.setText(resultSet.getString(5));
            etContactNum.setText(resultSet.getString(4));
            etVehicleId.setText(resultSet.getString(6));

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
