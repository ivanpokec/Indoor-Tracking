package hr.foi.core;

import hr.foi.dbaccess.UserModel;

/**
 * Created by Paula on 27.12.2016..
 */

public class LoggedUser {
    private static LoggedUser userInstance;

    private static UserModel userModel;


    public  LoggedUser() {

    }

    public static LoggedUser getUser() {
        if (userInstance == null) {
            userInstance = new LoggedUser();
        }
        return userInstance;
    }

    public void setUserModel(UserModel um) {
        userModel = um;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void releaseUserModel() {
        userModel = null;
    }

}
