package model;

public class Items {
    private String itemID;
    private String itemName;
    private String itemSize;
    private String itemPrice;
    private String itemCategory;
    private String itemStatus;
    private String itemWishlist;
    private String itemOfferStatus;

    
    public Items(String itemID, String itemName, String itemSize, String itemPrice, String itemCategory,
            String itemStatus, String itemWishlist, String itemOfferStatus) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemSize = itemSize;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.itemStatus = itemStatus;
        this.itemWishlist = itemWishlist;
        this.itemOfferStatus = itemOfferStatus;
    }


    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemWishlist() {
        return itemWishlist;
    }

    public void setItemWishlist(String itemWishlist) {
        this.itemWishlist = itemWishlist;
    }

    public String getItemOfferStatus() {
        return itemOfferStatus;
    }

    public void setItemOfferStatus(String itemOfferStatus) {
        this.itemOfferStatus = itemOfferStatus;
    }

    
}
