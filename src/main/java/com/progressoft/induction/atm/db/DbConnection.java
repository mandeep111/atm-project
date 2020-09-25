package com.progressoft.induction.atm.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    public static Connection get_connection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_db", "root", "admin123");
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }
}
