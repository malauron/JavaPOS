package com.JavaPOS.Interfaces;

import com.JavaPOS.DataModels.User;
import com.JavaPOS.Utilities.ExecuteStatus;
import javafx.collections.ObservableList;

public interface IUsers {
    ObservableList<User> getUsers(String param);
    User getUser(String username, String password);
    ExecuteStatus handleUser(User user);
}
