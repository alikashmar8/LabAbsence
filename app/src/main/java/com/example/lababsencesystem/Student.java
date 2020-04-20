package com.example.lababsencesystem;

public class Student extends User {
    public Student(String name, String email, String username, String password, int fileNumber,String type) {
        super(name, email, username, password, fileNumber,type);
    }

    public Student() {
        super();
    }
}
