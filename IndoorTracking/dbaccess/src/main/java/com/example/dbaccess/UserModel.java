package com.example.dbaccess;

/**
 * Created by Paula on 14.11.2016..
 */

public class UserModel {
    int Id;
    String userName;
    String passWord;
    String name;
    String locationName;
    int locationId;

    public UserModel() {

    }

    public int getId() {
        return Id;
    }

    public void setLocationId(int locationId) {this.locationId = locationId;}

    public int getLocationId() {return locationId;}

    public void setId(int id) {
        this.Id = id;
    }

    public void setOdjel(String locationName) {this.locationName = locationName;}

    public String getOdjel() { return locationName; }

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
}
