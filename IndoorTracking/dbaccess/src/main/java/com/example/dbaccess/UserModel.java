package com.example.dbaccess;

/**
 * Created by Paula on 14.11.2016..
 */

public class UserModel {
    public int Id;
    public String userName;
    public String passWord;
    public String name;
    public String locationName;
    public int locationId;
    public String sector;
    public String currentLocarion;

    public UserModel() {

    }

    public UserModel(String name, String locationName, String currentLocation) {
        this.name = name;
        this.locationName=locationName;
        this.currentLocarion =currentLocation;
    }

    public int getId() {
        return Id;
    }

    public void setLocationId(int locationId) {this.locationId = locationId;}

    public int getLocationId() {return locationId;}

    public void setId(int id) {
        this.Id = id;
    }

    public void setLocationName(String locationName) {this.locationName = locationName;}

    public String getLocationName() { return locationName; }

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

    public String getCurrentLocarion() {return currentLocarion;}

    public void setCurrentLocarion(String currentLocarion) {
        this.currentLocarion = currentLocarion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

}
