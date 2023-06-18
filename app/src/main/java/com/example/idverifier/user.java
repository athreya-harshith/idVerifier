package com.example.idverifier;

public class user
{
    public static final int IN_CAMPUS = 1,OUT_CAMPUS=0;

    public static final String STUDENT="student",SECURITY = "security",ADMIN="admin";
    private String name;
    private String uid;
    private String email;
    private String mobile;
    private String parentMobile;
    private String profilePic;
    private String id;
    private String branch;
    private String year;

    private int currentStatus;

    private String role;
    private gatePass gPass;
    public  user()
    {
    }
    public user(String name, String uid, String email, String role)
    {

        this.name = name;
        this.uid = uid;
        this.email = email;
        this.mobile = "";
        this.parentMobile="";
        this.profilePic="";
        this.id="";
        this.branch= "";
        this.year="";
        this.currentStatus = user.IN_CAMPUS;
        this.role = role;
        this.gPass = new gatePass();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getParentMobile() {
        return parentMobile;
    }

    public void setParentMobile(String parentMobile) {
        this.parentMobile = parentMobile;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public gatePass getgPass() {
        return gPass;
    }

    public void setgPass(gatePass gPass) {
        this.gPass = gPass;
    }



}
