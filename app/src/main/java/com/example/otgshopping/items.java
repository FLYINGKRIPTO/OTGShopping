package com.example.otgshopping;

public class items {
    private String itemName , itemPrice , itemQuantity ;

    public items(String itemName, String itemPrice, String itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }
}
