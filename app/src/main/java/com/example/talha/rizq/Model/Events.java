package com.example.talha.rizq.Model;

public class Events {

    private String eid, description, location, time;
    public Events(){

    }

    public Events(String eid, String description, String location, String time) {
        this.eid = eid;
        this.description = description;
        this.location = location;
        this.time = time;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
