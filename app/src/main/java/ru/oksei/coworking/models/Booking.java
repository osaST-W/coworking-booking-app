package ru.oksei.coworking.models;

import java.io.Serializable;

public class Booking implements Serializable {
    private long id;
    private long workspaceId;
    private String workspaceName;
    private String category;
    private String bookingDate; // YYYY-MM-DD
    private String startTime;   // HH:00
    private int durationHours;
    private double totalPrice;
    private String status;      // "CONFIRMED", "CANCELLED", "COMPLETED"
    private String qrCode;      // pass data string
    private String createdAt;

    public Booking(long id, long workspaceId, String workspaceName, String category,
                   String bookingDate, String startTime, int durationHours,
                   double totalPrice, String status, String qrCode, String createdAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
        this.category = category;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.durationHours = durationHours;
        this.totalPrice = totalPrice;
        this.status = status;
        this.qrCode = qrCode;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public long getWorkspaceId() { return workspaceId; }
    public String getWorkspaceName() { return workspaceName; }
    public String getCategory() { return category; }
    public String getBookingDate() { return bookingDate; }
    public String getStartTime() { return startTime; }
    public int getDurationHours() { return durationHours; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getQrCode() { return qrCode; }
    public String getCreatedAt() { return createdAt; }

    public void setStatus(String status) { this.status = status; }
}
