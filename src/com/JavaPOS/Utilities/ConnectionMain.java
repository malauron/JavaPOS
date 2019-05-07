package com.JavaPOS.Utilities;

import java.sql.SQLException;

public class ConnectionMain extends ConnectionMySQL {
    private static ConnectionMain INSTANCE;
    private ConnectionMain() throws SQLException, ClassNotFoundException {}
    public static ConnectionMain getInstance() throws SQLException, ClassNotFoundException {
        if (INSTANCE == null) {
            synchronized(ConnectionMain.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ConnectionMain();
                }
            }
        }
        return INSTANCE;
    }
}
