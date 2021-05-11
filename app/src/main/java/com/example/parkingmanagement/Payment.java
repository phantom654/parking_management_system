package com.example.parkingmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.AsynchronousByteChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Payment extends Activity implements PaymentResultListener {

    Button btnPay;
    TextView tvAmountToPay, tvSlot, tvSelectedDate, tvDuration;

    String userId, name, address, password, contactNum, email, vehicleId, slot, parkingId, time, date, duration;

    String selectedStartTime, selectedEndTime;

    String companyName = "PSR Pvt. Ltd.";

    String city, building;

    ProgressBar progressbar;

    int finalI, finalJ, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Error"+Thread.currentThread().getStackTrace()[2],paramThrowable.getLocalizedMessage());
            }
        });

        setContentView(R.layout.activity_payment);



        btnPay = findViewById(R.id.btnPay);
        tvAmountToPay = findViewById(R.id.tvAmountToPay);
        tvSlot = findViewById(R.id.tvSlot);
        progressbar = findViewById(R.id.progressBarPay);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvDuration = findViewById(R.id.tvDuration);

        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
        }

        System.out.println(userId);

        amount = getIntent().getIntExtra("duration", 100);
        tvAmountToPay.setText("Amount to Pay : " + Integer.toString(amount));

        amount = Math.round(Float.parseFloat(String.valueOf(amount))*100);

        finalI = getIntent().getIntExtra("finalI", 0);
        finalJ = getIntent().getIntExtra("finalJ", 0);
        parkingId=getIntent().getStringExtra("parkingId");
        selectedStartTime=getIntent().getStringExtra("selectedStartTime");
        selectedEndTime=getIntent().getStringExtra("selectedEndTime");
        city = getIntent().getStringExtra("city");
        building = getIntent().getStringExtra("building");

        System.out.println(selectedStartTime);

        duration=Integer.toString(getIntent().getIntExtra("duration", 0));
        date=getIntent().getStringExtra("date");

        System.out.println(date);

        tvDuration.setText("Duration\t: "+duration);
        tvSelectedDate.setText("Date\t:"+date);



//        System.out.println(amount);
//        System.out.println(finalI);
//        System.out.println(finalJ);

        slot = "Selected slot : (" + finalI + ", " + finalJ + ")";


        tvSlot.setText(slot);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PayAsync().execute();
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Processing Payment");

        builder.setMessage(s);

        builder.show();


        String params[] = {s};
        new EnterIntoDatabase().execute(params);

    }




    class EnterIntoDatabase extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            btnPay.setText("Processing Payment, Please Wait");
            btnPay.setEnabled(false);

        }

        @Override
        protected Void doInBackground(String... params) {

            String paymentId = params[0];

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking", "admin", "rajurand");
                Statement statementLogin = connection.createStatement();

                String enterRegistration = String.format("INSERT INTO `reservations` (`parkingId`, `startDateTime`, `endDateTime`, `userId`, `innoiceId`, `rowId`, `columnId`) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s');", parkingId, selectedStartTime, selectedEndTime, userId, paymentId, Integer.toString(finalI), Integer.toString(finalJ) );
                int resultCheckAvailability = statementLogin.executeUpdate(enterRegistration);

                Intent intent = new Intent(getApplicationContext(), ShowInvoiceActivity.class);

                intent.putExtra("name", city);
                intent.putExtra("email", building);
                intent.putExtra("vehicleId", vehicleId);
                intent.putExtra("paymentId", paymentId);
                intent.putExtra("parkingId", parkingId);
                intent.putExtra("slotId", slot);
                intent.putExtra("userId", userId);
                intent.putExtra("date", date);
                intent.putExtra("duration",duration);


                System.out.println("userID : "+userId);
//                Toast.makeText(getApplicationContext(), "Payment Successfull", Toast.LENGTH_SHORT).show();



                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            btnPay.setText("Pay");

            btnPay.setEnabled(true);
        }
    }


    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();


    }

    class PayAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressbar.setVisibility(View.VISIBLE);
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

//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        // Stuff that updates the UI
//                        etName.setText(name);
//                        etEmail.setText(email);
//                        etAddress.setText(address);
//                        etContactNum.setText(contactNum);
//                        etVehicleId.setText(vehicleId);
//                    }
//                });


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

            Checkout checkout = new Checkout();

            checkout.setKeyID("rzp_test_G3SP6nMsC777Aa");

            checkout.setImage(R.drawable.logo);

            JSONObject object = new JSONObject();

            try {
                object.put("name", companyName);

                object.put("description", slot);

                object.put("theme.color", "#0093DD");

                object.put("currency", "INR");

                object.put("amount", amount);

                object.put("prefill.contact", contactNum);

                object.put("prefill.email", email);

                object.put("send_sms_hash", false);

                checkout.open(Payment.this, object);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressbar.setVisibility(View.INVISIBLE);
        }
    }
}
