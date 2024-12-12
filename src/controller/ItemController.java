package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.Alert;
import model.Items;
import database.DatabaseConnection;

public class ItemController {
    private Items item;

    private DatabaseConnection db  = DatabaseConnection.getInstance();

    
    public String uploadItem(String name, String category, String size, String price) throws Exception {
        // Input validation
        if (name == null || name.trim().isEmpty() || name.length() < 3) {
            throw new IllegalArgumentException("Item Name must be at least 3 characters long and cannot be empty.");
        }
        if (category == null || category.trim().isEmpty() || category.length() < 3) {
            throw new IllegalArgumentException("Item Category must be at least 3 characters long and cannot be empty.");
        }
        if (size == null || size.trim().isEmpty()) {
            throw new IllegalArgumentException("Item Size cannot be empty.");
        }
        if (price == null || price.trim().isEmpty()) {
            throw new IllegalArgumentException("Item Price cannot be empty.");
        }
        try {
            Double.parseDouble(price); // Ensure price is a valid number
            if (Double.parseDouble(price) <= 0) {
                throw new IllegalArgumentException("Item Price must be greater than 0.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Item Price must be a valid number.");
        }
    
        // Create an Item object to store the new item's data
        Items newItem = new Items(null, name, size, price, category, "Active", "No", "No");
    
        // Insert into database
        String sql = "INSERT INTO items (ItemName, ItemCategory, ItemSize, ItemPrice) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = db.preparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, newItem.getItemName());
            ps.setString(2, newItem.getItemCategory());
            ps.setString(3, newItem.getItemSize());
            ps.setString(4, newItem.getItemPrice());
    
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating item failed, no rows affected.");
            }
    
            // Retrieve the generated ID and set it to the new Item object
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newItem.setItemID(generatedKeys.getString(1)); // Assuming ID is returned as a String
                    return "Item uploaded successfully with ID: " + newItem.getItemID();
                } else {
                    throw new SQLException("Creating item failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Database error during item upload: " + e.getMessage(), e);
        }
    }
    


    
    public void editItem(String id, String name, String category, String size, String price){
        
    }

    public void deleteItem(){

    }

    public void viewItem(){

    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
