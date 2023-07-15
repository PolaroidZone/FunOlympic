package com.creapptors.funolympic.model;

public class Video {
    String vid;
    String eid;
    String title;
    String description;
    String thumbNail;

    public Video() {
    }

    public Video(String vid, String eid, String title, String description) {
        this.vid = vid;
        this.eid = eid;
        this.title = title;
        this.description = description;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }
}
