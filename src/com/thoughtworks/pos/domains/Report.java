package com.thoughtworks.pos.domains;

import java.util.List;

/**
 * Created by Administrator on 2014/12/31.
 */
public class Report{
    private List<ItemGroup> itemGroupies;
    private User user;

    public Report(List<ItemGroup> itemGroupies){
        this.itemGroupies = itemGroupies;
        this.user = null;
    }
    public Report(List<ItemGroup> itemGroupies,User user){
        this.itemGroupies = itemGroupies;
        this.user = user;
    }

    public List<ItemGroup> getItemGroupies() {
        return itemGroupies;
    }

    public boolean hasPromotion(){
        boolean result = false;
        for (ItemGroup itemGroup : itemGroupies)
            result |= itemGroup.groupPromotion();
        return result;
    }

    public User getUser(){
        return user;
    }

    public double getTotal(){
        double result = 0.00;
        for (ItemGroup itemGroup : itemGroupies) {
            itemGroup.setVip(user!=null?user.isisVip():false);
            result += itemGroup.subTotal();
        }
        return result;
    }

    public double getSaving(){
        double result = 0.00;
        for (ItemGroup itemGroup : itemGroupies)
            result += itemGroup.saving();
        return result;
    }
}
