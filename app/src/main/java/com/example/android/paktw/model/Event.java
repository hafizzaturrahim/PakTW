package com.example.android.paktw.model;

/**
 * Created by Hafizh on 27/10/2015.
 */
public class Event {
    int id_event;
    String name_event;
    String date_event;
    String detail_event;
    String status;
    int count_day;
    int id_calendar;

    //constructor
    public Event() {
    }

//    public Event(int id_event) {
//        this.id_event = id_event;
//    }
    //
    public Event(int id_event, String name_event, String date_event, String detail_event, String status) {
        this.id_event = id_event;
        this.name_event = name_event;
        this.date_event = date_event;
        this.detail_event = detail_event;
        this.status = status;
    }
    public Event(String name_event, String date_event, String detail_event, String status) {
        this.name_event = name_event;
        this.date_event = date_event;
        this.detail_event = detail_event;
        this.status = status;
    }

    public Event(String name_event, String date_event) {
        this.name_event = name_event;
        this.date_event = date_event;
    }

    //setter
    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public void setName_event(String name_event) {
        this.name_event = name_event;
    }

    public void setDate_event(String date_event) {
        this.date_event = date_event;
    }

    public void setDetail_event(String detail_event) {
        this.detail_event = detail_event;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId_calendar(int id_calendar) {
        this.id_calendar = id_calendar;
    }

    public void setCount_day(int count_day) {
        this.count_day = count_day;
    }

    //getter

    public int getId_event() {
        return id_event;
    }

    public String getName_event() {
        return name_event;
    }

    public String getDate_event() {
        return date_event;
    }

    public String getDetail_event() {
        return detail_event;
    }

    public String getStatus() {
        return status;
    }


    public int getCount_day() {
        return count_day;
    }

    public int getId_calendar() {
        return id_calendar;
    }
}
