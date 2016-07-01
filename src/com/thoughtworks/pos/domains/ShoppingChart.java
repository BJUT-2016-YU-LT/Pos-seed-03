package com.thoughtworks.pos.domains;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/12/28.
 */
public class ShoppingChart {
    private ArrayList<Item> items = new ArrayList<Item>();
    private User user;

    public ShoppingChart(){

    }

    public  ShoppingChart(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    public void add(Item item) {
        this.items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
