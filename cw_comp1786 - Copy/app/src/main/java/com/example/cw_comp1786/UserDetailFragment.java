package com.example.cw_comp1786;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw_comp1786.adapter.UserAdapter;
import com.example.cw_comp1786.db.databasehepler.UserTableHelper;
import com.example.cw_comp1786.db.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class UserDetailFragment extends Fragment {
    private UserTableHelper userTableHelper;
    private User user;
    private  UserAdapter userAdapter;

    TextView editName, editEmail, editPassword;
    Spinner roleSpinner;
    Button btn_update_user, btn_delete_user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        userTableHelper = new UserTableHelper(getContext());


        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        roleSpinner = view.findViewById(R.id.roleSpinner);
        btn_update_user = view.findViewById(R.id.btn_updateUser);
        btn_delete_user = view.findViewById(R.id.btn_deleteUser);

        btn_update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_user();
            }
        });

        btn_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_delete_user(user);
            }
        });



        if (getArguments() != null) {
            String userId = getArguments().getString("user_id");
            user = userTableHelper.getUser(userId);

            editName.setText(user.getName());
            editEmail.setText(user.getEmail());
            editPassword.setText(user.getPassword());

            String[] roles = {"Teacher", "User"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, roles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roleSpinner.setAdapter(adapter);
            String userRole = user.getRole();
            int spinnerPosition = adapter.getPosition(userRole);
            roleSpinner.setSelection(spinnerPosition);
        }



        return view;
    }

    private void confirm_delete_user(User user) {
        AlertDialog.Builder buider = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogview = inflater.inflate(R.layout.dialog_confirmation_delete, null);

        buider.setView(dialogview);

        buider.setPositiveButton("Confirm", (dialog, which)->{
           delete_user(user);
           dialog.dismiss();
        });
        buider.setNegativeButton("Cancle", (dialog, which)->{
            dialog.dismiss();
        });

        AlertDialog alertDialog = buider.create();
        alertDialog.show();

    }

    private void delete_user(User user) {
    userTableHelper.deleteUserInfirebase(user.getId(),
            new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Deleted User Successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new UserListFragment())
                            .commit();
                }
            },
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Deleted User Failed", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void update_user() {
        String updateName = editName.getText().toString().trim();
        String updateEmail = editEmail.getText().toString().trim();
        String updatePassword = editPassword.getText().toString().trim();
        String updateRole = roleSpinner.getSelectedItem().toString().trim();
        if (updateName == null || updateEmail == null|| updatePassword == null || updateRole == null) {
            Toast.makeText(getContext(), "Please select both date and time.", Toast.LENGTH_SHORT).show();
            return;
        }
        user.setName(updateName);
        user.setEmail(updateEmail);
        user.setPassword(updatePassword);
        user.setRole(updateRole);
        userTableHelper.updateClassInfirebase(user,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Class updated successfully!", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new UserListFragment())
                                .commit();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to update class.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}