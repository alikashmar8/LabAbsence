package com.example.lababsencesystem;

public class CourseStudent {
    String name;
    int fileNumber;

    public CourseStudent(String name, int fileNumber) {
        this.name = name;
        this.fileNumber = fileNumber;
    }

    public CourseStudent() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    @Override
    public String toString() {
        return fileNumber + " - " + name;
    }
}
