package com.forum.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:forum.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }

    private static void initDB() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS topics (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, user_id INTEGER, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS posts (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, topic_id INTEGER, user_id INTEGER, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
            
            // Insert dummy users if not exists
            stmt.execute("INSERT OR IGNORE INTO users (username, password) VALUES ('admin', 'admin')");
            stmt.execute("INSERT OR IGNORE INTO users (username, password) VALUES ('user', 'user')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}