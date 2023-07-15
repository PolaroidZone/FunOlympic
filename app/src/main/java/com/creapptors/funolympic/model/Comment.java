package com.creapptors.funolympic.model;

public class Comment {
    String cid;
    String uid;
    String eid;
    String details;

    public Comment() {
    }

    public Comment(String cid, String uid, String eid, String details) {
        this.cid = cid;
        this.uid = uid;
        this.eid = eid;
        this.details = details;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
