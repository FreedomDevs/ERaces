package dev.elysium.eraces.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static dev.elysium.eraces.ERaces.logger;

public class SqliteDatabase {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void connect(String path) {
        try {
            // Путь к файлу базы
            String url = "jdbc:sqlite:"+path;
            connection = DriverManager.getConnection(url);
            logger().info("SQLite connected!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
