package com.example.cw_comp1786.db.entity;

public class ClassInstanceDisplay {
    private int instanceId;
    private String userName;
    private String classTitle;
    private String day;
    private String time;
    private String sessionDate;

    public ClassInstanceDisplay() {
    }

    public ClassInstanceDisplay(int instanceId, String userName, String classTitle, String day, String time, String sessionDate) {
        this.instanceId = instanceId;
        this.userName = userName;
        this.classTitle = classTitle;
        this.day = day;
        this.time = time;
        this.sessionDate = sessionDate;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

}
