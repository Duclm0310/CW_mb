package com.example.cw_comp1786;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cw_comp1786.adapter.ClassinstanceAdapter;
import com.example.cw_comp1786.db.DatabaseHelper;
import com.example.cw_comp1786.db.databasehepler.ClassInstanceDisplayHelper;
import com.example.cw_comp1786.db.databasehepler.ClassInstanceHelper;
import com.example.cw_comp1786.db.databasehepler.UserTableHelper;
import com.example.cw_comp1786.db.entity.ClassInstanceDisplay;
import com.example.cw_comp1786.db.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClassInstanceListFragment extends Fragment {
    private ClassinstanceAdapter classSessionAdapter;
    private List<ClassInstanceDisplay> classInstanceDisplays;
    private RecyclerView recycler_view_classsesion;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private UserTableHelper userTableHelper;
    private TextView tvDay;

    private Button btn_create_classsession, btn_pick_date;
    private List<User> teacherList;


    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;

    public ClassInstanceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteOpenHelper = new DatabaseHelper(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_instance_list, container, false);
        tvDay = view.findViewById(R.id.tv_Day);

        userTableHelper = new UserTableHelper(getContext());
        ClassInstanceHelper classInstanceHelper = new ClassInstanceHelper(getContext());
        classInstanceHelper.syncFromFirebaseToSQLite();

        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        tvDay.setText(currentDate);

        classInstanceDisplays = ClassInstanceDisplayHelper.getClassSessionDisplayListByDate(sqLiteDatabase, selectedYear, selectedMonth + 1, selectedDay);
        teacherList = userTableHelper.getTeachers();


        recycler_view_classsesion = view.findViewById(R.id.recycler_view_classsesion);
        recycler_view_classsesion.setLayoutManager(new LinearLayoutManager(getContext()));
        classSessionAdapter = new ClassinstanceAdapter(classInstanceDisplays);
        recycler_view_classsesion.setAdapter(classSessionAdapter);
        classSessionAdapter.setOnItemClickListener((sessionId, position) -> {

            Fragment updateFragment = new ClassInstanceDetailFragment();
            Bundle args = new Bundle();
            args.putInt("sessionId", sessionId);
            updateFragment.setArguments(args);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_classsession, updateFragment);
            ((CalenderManagerActivity) requireActivity()).loadFragment(updateFragment, true);
            transaction.commit();
        });


        btn_create_classsession = view.findViewById(R.id.btn_create_classsession);
        btn_create_classsession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment classInstanceCreateFragment = new ClassInstanceCreateFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_classsession,classInstanceCreateFragment );
                ((CalenderManagerActivity) requireActivity()).loadFragment(classInstanceCreateFragment, true);
                transaction.commit();
            }
        });


        btn_pick_date = view.findViewById(R.id.btn_pick_date);
        btn_pick_date.setOnClickListener(view12 -> showDatePickerDialog(teacherList));


        return view;
    }

    private void showDatePickerDialog(List<User> teacherList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        Spinner spinnerTeachers = dialogView.findViewById(R.id.spinner_teachers);
        CheckBox checkBoxSelectAllDays = dialogView.findViewById(R.id.checkbox_select_all_days);
        Button btnSetFilter = dialogView.findViewById(R.id.btn_set_filter);

// setspinner
        teacherList = userTableHelper.getTeachers();
        List<String> teacherNames = new ArrayList<>();
        for (User teacher : teacherList) {
            teacherNames.add(teacher.getName());
        }
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, teacherNames);
        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeachers.setAdapter(teacherAdapter);

//calender
        final Calendar selectedDate = Calendar.getInstance();
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate.set(year, month, dayOfMonth);
        });

        checkBoxSelectAllDays.setOnCheckedChangeListener((buttonView, isChecked) -> {
            calendarView.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        });


        btnSetFilter.setOnClickListener(v -> {
            String selectedTeacher = spinnerTeachers.getSelectedItem().toString();
            if (checkBoxSelectAllDays.isChecked()) {
                classInstanceDisplays = ClassInstanceDisplayHelper.getClassSessionDisplayListByUser(sqLiteDatabase, selectedTeacher);
            } else {
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH) + 1;
                int day = selectedDate.get(Calendar.DAY_OF_MONTH);
                classInstanceDisplays = ClassInstanceDisplayHelper.getClassSessionDisplayListByDateAndUser(sqLiteDatabase, year, month, day, selectedTeacher);
            }
            classSessionAdapter.updateData(classInstanceDisplays);
            dialog.dismiss();
        });

        dialog.show();
    }


    private void filterClassInstancesByDate(int year, int month, int day) {
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        classInstanceDisplays = ClassInstanceDisplayHelper.getClassSessionDisplayListByDate(sqLiteDatabase, year, month, day);
        sqLiteDatabase.close();
        classSessionAdapter.updateData(classInstanceDisplays);
    }


}