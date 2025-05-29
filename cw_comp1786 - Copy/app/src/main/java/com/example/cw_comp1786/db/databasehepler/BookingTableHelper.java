package com.example.cw_comp1786.db.databasehepler;

import static com.example.cw_comp1786.db.entity.Booking.TABLE_BOOKING;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cw_comp1786.db.DatabaseHelper;
import com.example.cw_comp1786.db.entity.Booking;
import com.example.cw_comp1786.db.firebase.BookingFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingTableHelper {
    private SQLiteDatabase sqLiteDatabase;
    private final DatabaseHelper databaseHelper;
    private BookingFirebase bookingFirebase;

    private static final String CREATE_BOOKING_TABLE =
            "CREATE TABLE " + TABLE_BOOKING + " ("
                    + Booking.COLUMN_BOOKING_ID + " TEXT PRIMARY KEY , "
                    + Booking.COLUMN_USER_ID + " INTEGER NOT NULL, "
                    + Booking.COLUMN_INSTANCE_ID + " INTEGER NOT NULL, "
                    + Booking.COLUMN_STATUS + " TEXT NOT NULL, "
                    + Booking.COLUMN_CONTACT_EMAIL + " TEXT NOT NULL, "
                    + "FOREIGN KEY(" + Booking.COLUMN_INSTANCE_ID + ") REFERENCES ClassInstance(session_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY(" + Booking.COLUMN_USER_ID + ") REFERENCES User(user_id) ON DELETE SET NULL);";

    public BookingTableHelper(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
        this.bookingFirebase = new BookingFirebase();
    }

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOKING_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);
        onCreate(sqLiteDatabase);
    }

    public void updateBookingFirebase(Booking booking, String newStatus, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        int result = updateBookingStatus(booking, newStatus);
        if (result > 0) {
            bookingFirebase.updateBooking(booking, onSuccessListener, onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Failed to update class status in SQLite"));
        }
    }

    public int updateBookingStatus(Booking booking, String newStatus) {
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Booking.COLUMN_STATUS, newStatus);
        int result = sqLiteDatabase.update(TABLE_BOOKING, values,
                Booking.COLUMN_BOOKING_ID + " = ?",
                new String[]{String.valueOf(booking.getBookingId())});
        return result;
    }


    public List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKING;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setBookingId(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_BOOKING_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_USER_ID)));
                booking.setInstanceId(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_INSTANCE_ID)));
                booking.setContactEmail(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_CONTACT_EMAIL)));
                booking.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_STATUS)));
                bookingList.add(booking);
            } while (cursor.moveToNext());
            cursor.close();
        }
        sqLiteDatabase.close();
        return bookingList;
    }


    public void syncFromFirebaseToSQLite() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bookings");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        Booking booking = snapshot.getValue(Booking.class);
                        if (booking != null) {
                            insertOrUpdateBooking(booking); // Thêm hoặc cập nhật vào SQLite
                            Log.d("Booking", "Fetched booking: " + booking.toString());
                        } else {
                            Log.e("Booking", "Fetched booking is null");
                        }
                    } catch (DatabaseException e) {
                        Log.e("Booking", "Error converting Firebase data", e);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Booking", "Error while fetching data from Firebase", databaseError.toException());
            }
        });
    }


    public void insertOrUpdateBooking(Booking booking) {
        if (booking == null || booking.getBookingId() == "") {
            Log.e("Booking", "Invalid booking data: booking or booking ID is null");
            return;
        }

        if (booking.getContactEmail() == null) {
            Log.e("Booking", "Booking contact email is null for booking ID: " + booking.getBookingId());
            return;
        }

        sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Booking.COLUMN_BOOKING_ID, booking.getBookingId());
        values.put(Booking.COLUMN_INSTANCE_ID, booking.getInstanceId());
        values.put(Booking.COLUMN_USER_ID, booking.getUserId());
        values.put(Booking.COLUMN_CONTACT_EMAIL, booking.getContactEmail());
        values.put(Booking.COLUMN_STATUS, booking.getStatus());

        int rows = sqLiteDatabase.update(TABLE_BOOKING, values,
                Booking.COLUMN_BOOKING_ID + " = ?",
                new String[]{String.valueOf(booking.getBookingId())});
        if (rows == 0) {
            long result = sqLiteDatabase.insert(TABLE_BOOKING, null, values);
            if (result == -1) {
                Log.e("Booking", "Error inserting booking into SQLite");
            } else {
                Log.d("Booking", "New booking inserted with ID: " + result);
            }
        } else {
            Log.d("Booking", "Booking updated with ID: " + booking.getBookingId());
        }
        sqLiteDatabase.close();
    }
}

