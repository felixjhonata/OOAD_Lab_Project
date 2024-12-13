package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.Alert;

import database.DatabaseConnection;
import model.Items;

public class ItemController {
    private Items newItem;
    private PreparedStatement psUpdate, ps;
    private ResultSet rs;

    private DatabaseConnection db = DatabaseConnection.getInstance();

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
        newItem = new Items(null, name, size, price, category, "Active", "No", "No");

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

    public boolean editItem(String id, String name, String category, String size, String price) {
        // validation for empty fields
        if (name == null || name.trim().isEmpty()) {
            showAlert("Validation Error", "Item Name cannot be empty.");
            return false;
        }
        if (category == null || category.trim().isEmpty()) {
            showAlert("Validation Error", "Item Category cannot be empty.");
            return false;
        }
        if (size == null || size.trim().isEmpty()) {
            showAlert("Validation Error", "Item Size cannot be empty.");
            return false;
        }
        if (price == null || price.trim().isEmpty()) {
            showAlert("Validation Error", "Item Price cannot be empty.");
            return false;
        }

        // validate if the item with provided id exists in the database
        rs = null;
        try {
            String checkQuery = "SELECT * FROM items WHERE ItemID = ?";
            ps = db.preparedStatement(checkQuery, Statement.NO_GENERATED_KEYS);
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (!rs.next()) {
                showAlert("Item Not Found", "Item with ID " + id + " does not exist.");
                return false; // Item ID does not exist
            }

            // update the item if it exists
            String updateQuery = "UPDATE items SET ItemName = ?, ItemCategory = ?, ItemSize = ?, ItemPrice = ? WHERE ItemID = ?";
            psUpdate = db.preparedStatement(updateQuery, Statement.NO_GENERATED_KEYS);

            psUpdate.setString(1, name);
            psUpdate.setString(2, category);
            psUpdate.setString(3, size);
            psUpdate.setString(4, price);
            psUpdate.setString(5, id);

            int rowsUpdated = psUpdate.executeUpdate();

            // check if the update was successful
            if (rowsUpdated > 0) {
                showAlert("Success", "Item with ID " + id + " updated successfully.");
                return true;
            } else {
                showAlert("Error", "Failed to update item.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while updating the item.");
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteItem(String itemID) {
        String query = "DELETE FROM items WHERE ItemID LIKE ?";
        ps = null;

        try {
            ps = db.preparedStatement(query, Statement.NO_GENERATED_KEYS); // Prepare the statement
            ps.setString(1, itemID); // Bind the parameter

            int rowsAffected = ps.executeUpdate(); // Execute the deletion

            if (rowsAffected > 0) {
                showInformation("Success", "Item with ID " + itemID + " was successfully deleted.");
            } else {
                showInformation("No Item Found", "No item with ID " + itemID + " exists in the database.");
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while deleting the item: " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close(); // Close the PreparedStatement
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ResultSet viewItem(int page) throws SQLException {
        String query = "SELECT * FROM items ORDER BY ItemID ASC LIMIT 10 OFFSET ?";
        int offset = (page - 1) * 10; // Calculate the OFFSET for pagination
        ps = null;
        rs = null;

        try {
            // Use the singleton instance of DatabaseConnection to prepare the statement
            ps = db.preparedStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE);
            ps.setInt(1, offset); // Set the OFFSET for the query
            rs = ps.executeQuery();

            // Check if the ResultSet is empty
            if (!rs.isBeforeFirst()) {
                showInformation("No Data", "There is no data available in the database.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error while retrieving items: " + e.getMessage(), e);
        }
        return rs;
    }

    public void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
