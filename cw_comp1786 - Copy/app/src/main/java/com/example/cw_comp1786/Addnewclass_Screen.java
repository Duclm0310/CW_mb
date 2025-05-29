package com.example.cw_comp1786;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cw_comp1786.adapter.YogaclassAdapter;
import com.example.cw_comp1786.db.databasehepler.YogaclassTableHelper;
import com.example.cw_comp1786.db.entity.Yogaclass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Addnewclass_Screen extends AppCompatActivity {
    private TextView selectedDateTextView, selectedDayOfWeekTextView;
    private Button selectTimeButton;
    private EditText editTextClassTitle, editTextCapacity, editTextDuration, editTextPrice, editTextClassType, editTextDescription;
    private String selectedTime = "";
    private YogaclassTableHelper yogaclassTableHelper= new YogaclassTableHelper(this);
    private YogaclassAdapter yogaclassAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewclass);

        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        selectedDayOfWeekTextView = findViewById(R.id.selectedDayOfWeekTextView);
        editTextClassTitle = findViewById(R.id.editTextClassTitle);
        editTextCapacity = findViewById(R.id.editTextCapacity);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextClassType = findViewById(R.id.editTextClassType);
        editTextDescription = findViewById(R.id.editTextDescription);
        Button openDatePickerButton = findViewById(R.id.openDatePickerButton);
        Button buttonAddClass = findViewById(R.id.buttonAddClass);
        selectTimeButton = findViewById(R.id.selectTimeButton);

        openDatePickerButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        selectedDateTextView.setText(selectedDate);

                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                        String dayOfWeek = dayFormat.format(calendar.getTime());
                        selectedDayOfWeekTextView.setText(dayOfWeek);
                    }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        selectTimeButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (TimePicker view, int selectedHour, int selectedMinute) -> {
                        selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        selectTimeButton.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        buttonAddClass.setOnClickListener(v -> {
            showConfirmationDialog();
        });
    }

    private void showConfirmationDialog() {
        String classTitle = editTextClassTitle.getText().toString().trim();
        String selectedDate = selectedDateTextView.getText().toString().trim();
        String dayOfWeek = selectedDayOfWeekTextView.getText().toString().trim();
        int capacity = Integer.parseInt(editTextCapacity.getText().toString().trim());
        int duration = Integer.parseInt(editTextDuration.getText().toString().trim());
        double price = Double.parseDouble(editTextPrice.getText().toString().trim());
        String classType = editTextClassType.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        Date createdAt = new Date();

        if (classTitle.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty() || classType.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirmation_addclass, null);
        builder.setView(dialogView);
        //

        TextView tvtitle = dialogView.findViewById(R.id.tv_class_session_title);
        TextView tvdayofweek = dialogView.findViewById(R.id.tv_class_dayofweek);
        TextView tvtime = dialogView.findViewById(R.id.tv_class_time);
        TextView tvcapacity = dialogView.findViewById(R.id.tv_class_capacity);
        TextView tvduration = dialogView.findViewById(R.id.tv_class_duration);
        TextView tvprice = dialogView.findViewById(R.id.tv_class_price);
        TextView tvtype = dialogView.findViewById(R.id.tv_class_type);
        TextView tvdescription = dialogView.findViewById(R.id.tv_class_description);
        //

        tvtitle.setText("Title: "+ classTitle);
        tvdayofweek.setText("Day: "+ selectedDate + " " + dayOfWeek);
        tvtime.setText("Time: " + selectedTime);
        tvcapacity.setText("Capacity: " + capacity);
        tvduration.setText("Duration: " + duration);
        tvprice.setText("Price: " + price);
        tvtype.setText("Type: "+ classType);
        tvdescription.setText("Description: " + description);
        //
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            addNewClass( yogaclassTableHelper, classTitle, dayOfWeek, selectedTime, capacity,duration, price,classType, description );
            dialog.dismiss();
            finish();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addNewClass(YogaclassTableHelper yogaclassTableHelper, String classTitle, String day, String selectedTime,
                             int capacity, int duration, double price, String classType, String description) {
                Date createdAt = new Date();
                Yogaclass newClass = new Yogaclass(
                null,
                classTitle,
                day,
                selectedTime,
                capacity,
                duration,
                price,
                classType,
                description,
                createdAt
        );
        yogaclassTableHelper.createClass(newClass,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Yogaclass", "Class added successfully to SQLite and Firebase.");
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Yogaclass", "Failed to add class: " + e.getMessage());
                    }
                }
        );
    }
}