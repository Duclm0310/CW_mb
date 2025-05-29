package com.example.cw_comp1786;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cw_comp1786.adapter.UserAdapter;
import com.example.cw_comp1786.adapter.YogaclassAdapter;
import com.example.cw_comp1786.db.databasehepler.BookingTableHelper;
import com.example.cw_comp1786.db.databasehepler.ClassInstanceHelper;
import com.example.cw_comp1786.db.databasehepler.UserTableHelper;
import com.example.cw_comp1786.db.databasehepler.YogaclassTableHelper;


public class MainActivity extends AppCompatActivity {
    UserTableHelper userTableHelper;
    BookingTableHelper bookingTableHelper;
    ClassInstanceHelper classInstanceHelper;
    YogaclassTableHelper yogaclassTableHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);




        userTableHelper = new UserTableHelper(this);
        userTableHelper.syncFromFirebaseToSQLite();

        bookingTableHelper = new BookingTableHelper(this);
        bookingTableHelper.syncFromFirebaseToSQLite();

        classInstanceHelper = new ClassInstanceHelper(this);
        classInstanceHelper.syncFromFirebaseToSQLite();

        yogaclassTableHelper = new YogaclassTableHelper(this);
        yogaclassTableHelper.syncFromFirebaseToSQLite();
///

        UserAdapter userAdapter = new UserAdapter(userTableHelper.getUsers());
        YogaclassAdapter yogaclassAdapter = new YogaclassAdapter(yogaclassTableHelper.getAllClasses());



        CardView cardMana_Class = findViewById(R.id.cardMana_Class);
        cardMana_Class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Allclass_Screen.class);
                startActivity(intent);
            }
        });
        CardView cardMana_User = findViewById(R.id.cardMana_User);
        cardMana_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserManagementActivity.class);
                startActivity(intent);
            }
        });
        CardView cardMana_Calender =findViewById(R.id.cardMana_Calender);
        cardMana_Calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CalenderManagerActivity.class);
                startActivity(intent);
            }
        });
        CardView cardMana_Booking =findViewById(R.id.cardMana_Booking);
        cardMana_Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Booking_Manager.class);
                startActivity(intent);
            }
        });

        TextView tvclasses = findViewById(R.id.tvclasses);
        tvclasses.setText(String.valueOf(yogaclassAdapter.getItemCount()));

        TextView tvstudents = findViewById(R.id.tvstudents);
        tvstudents.setText(String.valueOf(userAdapter.getItemCount()));

    }
}