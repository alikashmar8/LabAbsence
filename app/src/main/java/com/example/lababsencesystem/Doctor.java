package com.example.lababsencesystem;

import com.example.lababsencesystem.User;

public class Doctor extends User {
    public Doctor(String name, String email, String username, String password, int fileNumber,String type) {
        super(name, email, username, password, fileNumber,type);
    }

    public Doctor() {
    }
}
