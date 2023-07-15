package com.creapptors.funolympic.model;

import com.google.type.Date;

public class funOlympics {
    String oid;
    String aid;
    String eid;
    String title;
    String fo_date;

    public funOlympics() {
    }

    public funOlympics(String title, String date) {
        this.title = title;
        this.fo_date = date;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
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

    public String getFo_date() {
        return fo_date;
    }

    public void setFo_date(String fo_date) {
        this.fo_date = fo_date;
    }
}
