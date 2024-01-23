package com.quran.khalil;

public class User {
    public String username, email, password, mobile,image,onSignlID;

    public User() {
    }

    public User(String name, String password, String email, String mobile) {
        this.username = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
    }

}
