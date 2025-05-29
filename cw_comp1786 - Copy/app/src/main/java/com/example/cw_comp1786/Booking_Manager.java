package com.example.cw_comp1786;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw_comp1786.adapter.BookingAdapter;
import com.example.cw_comp1786.db.databasehepler.BookingTableHelper;
import com.example.cw_comp1786.db.entity.Booking;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


public class Booking_Manager extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private BookingTableHelper bookingTableHelper;
    private List<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_manager);

        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookingTableHelper = new BookingTableHelper(this);
        bookingTableHelper.syncFromFirebaseToSQLite();
        bookingList = bookingTableHelper.getAllBookings();

        bookingAdapter = new BookingAdapter(bookingList);
        recyclerView.setAdapter(bookingAdapter);

        bookingAdapter.setOnItemClickListener(new BookingAdapter.OnBookingClickListener() {
            @Override
            public void onAcceptClick(Booking booking, int position) {
                updateBookingStatus(booking, "Accepted", position);
            }

            @Override
            public void onCancelClick(Booking booking, int position) {
                updateBookingStatus(booking, "Cancelled", position);
            }
        });
    }

    private void updateBookingStatus(Booking booking, String newStatus, int position) {
        bookingTableHelper.updateBookingStatus(booking, newStatus);
        bookingTableHelper.updateBookingFirebase(
                booking, newStatus,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        booking.setStatus(newStatus);
                        bookingList.set(position, booking);
                        bookingAdapter.notifyItemChanged(position);
                        Toast.makeText(Booking_Manager.this, "Booking " + newStatus.toLowerCase(), Toast.LENGTH_SHORT).show();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Booking_Manager.this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}