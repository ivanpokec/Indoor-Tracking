package com.example.dbaccess;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Paula on 19.12.2016..
 */

public class HistoryModel {

    String name;
    String time;
    String description;
    String user;

    String datum;
    String vrijeme;


    public HistoryModel(String description, String roomName, String time, String user) {
        this.name = roomName;
        this.time = time;
        this.description = description;
        this.user = user;
        separateDateTime();
    }

    public String getRoomName() {
        return name;
    }

    public void setRoomName(String name) {
        this.name = name;
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
        return user;
    }

    public void setUserNameLastName(String user) {
        this.user = user;
    }

    public String getDatum() {
        return datum;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    private void separateDateTime() {
        String time = this.getTime();
        String[] splittedTime = time.split(" ");
        this.datum = splittedTime[0];
        this.vrijeme = splittedTime[1];
    }
}
