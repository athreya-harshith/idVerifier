package com.example.idverifier;

public class gatePass
{
    //implementation pending

    public String gpassId;

    public String getIssuedTime() {
        return issuedTime;
    }

    public void setIssuedTime(String issuedTime) {
        this.issuedTime = issuedTime;
    }

    public String issuedTime;// its url to the qr code
    public String uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    public int validity;

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String email;
    public String toDestination;
    public String reason;
    public String travelDate;
    public gatePass() {

    }

    public gatePass(String uid, String name,String email,String toDestination, String reason, String travelDate) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.toDestination = toDestination;
        this.reason = reason;
        this.travelDate = travelDate;
    }

    public gatePass(String uid, String name,String email,String toDestination, String reason, String travelDate,String issueDate) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.toDestination = toDestination;
        this.reason = reason;
        this.travelDate = travelDate;
        this.issuedTime = issueDate;
    }

    public String getGpassId() {
        return gpassId;
    }

    public void setGpassId(String gpassId) {
        this.gpassId = gpassId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToDestination() {
        return toDestination;
    }

    public void setToDestination(String toDestination) {
        this.toDestination = toDestination;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }
}
