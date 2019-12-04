package com.artbycode.spacelet.Data;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String userId;
    private String number; //used to login
    private String password; //used to login
    private String username;
    private String status;
    private String imgURL;

    public User(){}

    public User(String userId,String number,String password,String username,String status,String imgURL){
        this.userId = userId;
        this.number = number;
        this.password = password;
        this.username = username;
        this.status = status;
        this.imgURL = imgURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
