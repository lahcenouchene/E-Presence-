package com.example.e_presence;

public class StudentItem {
    private int roll;
    private String name;
    private String status;
    private long sid;

    public int backgroundColor;

    public StudentItem(long sid, int roll, String name, String status) {
        this.roll = roll;
        this.name = name;
        this.sid=sid;
        if (status == null) {
            this.status = "";
        } else {
            this.status = status;
        }
    }


    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getSid() {
        return sid;
    }
}