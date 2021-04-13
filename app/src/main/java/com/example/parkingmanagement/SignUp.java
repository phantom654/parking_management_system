package com.example.parkingmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    TextInputEditText textInputEditTextFullName, textInputEditTextUserName, textInputEditTextPassword, textInputEditTextEmail;
    Button buttonSignUp;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textInputEditTextFullName = findViewById(R.id.textInputLayoutFullname);
        textInputEditTextUserName = findViewById(R.id.textInputLayoutUsername);
        textInputEditTextPassword = findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail = findViewById(R.id.textInputLayoutEmail);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName = String.valueOf(textInputEditTextFullName.getText());
                String userName = String.valueOf(textInputEditTextUserName.getText());
                String password = String.valueOf(textInputEditTextPassword.getText());
                String email = String.valueOf(textInputEditTextEmail.getText());

                if(fullName.isEmpty() || userName.isEmpty() || password.isEmpty() || email.isEmpty())
                {


                }
                else
                {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }



            }
        });



    }
}