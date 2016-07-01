package com.thoughtworks.pos.bin;

import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.domains.Pos;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.services.services.InputParser;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2015/1/2.
 */
public class PosCLI {
    public static void main (String args[]) throws IOException, EmptyShoppingCartException {
        switch(args.length)
        {
            case 1: {
                InputParser inputParser = new InputParser(new File(args[0]));
                ShoppingChart shoppingChart = inputParser.Parser();
                Pos pos = new Pos();
                String shoppingList = pos.getShoppingList(shoppingChart);
                System.out.print(shoppingList);
                break;
            }
            case 2: {
                InputParser inputParser = new InputParser(new File(args[0]), new File(args[1]));
                ShoppingChart shoppingChart = inputParser.Parser();

                Pos pos = new Pos();
                String shoppingList = pos.getShoppingList(shoppingChart);
                System.out.print(shoppingList);
                break;
            }
            case 3:{
                InputParser inputParser = new InputParser(new File(args[0]), new File(args[1]),new File("users.json"));
                ShoppingChart shoppingChart = inputParser.Parser();

                Pos pos = new Pos();
                pos.setShowTime(true);
                String shoppingList = pos.getShoppingList(shoppingChart);
                System.out.print(shoppingList);
                break;
            }
        }
    }
}
