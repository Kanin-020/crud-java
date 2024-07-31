package org.example.system.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/crud";
    private static final String USER = "root";

    private DatabaseConnection(){
        throw new IllegalStateException();
    }

    public static Connection getConnection() throws SQLException {

        Dotenv dotenv = Dotenv.load();

        final String PASSWORD = dotenv.get("PASSWORD");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
