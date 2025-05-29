package com.example.cw_comp1786.db.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cw_comp1786.db.entity.ClassInstance;
import com.example.cw_comp1786.db.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassInstanceFirebase extends FirebaseHelper {

    public void addClassInstance(ClassInstance classInstance, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classInstances");
        int classInstanceId = -1;
        classInstanceId = classInstance.getInstanceId();
        if (classInstanceId == -1) {
            onFailureListener.onFailure(new Exception("User ID cannot be null"));
            return;
        }
        databaseReference.child(String.valueOf(classInstanceId)).setValue(classInstance)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void updateClassInstance(ClassInstance classInstance, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classInstances").child(String.valueOf(classInstance.getInstanceId()));
        databaseReference.setValue(classInstance)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }


    public void deleteClassInstance(int InstanceId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        Log.d("classInstances", "Attempting to delete classInstance with ID: " + InstanceId);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classInstances").child(String.valueOf(InstanceId));
        databaseReference.removeValue()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}
