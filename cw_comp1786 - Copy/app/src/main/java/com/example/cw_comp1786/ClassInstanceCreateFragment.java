package com.example.cw_comp1786;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw_comp1786.db.databasehepler.ClassInstanceHelper;
import com.example.cw_comp1786.db.databasehepler.UserTableHelper;
import com.example.cw_comp1786.db.databasehepler.YogaclassTableHelper;
import com.example.cw_comp1786.db.entity.ClassInstance;
import com.example.cw_comp1786.db.entity.User;
import com.example.cw_comp1786.db.entity.Yogaclass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClassInstanceCreateFragment extends Fragment {
    private Spinner spinnerClass, spinnerTeacher;
    private TextView textDay, textTime;
    private DatePicker datePicker;
    private Button buttonSaveInstance;
    private EditText editComment;

    private int selectedClassId = -1, selectedTeacherId = -1;
    private String classDayOfWeek, comment= " ";


    private UserTableHelper userTableHelper;
    private YogaclassTableHelper yogaclassTableHelper;
    private ClassInstanceHelper classInstanceHelper;
    private List<User> teacherLists;
    private List<Yogaclass> allYogaclass;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=   inflater.inflate(R.layout.fragment_create_class_instance, container, false);

        spinnerClass = view.findViewById(R.id.spinner_class_update);
        spinnerTeacher = view.findViewById(R.id.spinner_teacher_update);
        textDay = view.findViewById(R.id.text_day_update);
        textTime = view.findViewById(R.id.text_time_update);
        datePicker = view.findViewById(R.id.datepicker_class_instance_date_update);
        buttonSaveInstance = view.findViewById(R.id.button_save_class_instance);
        editComment = view.findViewById(R.id.editComment_update);

        comment = editComment.getText().toString().trim();


        userTableHelper = new UserTableHelper(getContext());
        yogaclassTableHelper = new YogaclassTableHelper(getContext());
        classInstanceHelper = new ClassInstanceHelper(getContext());

        loadClasses();
        loadTeachers();


        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Yogaclass selectedClass = allYogaclass.get(position);
                selectedClassId = Integer.parseInt(selectedClass.getClassid());

                textDay.setText(selectedClass.getDay());
                textTime.setText(selectedClass.getTime());
                classDayOfWeek = selectedClass.getDay();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                User selectedTeacher = teacherLists.get(position);
                selectedTeacherId = Integer.parseInt(selectedTeacher.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        buttonSaveInstance.setOnClickListener(v -> saveClassInstance());

        return view;
    }

    private void saveClassInstance() {
        comment = editComment.getText().toString().trim();

        if (selectedClassId == -1 || selectedTeacherId == -1) {
            Toast.makeText(getContext(), "Please select both class and teacher", Toast.LENGTH_SHORT).show();
            return;
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String selectedDay = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));

        if (!selectedDay.equals(classDayOfWeek)) {
            Toast.makeText(getContext(), "Please select a date that matches the class day", Toast.LENGTH_SHORT).show();
            return;
        }

        ClassInstance classInstance = new ClassInstance();
        classInstance.setClassId(String.valueOf(selectedClassId));
        classInstance.setUserId(String.valueOf(selectedTeacherId));
        classInstance.setSessionDate(calendar.getTime());
        classInstance.setComments(comment);

        classInstanceHelper.createClassInstanceinFirebase(classInstance,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Class instance saved successfully", Toast.LENGTH_SHORT).show();
                        ((CalenderManagerActivity) getActivity()).goBackToFragment(new ClassInstanceListFragment());
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to add class instance.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY: return "Sunday";
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            case Calendar.SATURDAY: return "Saturday";
            default: return "";
        }
    }

    private void loadTeachers() {
        teacherLists = userTableHelper.getTeachers();
        List<String> teacherNames = new ArrayList<>();
        for (User teacher : teacherLists) {
            teacherNames.add(teacher.getName());
        }
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, teacherNames);
        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeacher.setAdapter(teacherAdapter);



    }

    private void loadClasses() {
        allYogaclass = yogaclassTableHelper.getAllClasses();
        List<String> classTitles = new ArrayList<>();
        for (Yogaclass yogaclass : allYogaclass) {
            classTitles.add(yogaclass.getClasstitle());
        }
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, classTitles);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

    }


}