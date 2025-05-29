package com.example.cw_comp1786.db.databasehepler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cw_comp1786.db.DatabaseHelper;
import com.example.cw_comp1786.db.entity.User;
import com.example.cw_comp1786.db.firebase.UserFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserTableHelper {


    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private UserFirebase userFirebase;


    public static final String CREATE_USER_TABLE =
            "CREATE TABLE User ("
                    + "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "email TEXT NOT NULL,"
                    + "password TEXT NOT NULL,"
                    + "role TEXT NOT NULL,"
                    + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public UserTableHelper(Context context) {
        this.sqLiteOpenHelper = new DatabaseHelper(context);
        this.userFirebase = new UserFirebase();
    }

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
        onCreate(sqLiteDatabase);
    }


    public void createUserInFirebase(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        long result = createUser(user);

        if (result != -1) {
            user.setId(String.valueOf(result));
            userFirebase.addUser(user, onSuccessListener, onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Failed to insert User into SQLite"));
        }
    }


    public long createUser(User user){
         db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.COLUMN_USER_NAME, user.getName());
        values.put(User.COLUMN_USER_EMAIL, user.getEmail());
        values.put(User.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(User.COLUMN_USER_ROLE, user.getRole());
        return db.insert(User.TABLE_USER, null, values);
    }


    public User getUser(String userId) {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(User.TABLE_USER,
                null,
                User.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_ROLE)));
                cursor.close();
                return user;
            }
            cursor.close();
        }
        return null;
    }



    public List<User> getTeachers(){
        List<User> teachers = new ArrayList<>();
        db = sqLiteOpenHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + User.TABLE_USER + " WHERE " + User.COLUMN_USER_ROLE + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"Teacher"});

        if(cursor.moveToFirst()){
            do{
                User teacher = new User();
                teacher.setId(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_ID)));
                teacher.setName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_NAME)));
                teacher.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_PASSWORD)));
                teacher.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_EMAIL)));
                teacher.setRole(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_ROLE)));
            teachers.add(teacher);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teachers;
    }

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        db = sqLiteOpenHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + User.TABLE_USER + " WHERE " + User.COLUMN_USER_ROLE + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"User"});

        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setId(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_PASSWORD)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_EMAIL)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_ROLE)));
                users.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + User.TABLE_USER;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USER_ROLE)));

                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }



    // Hàm cập nhật user vào SQLite và Firebase
    public void updateClassInfirebase(User user, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        int result = updateUser(user);
        if (result > 0) {
            userFirebase.updateUser(user, onSuccessListener, onFailureListener);
        } else {
            // Xử lý lỗi khi không cập nhật thành công vào SQLite
            onFailureListener.onFailure(new Exception("Failed to update class in SQLite"));
        }
    }

    public int updateUser(User user) {
        db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.COLUMN_USER_NAME, user.getName());
        values.put(User.COLUMN_USER_EMAIL, user.getEmail());
        values.put(User.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(User.COLUMN_USER_ROLE, user.getRole());


        return db.update(User.TABLE_USER, values, User.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }


    // Hàm xóa lớp học từ SQLite và Firebase
    public void deleteUserInfirebase(String userId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        int result = deleteUser(userId);
        if (result > 0) {
            userFirebase.deleteUser(userId, onSuccessListener, onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Failed to delete User from SQLite"));
        }
    }

    public int deleteUser(String userId) {
        db = sqLiteOpenHelper.getWritableDatabase();
        String selection = User.COLUMN_USER_ID + " = ? ";
        String[] selectionAgrs ={userId};
        return db.delete(User.TABLE_USER, selection,selectionAgrs);
    }



    public void syncFromFirebaseToSQLite() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        insertOrUpdateUser(user);
                    }
                }
                Log.d("YogaFirebase", "Data from Firebase has been synced to SQLite");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("YogaFirebase", "Error while fetching data from Firebase", databaseError.toException());
            }
        });
    }

    public void insertOrUpdateUser(User user) {
        db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.COLUMN_USER_ID, user.getId());
        values.put(User.COLUMN_USER_NAME, user.getName());
        values.put(User.COLUMN_USER_EMAIL, user.getEmail());
        values.put(User.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(User.COLUMN_USER_ROLE, user.getRole());


        int rows = db.update(User.TABLE_USER, values, User.COLUMN_USER_ID + " = ?", new String[]{user.getId()});
        if (rows == 0) {
            db.insert(User.TABLE_USER, null, values);
        }

        db.close();
    }

}

