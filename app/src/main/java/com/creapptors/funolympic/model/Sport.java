package com.creapptors.funolympic.model;

public class Sport {
    String sportId;
    String title;

    public Sport() {
    }

    public Sport(String sportId, String title) {
        this.sportId = sportId;
        this.title = title;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
