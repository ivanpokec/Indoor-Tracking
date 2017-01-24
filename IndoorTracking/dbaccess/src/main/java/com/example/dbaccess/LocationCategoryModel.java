package com.example.dbaccess;

/**
 * Created by Zana on 24.1.2017..
 */

public class LocationCategoryModel {

    int Id;
    public String name;
    String macAddress;
    String description;
    String category;

    public String getLocationInCategory() {
        return name;
    }

    public void setLocationName(String locationName) {
        this.name= locationName;
    }


    public LocationCategoryModel(String locationName) {
        this.name = locationName;
    }

}
