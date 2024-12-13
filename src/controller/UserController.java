package controller;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import database.DatabaseConnection;


public class UserController {

    private DatabaseConnection db = DatabaseConnection.getInstance();

    public boolean isUsernameUnique(String username) {
        String query = "SELECT COUNT(*) FROM user WHERE username = ?";
        try {
            PreparedStatement ps = db.preparedStatementWithGeneratedKeys(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private String generateId() {
		Random random = new Random();
        int randomNumber1 = random.nextInt(10);
        int randomNumber2 = random.nextInt(10);
        int randomNumber3 = random.nextInt(10);
        
        String randomID = "US" + randomNumber1 + randomNumber2 + randomNumber3;
        return randomID;
	}

    public String registerUser(String username, String password, String phoneNumber, String address, String role) {
        // Validate input
        if (username.isEmpty() || username.length() < 3) return "Username tidak valid!";
        if (!isUsernameUnique(username)) return "Username sudah digunakan!";
        if (password.isEmpty() || password.length() < 8 || !password.matches(".*[!@#$%^&*].*"))
            return "Password tidak valid!";
        if (!phoneNumber.matches("^\\+62\\d{9,}$")) return "Nomor telepon tidak valid!";
        if (address.isEmpty()) return "Alamat tidak boleh kosong!";
        if (!role.equals("Buyer") && !role.equals("Seller")) return "Role tidak valid!";

        String query = "INSERT INTO user (username, password, phoneNumber, address, role) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = db.preparedStatementWithGeneratedKeys(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, phoneNumber);
            ps.setString(4, address);
            ps.setString(5, role);
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // Retrieve the generated id
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1); // Get the auto-generated ID
                    System.out.println("Generated ID: " + generatedId);
                    return "Registrasi berhasil dengan ID: " + generatedId;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Terjadi kesalahan pada database!";
        }
    }

    
    public boolean loginUser(String username, String password) {
        // Login untuk admin menggunakan kredensial khusus
        if (username.equals("admin") && password.equals("admin")) {
            return true; // Admin login berhasil
        }

        // Cek apakah username dan password cocok dengan yang ada di database
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = db.preparedStatementWithGeneratedKeys(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true; // Login berhasil jika ada hasil
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Jika tidak ditemukan, login gagal
    }

    // Mendapatkan ID user dari database berdasarkan username
    public String getUserId(String username) {
        String query = "SELECT id FROM user WHERE username = ?";
        try {
            PreparedStatement ps = db.preparedStatementWithGeneratedKeys(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("id"); // Mengembalikan ID user
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Mendapatkan role user berdasarkan username
    public String getUserRole(String username) {
        String query = "SELECT role FROM user WHERE username = ?";
        try {
            PreparedStatement ps = db.preparedStatementWithGeneratedKeys(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("role"); // Mengembalikan role user
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

