package com.thoughtworks.pos.domains;

import java.util.List;

/**
 * Created by Administrator on 2014/12/31.
 */
public class ItemGroup {
    private List<Item> items;

    public ItemGroup(List<Item> items) {
        this.items = items;
    }

    public String groupName() {
        return items.get(0).getName();
    }

    public int groupSize() {
        return items.size();
    }

    public String groupUnit() {
        return items.get(0).getUnit();
    }

    public double groupPrice() {
        return items.get(0).getPrice();
    }

    public boolean groupPromotion() {
        if (items.size() >= 3 && items.get(0).getPromotion()) {
            return true;
        }
        return false;
    }

    public double subTotal() {
        double result = 0.00;
        for (Item item : items)
            result += item.getPrice() * item.getDiscount();
        if (groupPromotion()) {
            result -= items.get(0).getPrice();
        }
        return result;
    }

    public double saving() {
        double result = 0.00;
        for (Item item : items)
            result += item.getPrice() * (1 - item.getDiscount());
        if (groupPromotion()) {
            result += items.get(0).getPrice();
        }
        return result;
    }
}
