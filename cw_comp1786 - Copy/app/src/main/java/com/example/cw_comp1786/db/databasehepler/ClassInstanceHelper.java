package com.example.cw_comp1786.db.databasehepler;

import static android.provider.Settings.System.DATE_FORMAT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cw_comp1786.db.DatabaseHelper;
import com.example.cw_comp1786.db.entity.ClassInstance;
import com.example.cw_comp1786.db.entity.User;
import com.example.cw_comp1786.db.firebase.ClassInstanceFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClassInstanceHelper {
    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private ClassInstanceFirebase classInstanceFirebase;

    private static final String CREATE_TABLE_CLASSSESSION = "CREATE TABLE " + ClassInstance.TABLE_CLASSSESSION + " ("
            + ClassInstance.COLUMN_CLASSSESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ClassInstance.COLUMN_CLASSSESSION_CLASS_ID + " INTEGER, "
            + ClassInstance.COLUMN_CLASSSESSION_SESSION_DATE + " DATE NOT NULL, "
            + ClassInstance.COLUMN_CLASSSESSION_USER_ID + " INTEGER, "
            + ClassInstance.COLUMN_CLASSSESSION_COMMENTS + " TEXT, "
            + "FOREIGN KEY(" + ClassInstance.COLUMN_CLASSSESSION_CLASS_ID + ") REFERENCES YogaClass(class_id) ON DELETE CASCADE, "
            + "FOREIGN KEY(" + ClassInstance.COLUMN_CLASSSESSION_USER_ID + ") REFERENCES User(user_id) ON DELETE SET NULL);";

    public static void  onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CLASSSESSION);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ClassSession.TABLE_CLASSSESSION ");
        onCreate(sqLiteDatabase);
    }


    public ClassInstanceHelper(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
        this.classInstanceFirebase = new ClassInstanceFirebase();
    }


    public void createClassInstanceinFirebase(ClassInstance session, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        long result = addClassSession(session);

        if (result != -1) {
            session.setInstanceId((int) result);
            classInstanceFirebase.addClassInstance(session, onSuccessListener, onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Failed to insert Class Instance into SQLite"));
        }
    }


    public long addClassSession(ClassInstance session) {
        db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassInstance.COLUMN_CLASSSESSION_CLASS_ID, session.getClassId());
        values.put(ClassInstance.COLUMN_CLASSSESSION_SESSION_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(session.getSessionDate()));
        values.put(ClassInstance.COLUMN_CLASSSESSION_USER_ID, session.getUserId());
        values.put(ClassInstance.COLUMN_CLASSSESSION_COMMENTS, session.getComments());

        long result =  db.insert(ClassInstance.TABLE_CLASSSESSION, null, values);
        db.close();
        return result;
    }


    public List<ClassInstance> getAllClassSessions() {
        List<ClassInstance> classInstanceList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ClassInstance.TABLE_CLASSSESSION;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ClassInstance session = new ClassInstance();
                session.setInstanceId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_ID))));
                session.setClassId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_CLASS_ID))));

                String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_SESSION_DATE));
                try {
                    Date date = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(dateStr);
                    session.setSessionDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                session.setUserId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_USER_ID))));
                session.setComments(cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_COMMENTS)));

                classInstanceList.add(session);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return classInstanceList;
    }


    // SQLite and Firebase
    public void deleteUserInfirebase(int instanceId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        long result = deleteClassSession(instanceId);
        if (result > 0) {
            classInstanceFirebase.deleteClassInstance(instanceId, onSuccessListener, onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Failed to delete User from SQLite"));
        }
    }
    // Delete a class session by ID
    public long deleteClassSession(int sessionId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result = db.delete(ClassInstance.TABLE_CLASSSESSION, ClassInstance.COLUMN_CLASSSESSION_ID + " = ?", new String[]{String.valueOf(sessionId)});
        db.close();
        return result;
    }



    // Update a class session SQLite and Firebase
    public void updateClassInfirebase(ClassInstance classInstance, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        long result = updateClassSession(classInstance);
        if (result > 0) {
            classInstanceFirebase.updateClassInstance(classInstance, onSuccessListener, onFailureListener);
        } else {
            // Xử lý lỗi khi không cập nhật thành công vào SQLite
            onFailureListener.onFailure(new Exception("Failed to update class in SQLite"));
        }
    }


    public long updateClassSession(ClassInstance session) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ClassInstance.COLUMN_CLASSSESSION_CLASS_ID, session.getClassId());
        values.put(ClassInstance.COLUMN_CLASSSESSION_SESSION_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(session.getSessionDate()));
        values.put(ClassInstance.COLUMN_CLASSSESSION_USER_ID, session.getUserId());
        values.put(ClassInstance.COLUMN_CLASSSESSION_COMMENTS, session.getComments());

        long result = db.update(ClassInstance.TABLE_CLASSSESSION, values, ClassInstance.COLUMN_CLASSSESSION_ID + " = ?", new String[]{String.valueOf(session.getInstanceId())});
        db.close();
        return result;
    }



    public ClassInstance getClassInstanceById(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ClassInstance session = null;

        String selectQuery = "SELECT * FROM " + ClassInstance.TABLE_CLASSSESSION + " WHERE " + ClassInstance.COLUMN_CLASSSESSION_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            session = new ClassInstance();
            session.setInstanceId(cursor.getInt(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_ID)));
            session.setClassId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_CLASS_ID))));

            String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_SESSION_DATE));
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr);
                session.setSessionDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            session.setUserId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_USER_ID))));
            session.setComments(cursor.getString(cursor.getColumnIndexOrThrow(ClassInstance.COLUMN_CLASSSESSION_COMMENTS)));
        }

        cursor.close();
        db.close();
        return session;
    }

    public void syncFromFirebaseToSQLite() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classInstances");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ClassInstance classInstance = snapshot.getValue(ClassInstance.class);
                    if (classInstance != null) {
                        insertOrUpdateUser(classInstance);
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

    public void insertOrUpdateUser(ClassInstance classInstance) {
        db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClassInstance.COLUMN_CLASSSESSION_CLASS_ID, classInstance.getClassId());
        if (classInstance.getSessionDate() != null) {
            values.put(ClassInstance.COLUMN_CLASSSESSION_SESSION_DATE,
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(classInstance.getSessionDate()));
        } else {
            Log.e("insertOrUpdateUser", "Session date is null for instance ID: " + classInstance.getInstanceId());
        }

        values.put(ClassInstance.COLUMN_CLASSSESSION_USER_ID, classInstance.getUserId());
        values.put(ClassInstance.COLUMN_CLASSSESSION_COMMENTS, classInstance.getComments());
        values.put(ClassInstance.COLUMN_CLASSSESSION_ID, classInstance.getInstanceId());

        int rows = db.update(ClassInstance.TABLE_CLASSSESSION, values,
                ClassInstance.COLUMN_CLASSSESSION_ID + " = ?",
                new String[]{String.valueOf(classInstance.getInstanceId())});
        if (rows == 0) {
            db.insert(ClassInstance.TABLE_CLASSSESSION, null, values);
        }
        db.close();
    }


}
