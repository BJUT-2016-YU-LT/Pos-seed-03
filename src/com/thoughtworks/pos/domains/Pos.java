package com.thoughtworks.pos.domains;

import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.services.services.OutputParser;
import com.thoughtworks.pos.services.services.ReportDataGenerator;

import java.io.File;
import java.util.*;
import java.text.*;
/**
 * Created by Administrator on 2014/12/28.
 */
public class Pos {
    private boolean isShowTime;
    public String getShoppingList(ShoppingChart shoppingChart) throws EmptyShoppingCartException {

        Report report = new ReportDataGenerator(shoppingChart).generate();

        StringBuilder shoppingListBuilder = new StringBuilder()
                .append("***商店购物清单***\n");
        User user = report.getUser();
        for (ItemGroup itemGroup : report.getItemGroupies()) {
            if(user!=null && user.isisVip()){
                itemGroup.setVip(user.isisVip());
            }
        }
        if(user!=null && user.isisVip()) {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("打印时间：yyyy年MM月dd日 hh:mm:ss\n");
            int newpoint = PointCalc(user.getPoint(),report.getTotal());
            user.setPoint(newpoint);
            shoppingListBuilder.append(String.format("会员编号： %s\t会员积分：%d分\n", user.getId(), user.getPoint()))
                    .append("----------------------\n");
            OutputParser outputParser = new OutputParser(new File("users.json"));
            try{
                outputParser.UpdateUser(user);
            }
            catch (Exception ee){
                ee.printStackTrace();
            }
            if (isShowTime)
                shoppingListBuilder.append(dateFormat.format(date))
                    .append("----------------------\n");
        }
        for (ItemGroup itemGroup : report.getItemGroupies()) {
            shoppingListBuilder.append(
                    new StringBuilder()
                            .append("名称：").append(itemGroup.groupName()).append("，")
                            .append("数量：").append(itemGroup.groupSize()).append(itemGroup.groupUnit()).append("，")
                            .append("单价：").append(String.format("%.2f", itemGroup.groupPrice())).append("(元)").append("，")
                            .append("小计：").append(String.format("%.2f", itemGroup.subTotal())).append("(元)").append("\n")
                            .toString());
        }
        if (report.hasPromotion()) {
            shoppingListBuilder.append("----------------------\n")
            .append("挥泪赠送商品：\n");
            for (ItemGroup itemGroup : report.getItemGroupies()){
                if (itemGroup.groupPromotion()){
                    shoppingListBuilder.append("名称：").append(itemGroup.groupName()).append("，数量：1") .append(itemGroup.groupUnit()).append("\n");
                }
            }
        }
        StringBuilder subStringBuilder = shoppingListBuilder
                .append("----------------------\n")
                .append("总计：").append(String.format("%.2f", report.getTotal())).append("(元)").append("\n");

        double saving = report.getSaving();
        if (saving == 0) {
            return subStringBuilder
                    .append("**********************\n")
                    .toString();
        }
        return subStringBuilder
                .append("节省：").append(String.format("%.2f", saving)).append("(元)").append("\n")
                .append("**********************\n")
                .toString();
    }

    int PointCalc(int point,double price){
        int addpoint = (int)price / 5;
        int result = point;
        if(0<= point && point <= 200){
            result += (1 * addpoint);
        }
        else if(200<point && point<=500){
            result += (3*addpoint);
        }
        else if(500<point){
            result += (5*addpoint);
        }
        return result;
    }

    public void setShowTime(boolean isShowTime){
        this.isShowTime = isShowTime;
    }
}
