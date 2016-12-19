package com.example.dbaccess;

/**
 * Created by Paula on 19.12.2016..
 */

public class HistoryModel {
    String roomName;
    String time;
    String description;
    String userNameLastName;


    public HistoryModel(String description, String roomName, String time, String userNameLastName) {
        this.roomName = roomName;
        this.time = time;
        this.description = description;
        this.userNameLastName = userNameLastName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserNameLastName() {
        return userNameLastName;
    }

    public void setUserNameLastName(String userNameLastName) {
        this.userNameLastName = userNameLastName;
    }
}
