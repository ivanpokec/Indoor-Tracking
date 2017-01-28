package com.example.dbaccess;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Paula on 19.12.2016..
 */

public class HistoryModel {

    String date;
    String location;
    String time;
    String user;

    String datum;
    String vrijeme;


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

    public String getDatum() {
        return datum;
    }

    public String getVrijeme() {
        return vrijeme;
    }


    public void separateDateTime() {
        String time = this.getDate();
        String[] splittedTime = time.split(" ");
        this.datum = splittedTime[0];
        this.vrijeme = splittedTime[1];
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

}
