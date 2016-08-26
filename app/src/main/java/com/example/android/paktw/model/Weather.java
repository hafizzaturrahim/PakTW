package com.example.android.paktw.model;

/**
 * Created by Hafizh on 28/11/2015.
 */
public class Weather {
    int id_weather;
    String city;
    String date_weather;
    String temperature;
    double wind;
    double humidity;
    String status;
    int id_status;

    public Weather() {
    }


    public int getId_weather() {
        return id_weather;
    }

    public void setId_weather(int id_weather) {
        this.id_weather = id_weather;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate_weather() {
        return date_weather;
    }

    public void setDate_weather(String date_weather) {
        this.date_weather = date_weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId_status() {
        return id_status;
    }

    public void setId_status(int id_status) {
        this.id_status = id_status;
    }
}
