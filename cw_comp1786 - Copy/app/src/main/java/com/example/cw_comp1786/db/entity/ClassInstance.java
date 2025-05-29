package com.example.cw_comp1786.db.entity;

import java.util.Date;

public class ClassInstance {


    public static final String TABLE_CLASSSESSION = "ClassInstance";
    public static final String COLUMN_CLASSSESSION_ID = "session_id";
    public static final String COLUMN_CLASSSESSION_USER_ID = "user_id";
    public static final String COLUMN_CLASSSESSION_CLASS_ID = "class_id";
    public static final String COLUMN_CLASSSESSION_SESSION_DATE = "session_date";
    public static final String COLUMN_CLASSSESSION_COMMENTS = "comments";


    private int instanceId;
    private String classId;
    private Date sessionDate;
    private String userId;
    private String comments;

    public ClassInstance() {
    }

    public ClassInstance(int instanceId, String classId, Date sessionDate, String userId, String comments) {
        this.instanceId = instanceId;
        this.classId = classId;
        this.sessionDate = sessionDate;
        this.userId = userId;
        this.comments = comments;
    }

    // Getters và Setters
    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}