package com.example.cw_comp1786.db.firebase;

import android.util.Log;

import com.example.cw_comp1786.db.DatabaseHelper;
import com.example.cw_comp1786.db.databasehepler.YogaclassTableHelper;
import com.example.cw_comp1786.db.entity.Yogaclass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class YogaFirebase extends FirebaseHelper {

    public void addYogaClass(Yogaclass yogaclass, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("yoga_classes");
        String classId = yogaclass.getClassid();
        if (classId == null) {
            onFailureListener.onFailure(new Exception("Class ID cannot be null"));
            return;
        }
        databaseReference.child(classId).setValue(yogaclass)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void updateYogaClass(Yogaclass yogaClass, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("yoga_classes").child(yogaClass.getClassid());
        databaseReference.setValue(yogaClass)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }


    public void deleteYogaClass(String classId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        Log.d("Yogaclass", "Attempting to delete class with ID: " + classId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("yoga_classes").child(classId);
        databaseReference.removeValue()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}