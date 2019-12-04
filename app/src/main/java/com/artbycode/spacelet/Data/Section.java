package com.artbycode.spacelet.Data;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Section {
    private String sectionId;
    private String status;
    private String name;
    private String description;
    private String capacity;
    private String address;
    private String imgURL;

    public Section(){}

    public Section(String sectionId, String status, String name, String description, String capacity, String address, String imgURL){
        this.sectionId = sectionId;
        this.status = status;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.address = address;
        this.imgURL = imgURL;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
