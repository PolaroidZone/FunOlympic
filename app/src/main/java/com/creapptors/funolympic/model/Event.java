package com.creapptors.funolympic.model;

public class Event {
    String eid;
    String aid;
    String at_id;
    String sid;
    String title;
    String details;
    String date;

    public Event() {
    }

    public Event(String eid, String aid, String at_id, String sid, String title, String details, String date) {
        this.eid = eid;
        this.aid = aid;
        this.at_id = at_id;
        this.sid = sid;
        this.title = title;
        this.details = details;
        this.date = date;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAt_id() {
        return at_id;
    }

    public void setAt_id(String at_id) {
        this.at_id = at_id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
