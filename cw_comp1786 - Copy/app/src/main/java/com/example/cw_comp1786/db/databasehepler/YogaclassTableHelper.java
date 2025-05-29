package com.example.cw_comp1786.db.databasehepler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.cw_comp1786.db.DatabaseHelper;
import com.example.cw_comp1786.db.entity.Yogaclass;
import com.example.cw_comp1786.db.firebase.YogaFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class YogaclassTableHelper {

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;

    private YogaFirebase yogaFirebase;

    final static String CREATE_YOGACLASS_TABLE =
            "CREATE TABLE " + Yogaclass.TABLE_YOGA_CLASS + " ("
                    + Yogaclass.COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Yogaclass.COLUMN_CLASS_TITLE + " TEXT NOT NULL, "
                    + Yogaclass.COLUMN_CLASS_DAY + " TEXT NOT NULL, "
                    + Yogaclass.COLUMN_CLASS_TIME + " TEXT NOT NULL, "
                    + Yogaclass.COLUMN_CLASS_CAPACITY + " INTEGER NOT NULL, "
                    + Yogaclass.COLUMN_CLASS_DURATION + " INTEGER NOT NULL, "
                    + Yogaclass.COLUMN_CLASS_PRICE + " REAL NOT NULL, "
                    + Yogaclass.COLUMN_CLASS_TYPE + " TEXT NOT NULL, "
                    + Yogaclass.COLUMN_CLASS_DESCRIPTION + " TEXT NOT NULL, "
                    + Yogaclass.COLUMN_CLASS_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ");";

    public YogaclassTableHelper(Context context) {
        this.sqLiteOpenHelper = new DatabaseHelper(context);
        this.yogaFirebase = new YogaFirebase();
    }

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_YOGACLASS_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Yogaclass.TABLE_YOGA_CLASS);
        onCreate(sqLiteDatabase);
    }


    public void createClass(Yogaclass yogaclass, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        long result = createClassInSQLite(yogaclass);

        if (result != -1) {
            yogaclass.setClassid(String.valueOf(result));
            yogaFirebase.addYogaClass(yogaclass, onSuccessListener, onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Failed to insert class into SQLite"));
        }
    }
    private long createClassInSQLite(Yogaclass yogaclass) {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Yogaclass.COLUMN_CLASS_TITLE, yogaclass.getClasstitle());
        values.put(Yogaclass.COLUMN_CLASS_DAY, yogaclass.getDay());
        values.put(Yogaclass.COLUMN_CLASS_TIME, yogaclass.getTime());
        values.put(Yogaclass.COLUMN_CLASS_CAPACITY, yogaclass.getCapacity());
        values.put(Yogaclass.COLUMN_CLASS_DURATION, yogaclass.getDuration());
        values.put(Yogaclass.COLUMN_CLASS_PRICE, yogaclass.getPrice());
        values.put(Yogaclass.COLUMN_CLASS_TYPE, yogaclass.getClasstype());
        values.put(Yogaclass.COLUMN_CLASS_DESCRIPTION, yogaclass.getDescription());

        long id = sqLiteDatabase.insert(Yogaclass.TABLE_YOGA_CLASS, null, values);
        sqLiteDatabase.close();
        return id;
    }


    public Yogaclass getClass(String classId) {
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String[] columns = {
                Yogaclass.COLUMN_CLASS_ID,
                Yogaclass.COLUMN_CLASS_TITLE,
                Yogaclass.COLUMN_CLASS_DAY,
                Yogaclass.COLUMN_CLASS_TIME,
                Yogaclass.COLUMN_CLASS_CAPACITY,
                Yogaclass.COLUMN_CLASS_DURATION,
                Yogaclass.COLUMN_CLASS_PRICE,
                Yogaclass.COLUMN_CLASS_TYPE,
                Yogaclass.COLUMN_CLASS_DESCRIPTION,
                Yogaclass.COLUMN_CLASS_CREATED_AT
        };
        String selection = Yogaclass.COLUMN_CLASS_ID + " = ?";
        String[] selectionArgs = {classId};

        Cursor cursor = sqLiteDatabase.query(
                Yogaclass.TABLE_YOGA_CLASS,
                columns,
                selection,
                selectionArgs,
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Yogaclass yogaclass = new Yogaclass();
            yogaclass.setClassid(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_ID)));
            yogaclass.setClasstitle(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_TITLE)));
            yogaclass.setDay(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_DAY)));
            yogaclass.setTime(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_TIME)));
            yogaclass.setCapacity(cursor.getInt(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_CAPACITY)));
            yogaclass.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_DURATION)));
            yogaclass.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_PRICE)));
            yogaclass.setClasstype(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_TYPE)));
            yogaclass.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_DESCRIPTION)));
            cursor.close();
            return yogaclass;
        } else {
            if (cursor != null) cursor.close();
            return null;
        }
    }



    public void deleteClass(String classId, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        int result = deleteClassInSQLite(classId);
        if (result > 0) {
            yogaFirebase.deleteYogaClass(classId, onSuccessListener, onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Failed to delete class from SQLite"));
        }
    }



    private int deleteClassInSQLite(String classId) {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        String selection = Yogaclass.COLUMN_CLASS_ID + " = ?";
        String[] selectionArgs = {classId};
        int result = sqLiteDatabase.delete(Yogaclass.TABLE_YOGA_CLASS, selection, selectionArgs);
        sqLiteDatabase.close();
        return result;
    }




    public void updateClass(Yogaclass yogaclass, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        int result = updateClassInSQLite(yogaclass);
        if (result > 0) {
            yogaFirebase.updateYogaClass(yogaclass, onSuccessListener, onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Failed to update class in SQLite"));
        }
    }


    private int updateClassInSQLite(Yogaclass yogaclass) {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Yogaclass.COLUMN_CLASS_TITLE, yogaclass.getClasstitle());
        values.put(Yogaclass.COLUMN_CLASS_DAY, yogaclass.getDay());
        values.put(Yogaclass.COLUMN_CLASS_TIME, yogaclass.getTime());
        values.put(Yogaclass.COLUMN_CLASS_CAPACITY, yogaclass.getCapacity());
        values.put(Yogaclass.COLUMN_CLASS_DURATION, yogaclass.getDuration());
        values.put(Yogaclass.COLUMN_CLASS_PRICE, yogaclass.getPrice());
        values.put(Yogaclass.COLUMN_CLASS_TYPE, yogaclass.getClasstype());
        values.put(Yogaclass.COLUMN_CLASS_DESCRIPTION, yogaclass.getDescription());

        String selection = Yogaclass.COLUMN_CLASS_ID + " = ?";
        String[] selectionArgs = {yogaclass.getClassid()};

        return sqLiteDatabase.update(Yogaclass.TABLE_YOGA_CLASS, values, selection, selectionArgs);
    }


    // Lấy danh sách tất cả các lớp học
    public List<Yogaclass> getAllClasses() {
        List<Yogaclass> classList = new ArrayList<>();
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        String query = "SELECT * FROM " + Yogaclass.TABLE_YOGA_CLASS;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Yogaclass yogaclass = new Yogaclass();
                yogaclass.setClassid(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_ID)));
                yogaclass.setClasstitle(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_TITLE)));
                yogaclass.setDay(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_DAY)));
                yogaclass.setTime(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_TIME)));
                yogaclass.setCapacity(cursor.getInt(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_CAPACITY)));
                yogaclass.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_DURATION)));
                yogaclass.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_PRICE)));
                yogaclass.setClasstype(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_TYPE)));
                yogaclass.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Yogaclass.COLUMN_CLASS_DESCRIPTION)));
                classList.add(yogaclass);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classList;
    }

    public void syncFromFirebaseToSQLite() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("yoga_classes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Yogaclass yogaClass = snapshot.getValue(Yogaclass.class);

                    if (yogaClass != null) {
                        insertOrUpdateYogaClass(yogaClass);
                    }
                }
                Log.d("YogaFirebase", "Dữ liệu từ Firebase đã được đồng bộ vào SQLite");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("YogaFirebase", "Lỗi khi lấy dữ liệu từ Firebase", databaseError.toException());
            }
        });
    }

    public void insertOrUpdateYogaClass(Yogaclass yogaClass) {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Yogaclass.COLUMN_CLASS_ID, yogaClass.getClassid());
        values.put(Yogaclass.COLUMN_CLASS_TITLE, yogaClass.getClasstitle());
        values.put(Yogaclass.COLUMN_CLASS_TYPE, yogaClass.getClasstype());
        values.put(Yogaclass.COLUMN_CLASS_CAPACITY, yogaClass.getCapacity());
        values.put(Yogaclass.COLUMN_CLASS_DESCRIPTION, yogaClass.getDescription());
        values.put(Yogaclass.COLUMN_CLASS_DURATION, yogaClass.getDuration());
        values.put(Yogaclass.COLUMN_CLASS_PRICE, yogaClass.getPrice());
        values.put(Yogaclass.COLUMN_CLASS_TIME, yogaClass.getTime());
        values.put(Yogaclass.COLUMN_CLASS_DAY, yogaClass.getDay());

        int rows = sqLiteDatabase.update(Yogaclass.TABLE_YOGA_CLASS, values, Yogaclass.COLUMN_CLASS_ID + " = ?", new String[]{yogaClass.getClassid()});
        if (rows == 0) {
            sqLiteDatabase.insert(Yogaclass.TABLE_YOGA_CLASS, null, values);
        }

        sqLiteDatabase.close();
    }

}