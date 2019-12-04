package com.artbycode.spacelet.Data;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Space {
    private String spaceId;
    private String status;
    private String name;
    private String description;
    private String price;
    private String timeOccupied;
    private String imgURL;

    public Space(){}

    public Space(String spaceId,String status,String name,String description,String price,String timeOccupied,String imgURL){
        this.spaceId = spaceId;
        this.status = status;
        this.name = name;
        this.description = description;
        this.price = price;
        this.timeOccupied = timeOccupied;
        this.imgURL = imgURL;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimeOccupied() {
        return timeOccupied;
    }

    public void setTimeOccupied(String timeOccupied) {
        this.timeOccupied = timeOccupied;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
