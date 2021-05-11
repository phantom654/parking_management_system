package com.example.parkingmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingsAdapter extends  RecyclerView.Adapter<BookingsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Bookings> bookings;

    public BookingsAdapter(Context context, ArrayList bookings)
    {
        this.context=context;
        this.bookings=bookings;
    }

    @NonNull
    @Override
    public BookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_bookings_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookingsAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(bookings.get(position));

        Bookings booking = bookings.get(position);

        System.out.println(booking.getBuilding());

        holder.tvAllBookingsBuilding.setText("Building : "+booking.getBuilding());
        holder.tvAllBookingsCity.setText("City : "+booking.getCity());
        holder.tvAllBookingsTime.setText("Time : "+booking.getTime());
        holder.tvAllBookingsDuration.setText("Duration : "+booking.getDuration());
        holder.btnStatus.setText(booking.getStatus());

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CancelBooking(holder.btnCancel, booking.getInnoiceId()).execute();

            }
        });

        if(booking.getStatus().equals("future"))
        {
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnCancel.setText("CANCEL");
        }
        else
        {
            holder.btnCancel.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAllBookingsBuilding;
        TextView tvAllBookingsCity;
        TextView tvAllBookingsTime;
        TextView tvAllBookingsDuration;
        Button btnStatus;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            System.out.println("hello");

            this.tvAllBookingsBuilding = itemView.findViewById(R.id.tvAllBookingsBuilding);
            this.tvAllBookingsCity = itemView.findViewById(R.id.tvAllBookingsCity);
            this.tvAllBookingsTime = itemView.findViewById(R.id.tvAllBookingsTime);
            this.tvAllBookingsDuration = itemView.findViewById(R.id.tvAllBookingsDuration);
            this.btnStatus = itemView.findViewById(R.id.btnStatus);
            this.btnCancel = itemView.findViewById(R.id.btnCancel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bookings selectedBooking = (Bookings) v.getTag();

                    //call show invoice

                    Intent intent = new Intent(v.getContext(), ShowInvoiceActivity.class);

                    intent.putExtra("name", selectedBooking.getCity());
                    intent.putExtra("email", selectedBooking.getBuilding());
                    intent.putExtra("userId", selectedBooking.getUserId());
                    intent.putExtra("paymentId", selectedBooking.getInnoiceId());
                    intent.putExtra("parkingId", selectedBooking.getParkingId());
                    intent.putExtra("slotId", selectedBooking.getSlotId());
                    intent.putExtra("date", selectedBooking.getTime());
                    intent.putExtra("duration", selectedBooking.getDuration());
//                    intent.putExtra("pakringId", )

                    itemView.getContext().startActivity(intent);

                }
            });

        }
    }

    public class CancelBooking extends AsyncTask<Void, Void, Void>
    {
        Button btnCancel;
        String innoiceId;

        CancelBooking(Button btnCancel, String innoiceId)
        {
            this.btnCancel=btnCancel;
            this.innoiceId=innoiceId;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            btnCancel.setText("Cancelling ...");

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://parking.cxxwlprzsfrp.us-east-1.rds.amazonaws.com:3306/parking", "admin", "rajurand");
                Statement statementLogin = connection.createStatement();

                String queryCancel = String.format("DELETE FROM `parking`.`reservations` WHERE (`innoiceId` = '%s');", innoiceId);

                statementLogin.executeUpdate(queryCancel);




            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            btnCancel.setText("Cancelled");
        }
    }

}
