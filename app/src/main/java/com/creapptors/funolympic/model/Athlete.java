package com.creapptors.funolympic.model;

public class Athlete {

    String uid;
    String username;
    String email;
    String gender;

    public Athlete() {
    }

    public Athlete(String uid, String username, String email, String gender) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.gender = gender;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
