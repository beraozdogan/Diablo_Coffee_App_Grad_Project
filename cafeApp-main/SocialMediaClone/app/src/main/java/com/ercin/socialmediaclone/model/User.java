package com.ercin.socialmediaclone.model;

public class User {


    private String userUid;
    private String email;
    private String phoneNumber;
    private String name;
    private String surname;
    private String password;
    private String singUpTime;
    private String point;
    private String profilePhotoUrl;
    private String isAdmin;
    private String hediyeler;


    public User (){

    }

    public User (String email, String phoneNumber, String name, String surname, String password, String singUpTime, String userUid, String point, String profilePhotoUrl, String isAdmin, String hediyeler){
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.setName(name);
        this.setSurname(surname);
        this.setPassword(password);
        this.setSingUpTime(singUpTime);
        this.setUserUid(userUid);
        this.setPoint(point);
        this.setProfilePhotoUrl(profilePhotoUrl);
        this.setIsAdmin(isAdmin);
        this.setHediyeler(hediyeler);
    }

    public String getHediyeler() {
        return hediyeler;
    }

    public void setHediyeler(String hediyeler) {
        this.hediyeler = hediyeler;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSingUpTime() {
        return singUpTime;
    }

    public void setSingUpTime(String singUpTime) {
        this.singUpTime = singUpTime;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
