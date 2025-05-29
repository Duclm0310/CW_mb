package com.example.cw_comp1786.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cw_comp1786.db.databasehepler.BookingTableHelper;
import com.example.cw_comp1786.db.databasehepler.ClassInstanceHelper;
import com.example.cw_comp1786.db.databasehepler.UserTableHelper;
import com.example.cw_comp1786.db.databasehepler.YogaclassTableHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "YogaClassManager.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserTableHelper.onCreate(db);
        YogaclassTableHelper.onCreate(db);
        ClassInstanceHelper.onCreate(db);
        BookingTableHelper.onCreate(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserTableHelper.onUpgrade(db, oldVersion, newVersion);
        YogaclassTableHelper.onUpgrade(db, oldVersion, newVersion);
        ClassInstanceHelper.onUpgrade(db, oldVersion, newVersion);
        BookingTableHelper.onUpgrade(db, oldVersion, newVersion);
    }


}
