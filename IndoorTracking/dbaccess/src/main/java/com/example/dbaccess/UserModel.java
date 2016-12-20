package com.example.dbaccess;

/**
 * Created by Paula on 14.11.2016..
 */

public class UserModel {
    int Id;
    String userName;
    String passWord;
    String name;

    public UserModel() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
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
}
