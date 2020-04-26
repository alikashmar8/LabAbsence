package com.example.lababsencesystem;

import java.io.Serializable;

public class Lab implements Serializable {
    String course, date, time, id;
    int doctor;


    public Lab(String course, String date, String time, int doctor) {
        this.course = course;
        this.date = date;
        this.time = time;
        this.doctor = doctor;
    }

    public Lab() {
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Lab{" +
                "course='" + course + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", doctor=" + doctor +
                '}';
    }
}
