package com.example.cw_comp1786.db.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    protected FirebaseFirestore db;

    public FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
    }
}
