package com.example.cw_comp1786.db.databasehepler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cw_comp1786.db.entity.ClassInstance;
import com.example.cw_comp1786.db.entity.ClassInstanceDisplay;
import com.example.cw_comp1786.db.entity.Yogaclass;

import java.util.ArrayList;
import java.util.List;

public class ClassInstanceDisplayHelper {
//    private ClassInstance classInstance;
//    private Yogaclass yogaclass;

    public static List<ClassInstanceDisplay> getClassSessionDisplayList(SQLiteDatabase db) {
        List<ClassInstanceDisplay> displayList = new ArrayList<>();

        Cursor sessionCursor = db.rawQuery("SELECT * FROM " + ClassInstance.TABLE_CLASSSESSION, null);
        if (sessionCursor.moveToFirst()) {
            do {
                int sessionId = sessionCursor.getInt(sessionCursor.getColumnIndexOrThrow("session_id"));
                String userId = String.valueOf(sessionCursor.getInt(sessionCursor.getColumnIndexOrThrow("user_id")));
                String classId = String.valueOf(sessionCursor.getInt(sessionCursor.getColumnIndexOrThrow("class_id")));
                String sessionDate = sessionCursor.getString(sessionCursor.getColumnIndexOrThrow("session_date"));
                String userName = getUserNameById(db, userId);
                String classTitle = getClassTitleById(db, classId);
                String time = getClassTimeById(db, classId);

                ClassInstanceDisplay display = new ClassInstanceDisplay(sessionId, userName, classTitle, sessionDate, time, sessionDate);
                displayList.add(display);
            } while (sessionCursor.moveToNext());
        }
        sessionCursor.close();
        return displayList;
    }

    public static List<ClassInstanceDisplay> getClassSessionDisplayListByDate(SQLiteDatabase db, int year, int month, int day) {
        List<ClassInstanceDisplay> classInstances = new ArrayList<>();
        String date = String.format("%04d-%02d-%02d", year, month, day);
        Cursor cursor = db.rawQuery("SELECT * FROM " + ClassInstance.TABLE_CLASSSESSION + " WHERE " + ClassInstance.COLUMN_CLASSSESSION_SESSION_DATE + " = ?", new String[]{date});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int sessionId = cursor.getInt(cursor.getColumnIndexOrThrow("session_id"));
                String userId = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                String classId = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("class_id")));
                String sessionDate = cursor.getString(cursor.getColumnIndexOrThrow("session_date"));

                String userName = getUserNameById(db, userId);
                String classTitle = getClassTitleById(db, classId);
                String time = getClassTimeById(db, classId);

                ClassInstanceDisplay display = new ClassInstanceDisplay(sessionId, userName, classTitle, sessionDate, time, sessionDate);
                classInstances.add(display);
            }
            cursor.close();
        }
        return classInstances;
    }

    public static String getUserNameById(SQLiteDatabase db, String userId) {
        String userName = "";
        Cursor cursor = db.rawQuery("SELECT name FROM User WHERE user_id = ?", new String[]{userId});
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        }
        cursor.close();
        return userName;
    }


    private static String getClassTitleById(SQLiteDatabase db, String classId) {
        String classTitle = "";
        Cursor cursor = db.rawQuery("SELECT class_title FROM " + Yogaclass.TABLE_YOGA_CLASS + " WHERE class_id = ?", new String[]{classId});
        if (cursor.moveToFirst()) {
            classTitle = cursor.getString(cursor.getColumnIndexOrThrow("class_title"));
        }
        cursor.close();
        return classTitle;
    }


    private static String getClassTimeById(SQLiteDatabase db, String classId) {
        String time = "";
        Cursor cursor = db.rawQuery("SELECT time FROM " + Yogaclass.TABLE_YOGA_CLASS + " WHERE class_id = ?", new String[]{classId});
        if (cursor.moveToFirst()) {
            time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
        }
        cursor.close();
        return time;
    }


    public static List<ClassInstanceDisplay> getClassSessionDisplayListByDateAndUser(SQLiteDatabase db, int year, int month, int day, String userName) {
        List<ClassInstanceDisplay> classInstances = new ArrayList<>();
        String date = String.format("%04d-%02d-%02d", year, month, day);

        Cursor userCursor = db.rawQuery("SELECT user_id FROM User WHERE name = ?", new String[]{userName});
        if (userCursor.moveToFirst()) {
            String userId = userCursor.getString(userCursor.getColumnIndexOrThrow("user_id"));
            userCursor.close();

            Cursor cursor = db.rawQuery("SELECT * FROM " + ClassInstance.TABLE_CLASSSESSION +
                    " WHERE " + ClassInstance.COLUMN_CLASSSESSION_SESSION_DATE + " = ? AND user_id = ?", new String[]{date, userId});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int sessionId = cursor.getInt(cursor.getColumnIndexOrThrow("session_id"));
                    String classId = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("class_id")));
                    String sessionDate = cursor.getString(cursor.getColumnIndexOrThrow("session_date"));

                    String classTitle = getClassTitleById(db, classId);
                    String time = getClassTimeById(db, classId);

                    ClassInstanceDisplay display = new ClassInstanceDisplay(sessionId, userName, classTitle, sessionDate, time, sessionDate);
                    classInstances.add(display);
                }
                cursor.close();
            }
        } else {
            userCursor.close();
        }

        return classInstances;
    }



    public static List<ClassInstanceDisplay> getClassSessionDisplayListByUser(SQLiteDatabase sqLiteDatabase, String selectedTeacher) {
        List<ClassInstanceDisplay> classInstances = new ArrayList<>();

        Cursor userCursor = sqLiteDatabase.rawQuery("SELECT user_id FROM User WHERE name = ?", new String[]{selectedTeacher});
        if (userCursor.moveToFirst()) {
            String userId = userCursor.getString(userCursor.getColumnIndexOrThrow("user_id"));
            userCursor.close();
            Cursor sessionCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + ClassInstance.TABLE_CLASSSESSION + " WHERE user_id = ?", new String[]{userId});
            if (sessionCursor.moveToFirst()) {
                do {
                    int sessionId = sessionCursor.getInt(sessionCursor.getColumnIndexOrThrow("session_id"));
                    String classId = String.valueOf(sessionCursor.getInt(sessionCursor.getColumnIndexOrThrow("class_id")));
                    String sessionDate = sessionCursor.getString(sessionCursor.getColumnIndexOrThrow("session_date"));

                    String classTitle = getClassTitleById(sqLiteDatabase, classId);
                    String time = getClassTimeById(sqLiteDatabase, classId);

                    ClassInstanceDisplay display = new ClassInstanceDisplay(sessionId, selectedTeacher, classTitle, sessionDate, time, sessionDate);
                    classInstances.add(display);

                } while (sessionCursor.moveToNext());
            }
            sessionCursor.close();
        } else {
            userCursor.close();
        }
        return classInstances;
    }


}
