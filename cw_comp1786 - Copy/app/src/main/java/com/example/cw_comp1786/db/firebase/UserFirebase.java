package com.example.cw_comp1786.db.firebase;

import android.util.Log;

import com.example.cw_comp1786.db.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFirebase extends FirebaseHelper{

    public void addUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String userId = user.getId();
        if (userId == null) {
            onFailureListener.onFailure(new Exception("User ID cannot be null"));
            return;
        }
        databaseReference.child(userId).setValue(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }


    public void updateUser(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getId());
        databaseReference.setValue(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }


    public void deleteUser(String userId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        Log.d("User", "Attempting to delete User with ID: " + userId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.removeValue()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}
