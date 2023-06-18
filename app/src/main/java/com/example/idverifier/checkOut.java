package com.example.idverifier;

public class checkOut {
    public static final String ID_MODE="id",GATEPASS_MODE="gatePass";
    public String uid;
    public String name;
    public String mode;
    public String outTime;
    public String outDate;

    public String inTime;
    public String inDate;

    public String outVerifier;
    public String inVerifier;

    public checkOut()
    {

    }

    public checkOut(String uid, String name, String mode, String outTime, String outDate, String outVerifier) {
        this.uid = uid;
        this.name = name;
        this.mode = mode;
        this.outTime = outTime;
        this.outDate = outDate;
        this.outVerifier = outVerifier;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getOutVerifier() {
        return outVerifier;
    }

    public void setOutVerifier(String outVerifier) {
        this.outVerifier = outVerifier;
    }

    public String getInVerifier() {
        return inVerifier;
    }

    public void setInVerifier(String inVerifier) {
        this.inVerifier = inVerifier;
    }
}
