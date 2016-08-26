package com.example.android.paktw.model;

/**
 * Created by Hafizh on 02/12/2015.
 */

public class Disease {
    int id_disease;
    String name;
    String penyebab_disease;
    String gejala_disease;
    String pengendalian;

    public Disease() {
    }

    public Disease(int id_disease, String name, String penyebab_disease, String gejala_disease) {
        this.id_disease = id_disease;
        this.name = name;
        this.penyebab_disease = penyebab_disease;
        this.gejala_disease = gejala_disease;
    }

    public int getId_disease() {
        return id_disease;
    }

    public void setId_disease(int id_disease) {
        this.id_disease = id_disease;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPenyebab_disease() {
        return penyebab_disease;
    }

    public void setPenyebab_disease(String penyebab_disease) {
        this.penyebab_disease = penyebab_disease;
    }

    public String getGejala_disease() {
        return gejala_disease;
    }

    public void setGejala_disease(String gejala_disease) {
        this.gejala_disease = gejala_disease;
    }

    public String getPengendalian() {
        return pengendalian;
    }

    public void setPengendalian(String pengendalian) {
        this.pengendalian = pengendalian;
    }
}
