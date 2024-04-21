package com.example.pablab.JDBC;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnector {

    private static DbConnector instance;
    private static String url;
    private static String user;
    private static String password;
    private static Connection connection;

    private DbConnector() {
    }

    public static DbConnector getInstance() {
        if (instance == null) {
            instance = new DbConnector();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            initializeConnection();
        }
        return connection;
    }

    private void initializeConnection() throws SQLException {
        try {
            FileReader reader = new FileReader("C:\\Users\\Piotr\\Desktop\\PABLab\\PABLab\\src\\main\\resources\\Properties.txt");
            Properties prop = new Properties();
            prop.load(reader);
            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
