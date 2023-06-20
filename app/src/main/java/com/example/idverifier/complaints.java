package com.example.idverifier;

public class complaints
{
    String complaintTitle,complaintText,complainedBy,uid;

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public complaints() {
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public String getComplainedBy() {
        return complainedBy;
    }

    public void setComplainedBy(String complainedBy) {
        this.complainedBy = complainedBy;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public complaints(String complaintTitle, String complaintText, String complainedBy, String uid) {
        this.complaintTitle = complaintTitle;
        this.complaintText = complaintText;
        this.complainedBy = complainedBy;
        this.uid = uid;
    }
}
