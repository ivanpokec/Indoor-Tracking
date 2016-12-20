package com.example.dbaccess;

/**
 * Created by Paula on 20.12.2016..
 */

public class LocationModel {
    int Id;
    String name;
    String macAddress;
    String description;

    public LocationModel(int id, String name, String macAddress, String description) {
        Id = id;
        this.name = name;
        this.macAddress = macAddress;
        this.description = description;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
