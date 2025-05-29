package com.example.cw_comp1786;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cw_comp1786.db.databasehepler.YogaclassTableHelper;
import com.example.cw_comp1786.db.entity.Yogaclass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Updateclass_Screen extends AppCompatActivity {
    YogaclassTableHelper yogaclassTableHelper;
    private EditText editTextClassTitle, editTextCapacity, editTextDuration, editTextPrice, editTextClassType, editTextDescription;
    private Yogaclass yogaClass;
    private Button buttonUpdateClass;


    private TextView selectedDateTextView,
            selectedDayOfWeekTextView;
    private Button openDatePickerButton, selectTimeButton;
    private String selectedDate, selectedTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_updateclass_screen);

        yogaclassTableHelper = new YogaclassTableHelper(this);

        Intent intent = getIntent();
        String classId = intent.getStringExtra("class_id");

        yogaClass = yogaclassTableHelper.getClass(classId);

        editTextClassTitle = findViewById(R.id.editTextClassTitle);
        editTextCapacity = findViewById(R.id.editTextCapacity);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextClassType = findViewById(R.id.editTextClassType);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonUpdateClass = findViewById(R.id.buttonUpdateClass);

        selectedDayOfWeekTextView = findViewById(R.id.selectedDayOfWeekTextView);
        openDatePickerButton = findViewById(R.id.openDatePickerButton);
        selectTimeButton = findViewById(R.id.selectTimeButton);


        openDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });


        if (yogaClass != null) {
            editTextClassTitle.setText(yogaClass.getClasstitle());
            editTextCapacity.setText(String.valueOf(yogaClass.getCapacity()));
            editTextDuration.setText(String.valueOf(yogaClass.getDuration()));
            editTextPrice.setText(String.valueOf(yogaClass.getPrice()));
            editTextClassType.setText(yogaClass.getClasstype());
            editTextDescription.setText(yogaClass.getDescription());
            selectedDayOfWeekTextView.setText(yogaClass.getDay());
            selectTimeButton.setText(yogaClass.getTime());

        }

        buttonUpdateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClassDetails();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            calendar.set(year1, monthOfYear, dayOfMonth);
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//            String formattedDate = dateFormat.format(calendar.getTime());
//            selectedDateTextView.setText(formattedDate);

            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());
            selectedDayOfWeekTextView.setText(dayOfWeek);


            selectedDate = dayOfWeek;
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Chặn các ngày trước hôm nay
        datePickerDialog.show();
    }


    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
            selectTimeButton.setText(formattedTime);
            selectedTime = formattedTime;
        }, hour, minute, true);
        timePickerDialog.show();
    }


    private void updateClassDetails() {
        String updatedTitle = editTextClassTitle.getText().toString().trim();
        String updatedCapacity = editTextCapacity.getText().toString().trim();
        String updatedDuration = editTextDuration.getText().toString().trim();
        String updatedPrice = editTextPrice.getText().toString().trim();
        String updatedClassType = editTextClassType.getText().toString().trim();
        String updatedDescription = editTextDescription.getText().toString().trim();
        String updateTime = selectTimeButton.getText().toString().trim();
        String updateDay = selectedDayOfWeekTextView.getText().toString().trim();


        if (updatedTitle.isEmpty() || updatedCapacity.isEmpty() || updatedDuration.isEmpty() || updatedPrice.isEmpty() || updatedClassType.isEmpty()|| updateTime.isEmpty() || updateDay.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        yogaClass.setClasstitle(updatedTitle);
        yogaClass.setCapacity(Integer.parseInt(updatedCapacity));
        yogaClass.setDuration(Integer.parseInt(updatedDuration));
        yogaClass.setPrice(Double.parseDouble(updatedPrice));
        yogaClass.setClasstype(updatedClassType);
        yogaClass.setDescription(updatedDescription);
        yogaClass.setDay(updateDay);
        yogaClass.setTime(updateTime);

         yogaclassTableHelper.updateClass(yogaClass,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Yogaclass", "Class added successfully to SQLite and Firebase.");
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi thêm thất bại
                        Log.d("Yogaclass", "Failed to add class: " + e.getMessage());
                    }
                });

    }

}