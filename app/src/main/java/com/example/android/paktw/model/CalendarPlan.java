package com.example.android.paktw.model;

/**
 * Created by Hafizh on 27/10/2015.
 */
public class CalendarPlan {
    int id_calendar;
    String date_start;
    String status;
    String username;

    //constructor
    public CalendarPlan() {
    }

    public CalendarPlan(int id_calendar, String date_start, String status, int count_day) {
        this.id_calendar = id_calendar;
        this.date_start = date_start;
        this.status = status;
    }

    //setter
    public void setId_calendar(int id_calendar) {
        this.id_calendar = id_calendar;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    //getter

    public int getId_calendar() {
        return id_calendar;
    }

    public String getDate_start() {
        return date_start;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }
}
