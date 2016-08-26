package com.example.android.paktw.model;

/**
 * Created by Hafizh on 05/12/2015.
 */
public class History {
    int id_history;
    int id_calendar;
    String date_start;
    int count_day;
    int id_event;
    String name_event;
    String date;
    String detail_event;

    public History() {
    }

    public History(int id_history, int id_calendar, String date_start, int count_day, int id_event, String name_event, String date, String detail_event) {
        this.id_history = id_history;
        this.id_calendar = id_calendar;
        this.date_start = date_start;
        this.count_day = count_day;
        this.id_event = id_event;
        this.name_event = name_event;
        this.date = date;
        this.detail_event = detail_event;
    }

    public int getId_history() {
        return id_history;
    }

    public void setId_history(int id_history) {
        this.id_history = id_history;
    }

    public int getId_calendar() {
        return id_calendar;
    }

    public void setId_calendar(int id_calendar) {
        this.id_calendar = id_calendar;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public int getCount_day() {
        return count_day;
    }

    public void setCount_day(int count_day) {
        this.count_day = count_day;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public String getName_event() {
        return name_event;
    }

    public void setName_event(String name_event) {
        this.name_event = name_event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail_event() {
        return detail_event;
    }

    public void setDetail_event(String detail_event) {
        this.detail_event = detail_event;
    }
}
