package com.thoughtworks.pos.services.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.ShoppingChart;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * This Part is Writen By Ivan For Requirement 1 and 2
 */

public class InputParser{
    private File itemsFile;
    private final ObjectMapper objectMapper;

    /*
    Init Json Parser
     */
    public InputParser(File itemsFile) {
        this.itemsFile = itemsFile;
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }
    public ShoppingChart parser() throws IOException {
        return BuildShoppingChart(getItems());
    }
    private ShoppingChart BuildShoppingChart(ArrayList<Item> items) {
        ShoppingChart shoppingChart = new ShoppingChart();
        for (Item item : items) {
            shoppingChart.add(item);
        }
        return shoppingChart;
    }

    private ArrayList<Item> getItems() throws IOException {
        String itemsIndexStr = FileUtils.readFileToString(itemsFile);
        TypeReference<ArrayList<Item>> typeRef = new TypeReference<ArrayList<Item>>() {};
        return objectMapper.readValue(itemsIndexStr, typeRef);
    }
}