package com.example.cw_comp1786;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw_comp1786.db.databasehepler.UserTableHelper;
import com.example.cw_comp1786.db.entity.User;
import com.example.cw_comp1786.db.firebase.UserFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.util.Date;


public class UserAddFragment extends Fragment {
    private Spinner roleSpinner;
    private EditText editName, editEmail,editPassword,editPassword2;
    private Button btn_createUser;
    private String selectedRole;
    private UserTableHelper userTableHelper;



    public UserAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_add, container, false);

        userTableHelper = new UserTableHelper(getContext());

        roleSpinner = view.findViewById(R.id.roleSpinner);
        String[] roles = {"Teacher", "User"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = roles[position];
                Toast.makeText(getContext(), "Selected Role: " + selectedRole, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        editPassword2 = view.findViewById(R.id.editPassword2);
        btn_createUser = view.findViewById(R.id.btn_createUser);


        btn_createUser.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString();
            String confirmPassword = editPassword2.getText().toString();
            if (!isValidEmail(email)) {
                Toast.makeText(getContext(), "Invalid email format. Please enter a valid email.", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
            } else {
                showConfirmDialogUser();
            }
        });

        return view;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showConfirmDialogUser() {
        String userName = editName.getText().toString().trim();
        String userEmail= editEmail.getText().toString().trim();
        String userPassword = editPassword.getText().toString().trim();
        String userRole = selectedRole;
        Date createdAt = new Date();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirmation_adduser, null);
        builder.setView(dialogView);

        TextView tvname = dialogView.findViewById(R.id.tv_user_name);
        TextView tvpassword = dialogView.findViewById(R.id.tv_user_password);
        TextView tvemail = dialogView.findViewById(R.id.tv_user_email);
        TextView tvrole = dialogView.findViewById(R.id.tv_user_role1);

        tvname.setText("Username: " +userName);
        tvpassword.setText("Password: " +userPassword);
        tvemail.setText("Email: " + userEmail);
        tvrole.setText("Role: " + userRole);


        builder.setPositiveButton("Confirm", (dialog, which) -> {
            addNewUser(userTableHelper, userName, userPassword, userEmail, userRole, createdAt);
            Toast.makeText(getContext(), "User added successfully!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

            getParentFragmentManager().popBackStack();

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UserListFragment())
                    .commit();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void addNewUser(UserTableHelper userTableHelper, String userName, String userPassword, String userEmail, String userRole, Date createdAt) {
        User newuser = new User(null,
                userName,
                userEmail,
                userPassword,
                userRole,
                createdAt
        );
        userTableHelper.createUserInFirebase(newuser,  new OnSuccessListener<Void>() {
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
                });

    }
}