package com.example.lababsencesystem;

public class Course {
    String name,code;
    int credits,doctor;

    public Course(String name, String code, int credits,int doctor) {
        this.name = name;
        this.code = code;
        this.credits = credits;
        this.doctor = doctor;
    }

    public Course() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", credits=" + credits +
                ", doctor=" + doctor +
                '}';
    }
}
