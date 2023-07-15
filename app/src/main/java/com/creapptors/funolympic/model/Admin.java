package com.creapptors.funolympic.model;

public class Admin {
    String uid;
    String username;
    String email;
    String password;

    public Admin() {
    }

    public Admin(String uid, String username, String email, String password) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String gender) {
        this.password = password;
    }
}
