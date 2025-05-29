package com.example.cw_comp1786.db.firebase;

import com.example.cw_comp1786.db.entity.Booking;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingFirebase extends FirebaseHelper{

    public void updateBooking(Booking booking, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings")
                .child(String.valueOf(booking.getBookingId()));
        bookingRef.setValue(booking)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
}
