package com.example.parkingmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowInvoiceActivity extends AppCompatActivity {

    String name, email, vehicleId, slotId, paymentId, parkingId, userId, date, duration, city, building;

    TextView tvName, tvEmail, tvVehicleId, tvSlotId, tvPaymentId, tvInvoiceDate, tvInvoiceDuration;

    Button btnGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invoice);

        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        vehicleId=getIntent().getStringExtra("vehicleId");
        slotId=getIntent().getStringExtra("slotId");
        paymentId=getIntent().getStringExtra("paymentId");
        parkingId=getIntent().getStringExtra("parkingId");
        userId= getIntent().getStringExtra("userId");
        date=getIntent().getStringExtra("date");
        duration=getIntent().getStringExtra("duration");

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvVehicleId = findViewById(R.id.tvVehicleId);
        tvSlotId = findViewById(R.id.tvSlotId);
        tvPaymentId = findViewById(R.id.tvPaymentId);
        tvInvoiceDate = findViewById(R.id.tvInvoiceDate);
        tvInvoiceDuration = findViewById(R.id.tvInvoiceDuration);

        btnGoHome = findViewById(R.id.btnGoHome);

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userId", userId);

                startActivity(intent);

            }
        });

        tvName.setText("City\t: "+name);
        tvEmail.setText("Building\t: "+email);
        tvVehicleId.setText("UserId\t: "+userId);
        tvPaymentId.setText("PaymentId\t: "+paymentId);
        tvSlotId.setText("Parking\t: "+parkingId+", Slot: "+slotId);
        tvInvoiceDate.setText("Date\t: "+date);
        tvInvoiceDuration.setText("Duration\t: "+duration);



    }
}