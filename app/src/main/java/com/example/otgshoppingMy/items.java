package com.example.otgshoppingMy;

public class items {
    private String itemName  ;
    private Long itemPrice, itemQuantity;
    public items(String itemName,Long itemPrice, Long itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public Long getItemPrice() {
        return itemPrice;
    }

    public Long getItemQuantity() {
        return itemQuantity;
    }
}
