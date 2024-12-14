package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import database.DatabaseConnection;

public class Wishlist {

	private String wishlistID;
	private String itemID;
	private String userID;
	
	
	// Constructor: Used to create the Object of the 'Wishlist' Class
	public Wishlist(String wishlistID, String itemID, String userID) {
		super();
		this.wishlistID = wishlistID;
		this.itemID = itemID;
		this.userID = userID;
	}

	// Getter & Setter: Used to get and set the attributes inside of the wishlist
	public String getWishlistID() {
		return wishlistID;
	}

	public void setWishlistID(String wishlistID) {
		this.wishlistID = wishlistID;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public static List<Wishlist> ViewWishlist(String userID) {
		// TODO: View Logic
		List<Wishlist> wishlists = new ArrayList<>();
		
		DatabaseConnection db = DatabaseConnection.getInstance();
		
		String query = "SELECT * FROM wishlist WHERE User_id = ?;";
		
		PreparedStatement ps = db.preparedStatementWithGeneratedKeys(query);
		try {
			ps.setString(1, userID);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Wishlist wl = new Wishlist(rs.getString(1), rs.getString(2), rs.getString(3));
				wishlists.add(wl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return wishlists;
	}
	
	private static String createID() {
		Random rand = new Random();
		return String.format("WL%05d", rand.nextInt(100000));
	}
	
	public static Wishlist AddWishlist(String itemID, String userID) {
		// TODO: Add Logic
		DatabaseConnection db = DatabaseConnection.getInstance();
		
		String query = "INSERT INTO wishlist VALUES (?, ?, ?);";
		
		PreparedStatement ps = db.preparedStatementWithGeneratedKeys(query);
		
		Wishlist wishlist = new Wishlist(createID(), itemID, userID);
		
		try {
			ps.setString(1, wishlist.getWishlistID());
			ps.setString(2, itemID);
			ps.setString(3, userID);
			
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return wishlist;
	}
	
	public static boolean RemoveWishlist(String wishlistID) {
		// TODO: Remove Logic
		DatabaseConnection db = DatabaseConnection.getInstance();
		
		String query = "DELETE FROM wishlist WHERE Wishlist_id = ?;";
		
		PreparedStatement ps = db.preparedStatementWithGeneratedKeys(query);
		
		try {
			ps.setString(1, wishlistID);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
