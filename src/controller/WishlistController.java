package controller;

import java.util.List;

import model.Wishlist;

public class WishlistController {

	public WishlistController() {}
	
	public List<Wishlist> ViewWishlist(String userID) {
		return Wishlist.ViewWishlist(userID);
	}
	
	public Wishlist AddWishlist(String itemID, String userID) {
		return Wishlist.AddWishlist(itemID, userID);
	}
	
	public boolean RemoveWishlist(String wishlistID) {
		return Wishlist.RemoveWishlist(wishlistID);
	}
	
}
