package com.example.otgshopping;

public class Products {
    private Long barcode;
    private String item;
    private Long price;
    public Products(Long barcode, String item, Long price) {
        this.barcode = barcode;
        this.item = item;
        this.price = price;
    }

    public Products(){

    }
    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode= barcode;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


}
