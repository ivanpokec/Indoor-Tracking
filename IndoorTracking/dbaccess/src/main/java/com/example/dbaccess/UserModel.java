package com.example.dbaccess;

/**
 * Created by Paula on 14.11.2016..
 */

public class UserModel {
    private int userId;
    private String userName;
    private String passWord;
    private String name;
    private int locationId;
    private String locationName;
    public String locationCategory;
    private int currentLocationId;
    private String currentLocationName;
    private String currentLocationCategory;
    private String currentLocationDescription;


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return passWord;
    }

    public void setPassword(String password) {
        this.passWord = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocationId(int locationId) {this.locationId = locationId;}

    public int getLocationId() {return locationId;}

    public void setLocationName(String locationName) {this.locationName = locationName;}

    public String getLocationName() { return locationName; }

    public String getCurrentLocationName() {return currentLocationName;}

    public int getCurrentLocationId() {
        return currentLocationId;
    }

    public void setCurrentLocationName(String currentLocationName) {
        this.currentLocationName = currentLocationName;
    }

    public String getCurrentLocationCategory() {
        return currentLocationCategory;
    }

    public String getCurrentLocationDescription() {
        return currentLocationDescription;
    }


}
