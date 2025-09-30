package dev.elysium.eraces.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteDatabase {
    private Connection connection;

    public void connect(String path) {
        try {
            // Путь к файлу базы
            String url = "jdbc:sqlite:"+path;
            connection = DriverManager.getConnection(url);
            System.out.println("SQLite подключен!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
