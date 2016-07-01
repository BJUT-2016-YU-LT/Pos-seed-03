package com.thoughtworks.pos.services.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.scene.paint.GradientUtils;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.domains.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * This Part is Writen By Ivan For Requirement 1 and 2
 */

public class InputParser{
    private File itemsFile;
    private File indexFile;
    private File userFile;
    private final ObjectMapper objectMapper;
    private int ParserType;
    /*
    Init Json Parser
     */
    public InputParser(File itemsFile) {
        this.itemsFile = itemsFile;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        ParserType = 1;
    }

    public InputParser(File indexFile, File itemsFile) {
        this.indexFile = indexFile;
        this.itemsFile = itemsFile;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        ParserType = 2;
    }

    public InputParser(File indexFile, File itemsFile,File userFile) {
        this.indexFile = indexFile;
        this.itemsFile = itemsFile;
        this.userFile = userFile;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        ParserType = 3;
    }

    public ShoppingChart Parser() throws IOException {
        if(ParserType == 1)
            return BuildShoppingChart(getItems());
        else if(ParserType == 2)
            return BuildShoppingChart(getBoughtItemBarCodes(), getItemIndexes());
        else if(ParserType == 3)
            return BuildShoppingChart(getItemList(), getItemIndexes(),getUsers());
        else
            return null;
    }

    private ShoppingChart BuildShoppingChart(ArrayList<Item> items) {
        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item item : items) {
            shoppingChart.add(item);
        }
        return shoppingChart;
    }

    /*Add for Requirement5*/
    private ShoppingChart BuildShoppingChart(ItemList itemList, HashMap<String, Item> itemIndexes,HashMap<String, User> users) {
        User user = users.get(itemList.getUser());
        if(user!=null)
            user.setId(itemList.getUser());
        if(user==null)
            user = new User(itemList.getUser(),itemList.getUser(),false);
        ShoppingChart shoppingChart = new ShoppingChart(user);
        for (String barcode : itemList.getItems()) {
            Item mappedItem = itemIndexes.get(barcode);
            if(mappedItem == null){
                continue;
            }
            Item item = new Item(barcode, mappedItem.getName(), mappedItem.getUnit(), mappedItem.getPrice(), mappedItem.getDiscount(),mappedItem.getPromotion(),mappedItem.getVipDiscount());
            shoppingChart.add(item);
        }
        return shoppingChart;
    }

    /*Add for Requirement3*/
    private ShoppingChart BuildShoppingChart(String[] barCodes, HashMap<String, Item> itemIndexes) {
        ShoppingChart shoppingChart = new ShoppingChart();
        for (String barcode : barCodes) {
            Item mappedItem = itemIndexes.get(barcode);
            Item item = new Item(barcode, mappedItem.getName(), mappedItem.getUnit(), mappedItem.getPrice(), mappedItem.getDiscount(),mappedItem.getPromotion());
            shoppingChart.add(item);
        }
        return shoppingChart;
    }

    private ArrayList<Item> getItems() throws IOException {
        String itemsIndexStr = FileUtils.readFileToString(itemsFile);
        TypeReference<ArrayList<Item>> typeRef = new TypeReference<ArrayList<Item>>() {};
        return objectMapper.readValue(itemsIndexStr, typeRef);
    }

    private String[] getBoughtItemBarCodes() throws IOException {
        String itemsStr = FileUtils.readFileToString(itemsFile);
        return objectMapper.readValue(itemsStr, String[].class);
    }

    private HashMap<String, Item> getItemIndexes() throws IOException {
        String itemsIndexStr = FileUtils.readFileToString(indexFile);
        TypeReference<HashMap<String,Item>> typeRef = new TypeReference<HashMap<String,Item>>() {};
        return objectMapper.readValue(itemsIndexStr, typeRef);
    }

    public class ItemList{
        private String user;
        private ArrayList<String> items;

        public ItemList(){

        }

        public ItemList(String user,ArrayList<String> items){
            setUser(user);
            setItems(items);
        }

        public String getUser(){
            return user;
        }

        public  void setUser(String user){
            this.user = user;
        }

        public ArrayList<String> getItems(){
            return items;
        }

        public void setItems(ArrayList<String> items){
            this.items = items;
        }
    }

    private ItemList getItemList() throws IOException {
        String itemsListStr = FileUtils.readFileToString(itemsFile);
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
        HashMap <String,Object> map = objectMapper.readValue(itemsListStr, typeRef);
        String name = (String)map.get("user");
        ArrayList<String> items = (ArrayList<String>)map.get("items");
        ItemList itemList = new ItemList(name,items);
        return itemList;
    }

    private HashMap<String, User> getUsers() throws IOException {
        String usersStr = FileUtils.readFileToString(userFile);
        TypeReference<HashMap<String,User>> typeRef = new TypeReference<HashMap<String,User>>() {};
        return objectMapper.readValue(usersStr, typeRef);
    }
}