package com.example.cw_comp1786.db.entity;

public class Booking {

    // Table and Column Names
    public static final String TABLE_BOOKING = "Booking";
    public static final String COLUMN_BOOKING_ID = "booking_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_INSTANCE_ID = "instance_id";
    public static final String COLUMN_CONTACT_EMAIL = "contact_email";
    public static final String COLUMN_STATUS = "status";

    // Attributes
    private String bookingId;
    private String userId;
    private String instanceId;
    private String contactEmail;
    private String status;

    public Booking() {}

    public Booking(String bookingId, String userId, String instanceId, String contactEmail, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.instanceId = instanceId;
        this.contactEmail = contactEmail;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
