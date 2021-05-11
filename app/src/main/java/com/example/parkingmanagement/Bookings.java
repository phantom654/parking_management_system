package com.example.parkingmanagement;

import androidx.core.view.DisplayCutoutCompat;

public class Bookings {

    private String building;
    private String city;
    private String time;
    private String duration;
    private String status;
    private String innoiceId;
    private String userId;
    private String parkingId;
    private String slotId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInnoiceId() {
        return innoiceId;
    }

    public void setInnoiceId(String innoiceId) {
        this.innoiceId = innoiceId;
    }

    public Bookings(String building, String city, String time, String duration, String status, String innoiceId, String userId, String parkingId, String slotId)
    {
        this.building=building;
        this.city=city;
        this.time=time;
        this.duration=duration;
        this.status=status;
        this.innoiceId= innoiceId;
        this.userId=userId;
        this.parkingId=parkingId;
        this.slotId=slotId;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
