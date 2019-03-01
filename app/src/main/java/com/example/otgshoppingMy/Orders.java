package com.example.otgshoppingMy;

import java.util.ArrayList;

public class Orders {

    public ArrayList<items> list;
    public  String buyer;
    public Orders( ArrayList<com.example.otgshoppingMy.items> list, String buyer) {

        this.list = list;
        this.buyer = buyer;
    }

    public Orders(){

    }




    public ArrayList<com.example.otgshoppingMy.items> getList() {
        return list;
    }

    public void setList(ArrayList<com.example.otgshoppingMy.items> list) {
        this.list = list;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
}
