package com.example.dbaccess;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Paula on 19.12.2016..
 */

public class HistoryModel {

    private String date;
    private String location;
    private String time;
    private String user;

    public HistoryModel() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public static String convertDate(String date, String format) {
        String convertedDate = "";
        String oldDate = date;
        SimpleDateFormat date1 = new SimpleDateFormat(format);
        SimpleDateFormat date2 = new SimpleDateFormat("yyyyMMdd");
        try {
            Date newDate = date1.parse(oldDate);
            convertedDate = date2.format(newDate);
            Log.i("CONVERT", convertedDate);
        }
        catch (java.text.ParseException ex) {
            Log.i("PARSE", ex.getMessage());
        }

        return convertedDate;
    }

    public String showData(int historyType) {
        switch (historyType) {
            case 0: // FOR HISTORY ALL
                return this.getDate();
            case 1: // FOR HISTORY ALL DETAILS
                return this.getTime() + " " + this.getLocation();
            case 2: // FOR HISTORY BY DATE
                return this.getDate() + " " + this.getTime() + " " + this.getLocation();
            case 3: // FOR HISTORY BY LOCATION
                return this.getDate() + " " + this.getTime();
            default:
                return "";
        }
    }

}
