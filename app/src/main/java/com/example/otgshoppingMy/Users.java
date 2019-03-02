package com.example.otgshoppingMy;

public class Users {
    String id;
    String imageURL;
    String username;
    Long phoneNumber;

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public Users(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Users(String id, String imageURL, String username,Long phoneNumber) {
        this.id = id;
        this.imageURL = imageURL;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }
}
