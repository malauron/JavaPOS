package com.JavaPOS.Utilities;

import com.JavaPOS.DataModels.User;

public class CurrentUser extends User {
    private static CurrentUser INSTANCE = new CurrentUser();
    private CurrentUser() {}
    public static CurrentUser getInstance() {
        return INSTANCE;
    }
}
