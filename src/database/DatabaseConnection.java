package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "Project";
    private final String HOST = "localhost:3306";
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    private Connection con;
    private Statement st;
    private static DatabaseConnection db;

    // Singleton pattern untuk mendapatkan instance DatabaseConnection
    public static DatabaseConnection getInstance() {
        if (db == null) {
            db = new DatabaseConnection();
        }
        return db;
    }

    // Constructor privat untuk mencegah instansiasi langsung
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Muat driver MySQL
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metode untuk menjalankan query SELECT
    public ResultSet execQuery(String query) {
        ResultSet rs = null;
        try {
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    // Metode untuk menjalankan query INSERT, UPDATE, DELETE
    public int execUpdate(String query) {
        int affectedRows = 0;
        try {
            affectedRows = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    public Connection getConnection() {
        return con;
    }
}
