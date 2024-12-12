package controller;

import database.DatabaseConnection;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class UserController {

    private DatabaseConnection db = DatabaseConnection.getInstance();

    // Fungsi untuk membuat ID unik
    private String generateId() {
        Random random = new Random();
        int randomNumber1 = random.nextInt(10);
        int randomNumber2 = random.nextInt(10);
        int randomNumber3 = random.nextInt(10);
        return "US" + randomNumber1 + randomNumber2 + randomNumber3;
    }

    // Fungsi untuk memeriksa apakah username unik
    public boolean isUsernameUnique(String username) {
        String query = "SELECT COUNT(*) AS count FROM user WHERE Username = '" + username + "'";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fungsi untuk mendaftarkan pengguna baru
    public String registerUser(String username, String password, String phoneNumber, String address, String role) {
        // Validasi input
        if (username.isEmpty() || username.length() < 3) return "Username tidak valid!";
        if (!isUsernameUnique(username)) return "Username sudah digunakan!";
        if (password.isEmpty() || password.length() < 8 || !password.matches(".*[!@#$%^&*].*"))
            return "Password tidak valid!";
        if (!phoneNumber.matches("^\\+62\\d{9,}$")) return "Nomor telepon tidak valid!";
        if (address.isEmpty()) return "Alamat tidak boleh kosong!";
        if (!role.equals("Buyer") && !role.equals("Seller")) return "Role tidak valid!";

        // Buat ID pengguna
        String userId = generateId();

        // Query untuk menyisipkan data ke dalam tabel
        String query = String.format(
                "INSERT INTO user (User_id, Username, Password, Phone_Number, Address, Role) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
                userId, username, password, phoneNumber, address, role
        );

        try (Statement stmt = db.getConnection().createStatement()) {
            int affectedRows = stmt.executeUpdate(query);
            if (affectedRows > 0) {
                return "Registrasi berhasil dengan ID: " + userId;
            } else {
                return "Registrasi gagal!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Terjadi kesalahan pada database!";
        }
    }

    // Fungsi untuk login pengguna
    public boolean loginUser(String username, String password) {
        // Login untuk admin menggunakan kredensial khusus
        if (username.equals("admin") && password.equals("admin")) {
            return true; // Admin login berhasil
        }

        // Query untuk memeriksa username dan password
        String query = String.format(
                "SELECT * FROM user WHERE Username = '%s' AND Password = '%s'",
                username, password
        );

        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next(); // Jika ada hasil, login berhasil
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Login gagal jika tidak ada hasil
    }



}
