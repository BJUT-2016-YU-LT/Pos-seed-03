import com.thoughtworks.pos.common.EmptyShoppingCartException;
import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.Pos;
import com.thoughtworks.pos.domains.ShoppingChart;
import com.thoughtworks.pos.domains.User;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2014/12/28.
 */
public class PosTest {
    @Test
    public void testGetCorrectShoppingListForSingleItem() throws Exception {
        // given
        Item cokeCola = new Item("ITEM000000", "可口可乐", "瓶", 3.00);
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(cokeCola);

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                          "***商店购物清单***\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：3.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testGetCorrectShoppingListForTwoSameItems() throws Exception {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00));
        shoppingChart.add(new Item("ITEM000000", "可口可乐", "瓶", 3.00));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                          "***商店购物清单***\n"
                        + "名称：可口可乐，数量：2瓶，单价：3.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testGetCorrectShoppingListForMultipleItemsWithMultipleTypes() throws Exception{
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00));
        shoppingChart.add(new Item("ITEM000001", "可口可乐", "瓶", 3.00));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：2.00(元)\n"
                        + "名称：可口可乐，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：5.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testGetCorrectShoppingListWhenDifferentItemHaveSameItemName() throws  Exception{
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00));
        shoppingChart.add(new Item("ITEM000002", "雪碧", "瓶", 3.00));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：2.00(元)\n"
                        + "名称：雪碧，数量：1瓶，单价：3.00(元)，小计：3.00(元)\n"
                        + "----------------------\n"
                        + "总计：5.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test(expected = EmptyShoppingCartException.class)
    public void testThrowExceptionWhenNoItemsInShoppingCart() throws EmptyShoppingCartException{
        // given
        ShoppingChart shoppingChart = new ShoppingChart();

        // when
        Pos pos = new Pos();
        pos.getShoppingList(shoppingChart);
    }

    @Test
    public void testShouldSupportDiscountWhenHavingOneFavourableItem() throws EmptyShoppingCartException {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：1.60(元)\n"
                        + "----------------------\n"
                        + "总计：1.60(元)\n"
                        + "节省：0.40(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testIfhaveBothPrommotionAndDiscountDoWhat() throws EmptyShoppingCartException {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：2.00(元)\n"
                        + "----------------------\n"
                        + "总计：2.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testThereisnotCode() throws EmptyShoppingCartException {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000035", "雪碧", "瓶", 2.00, 0.8,true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：1瓶，单价：2.00(元)，小计：2.00(元)\n"
                        + "----------------------\n"
                        + "总计：2.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testIfhaveBothPrommotionAndDiscountDoWhatCountThree() throws EmptyShoppingCartException {
        // given
        ShoppingChart shoppingChart = new ShoppingChart();
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,true));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,true));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,true));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：3瓶，单价：2.00(元)，小计：4.00(元)\n"
                        + "----------------------\n"
                        + "挥泪赠送商品：\n"
                        + "名称：雪碧，数量：1瓶\n"
                        + "----------------------\n"
                        + "总计：4.00(元)\n"
                        + "节省：2.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testUserVipDiscount() throws EmptyShoppingCartException{
        // given
        User user = new User("USERTEST","Test",true);
        ShoppingChart shoppingChart = new ShoppingChart(user);
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号： USERTEST\t会员积分：0分\n"
                        + "----------------------\n"
                        + "名称：雪碧，数量：3瓶，单价：2.00(元)，小计：3.60(元)\n"
                        + "----------------------\n"
                        + "总计：3.60(元)\n"
                        + "节省：2.40(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testUserVipAddPoint() throws EmptyShoppingCartException{
        // given
        User user = new User("USERTEST","Test",true);
        user.setPoint(0);
        ShoppingChart shoppingChart = new ShoppingChart(user);
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号： USERTEST\t会员积分：1分\n"
                        + "----------------------\n"
                        + "名称：雪碧，数量：5瓶，单价：2.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "节省：4.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testUserVipAddPoint2() throws EmptyShoppingCartException{
        // given
        User user = new User("USERTEST","Test",true);
        user.setPoint(300);
        ShoppingChart shoppingChart = new ShoppingChart(user);
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号： USERTEST\t会员积分：303分\n"
                        + "----------------------\n"
                        + "名称：雪碧，数量：5瓶，单价：2.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "节省：4.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testUserVipAddPoint3() throws EmptyShoppingCartException{
        // given
        User user = new User("USERTEST","Test",true);
        user.setPoint(600);
        ShoppingChart shoppingChart = new ShoppingChart(user);
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号： USERTEST\t会员积分：605分\n"
                        + "----------------------\n"
                        + "名称：雪碧，数量：5瓶，单价：2.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "节省：4.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testUserVipAddPointWhenTwoHundred() throws EmptyShoppingCartException{
        // given
        User user = new User("USERTEST","Test",true);
        user.setPoint(200);
        ShoppingChart shoppingChart = new ShoppingChart(user);
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号： USERTEST\t会员积分：201分\n"
                        + "----------------------\n"
                        + "名称：雪碧，数量：5瓶，单价：2.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "节省：4.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testUserVipAddPointWhenFiveHundred() throws EmptyShoppingCartException{
        // given
        User user = new User("USERTEST","Test",true);
        user.setPoint(500);
        ShoppingChart shoppingChart = new ShoppingChart(user);
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号： USERTEST\t会员积分：503分\n"
                        + "----------------------\n"
                        + "名称：雪碧，数量：5瓶，单价：2.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "节省：4.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testUserIsNotVip() throws EmptyShoppingCartException{
        // given
        User user = new User("USERTEST","Test",false);
        user.setPoint(600);
        ShoppingChart shoppingChart = new ShoppingChart(user);
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "名称：雪碧，数量：5瓶，单价：2.00(元)，小计：8.00(元)\n"
                        + "----------------------\n"
                        + "总计：8.00(元)\n"
                        + "节省：2.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }

    @Test
    public void testUserDataError() throws EmptyShoppingCartException{
        // given
        User user = new User("USERTEST","Test",true);
        user.setPoint(-3);
        ShoppingChart shoppingChart = new ShoppingChart(user);
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));
        shoppingChart.add(new Item("ITEM000000", "雪碧", "瓶", 2.00, 0.8,false,0.75));

        // when
        Pos pos = new Pos();
        String actualShoppingList = pos.getShoppingList(shoppingChart);

        // then
        String expectedShoppingList =
                "***商店购物清单***\n"
                        + "会员编号： USERTEST\t会员积分：1分\n"
                        + "----------------------\n"
                        + "名称：雪碧，数量：5瓶，单价：2.00(元)，小计：6.00(元)\n"
                        + "----------------------\n"
                        + "总计：6.00(元)\n"
                        + "节省：4.00(元)\n"
                        + "**********************\n";
        assertThat(actualShoppingList, is(expectedShoppingList));
    }
}