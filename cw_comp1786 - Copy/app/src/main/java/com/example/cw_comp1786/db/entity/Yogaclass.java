package com.example.cw_comp1786.db.entity;

import java.util.Date;

public class Yogaclass {
    public static final String TABLE_YOGA_CLASS = "YogaClass";
    public static final String COLUMN_CLASS_ID = "class_id";
    public static final String COLUMN_CLASS_TITLE = "class_title";
    public static final String COLUMN_CLASS_DAY = "day";
    public static final String COLUMN_CLASS_TIME = "time";
    public static final String COLUMN_CLASS_CAPACITY = "capacity";
    public static final String COLUMN_CLASS_DURATION = "duration";
    public static final String COLUMN_CLASS_PRICE = "price";
    public static final String COLUMN_CLASS_TYPE = "class_type";
    public static final String COLUMN_CLASS_DESCRIPTION = "description";
    public static final String COLUMN_CLASS_CREATED_AT = "created_at";


    private String classid;
    private String classtitle;
    private String day;
    private String time;
    private int capacity;
    private int duration;
    private double price;
    private String classtype;
    private String description;
    private Date createat;

    public Yogaclass() {
    }
    public Yogaclass(String classid, String classtitle, String day, String time, int capacity, int duration, double price, String classtype, String description, Date createat) {
        this.classid = classid;
        this.classtitle = classtitle;
        this.day = day;
        this.time = time;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
        this.classtype = classtype;
        this.description = description;
        this.createat = createat;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClasstitle() {
        return classtitle;
    }

    public void setClasstitle(String classtitle) {
        this.classtitle = classtitle;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createat) {
        this.createat = createat;
    }
}
