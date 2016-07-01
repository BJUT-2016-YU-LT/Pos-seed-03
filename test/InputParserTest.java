/**
 * Created by Administrator on 2015/1/2.
 */

import com.thoughtworks.pos.domains.Item;
import com.thoughtworks.pos.domains.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.thoughtworks.pos.services.services.InputParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InputParserTest {

    private File indexFile;
    private File itemsFile;
    private File usersFile;
    @Before
    public void setUp() throws Exception {
        indexFile = new File("./inIndex.json");
        itemsFile = new File("./inItems.json");
        usersFile = new File("./inUsers.json");
    }

    @After
    public void tearDown() throws Exception {
        if(indexFile.exists()){
            indexFile.delete();
        }
        if(itemsFile.exists()){
            itemsFile.delete();
        }
        if(usersFile.exists()){
            usersFile.delete();
        }
    }

    @Test
    public void testParseJsonFileToItems() throws Exception {
        String sampleIndex = new StringBuilder()
                .append("{\n")
                .append("'ITEM000004':{\n")
                .append("\"name\": '电池',\n")
                .append("\"unit\": '个',\n")
                .append("\"price\": 2.00,\n")
                .append("\"discount\": 0.8\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder()
                .append("[\n")
                .append("\"ITEM000004\"")
                .append("]")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        InputParser inputParser = new InputParser(indexFile, itemsFile);
        ArrayList<Item> items = inputParser.Parser().getItems();

        assertThat(items.size(), is(1));
        Item item = items.get(0);
        assertThat(item.getName(), is("电池"));
        assertThat(item.getBarcode(), is("ITEM000004"));
        assertThat(item.getUnit(), is("个"));
        assertThat(item.getPrice(), is(2.00));
        assertThat(item.getDiscount(), is(0.8));
    }

    @Test
    public void testParseJsonFileToUsers() throws Exception{
        String sampleIndex = new StringBuilder()
                .append("{\n" +
                        "    \"ITEM000000\": {\n" +
                        "        \"name\": \"可口可乐\",\n" +
                        "        \"unit\": \"瓶\",\n" +
                        "        \"price\": 3.00,\n" +
                        "        \"vipDiscount\": 0.9\n" +
                        "    },\n" +
                        "    \"ITEM000001\": {\n" +
                        "        \"name\": \"雪碧\",\n" +
                        "        \"unit\": \"瓶\",\n" +
                        "        \"price\": 3.00,\n" +
                        "        \"discount\": 0.8,\n" +
                        "        \"vipDiscount\": 0.95\n" +
                        "    },\n" +
                        "    \"ITEM000002\": {\n" +
                        "        \"name\": \"电池\",\n" +
                        "        \"unit\": \"个\",\n" +
                        "        \"price\": 5.00\n" +
                        "    }\n" +
                        "}")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder()
                .append("{\n")
                .append("\"user\": \"USER0001\",")
                .append("\"items\":[\n" +
                        "\"ITEM000000\",\n" +
                        "\"ITEM000001\",\n" +
                        "\"ITEM000002\"\n" +
                        "]")
                .append("}")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        String sampleUsers = new StringBuilder()
                .append("{\n" +
                        "  \"USER0002\": {\n" +
                        "    \"name\": \"USER 002\",\n" +
                        "    \"isVip\": false,\n" +
                        "    \"point\": 0\n" +
                        "  },\n" +
                        "  \"USER0001\": {\n" +
                        "    \"name\": \"USER 001\",\n" +
                        "    \"isVip\": true,\n" +
                        "    \"point\": 20\n" +
                        "  },\n" +
                        "  \"USER0003\": {\n" +
                        "    \"name\": \"USER 003\",\n" +
                        "    \"isVip\": true,\n" +
                        "    \"point\": 0\n" +
                        "  }\n" +
                        "}")
                .toString();
        WriteToFile(usersFile, sampleUsers);

        InputParser inputParser = new InputParser(indexFile, itemsFile,usersFile);
        ArrayList<Item> items = inputParser.Parser().getItems();
        User user = inputParser.Parser().getUser();
        assertThat(items.size(), is(3));
        Item item = items.get(1);
        assertThat(item.getName(), is("雪碧"));
        assertThat(item.getBarcode(), is("ITEM000001"));
        assertThat(item.getUnit(), is("瓶"));
        assertThat(item.getPrice(), is(3.00));
        assertThat(item.getDiscount(), is(0.8));
        assertThat(user.getId(),is("USER0001"));
        assertThat(user.getName(),is("USER 001"));
        assertThat(user.isisVip(),is(true));
        assertThat(user.getPoint(),is(20));
    }

    private void WriteToFile(File file, String content) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.write(content);
        printWriter.close();
    }

    @Test
    public void testParseJsonWhenHasNoDiscount() throws Exception {
        String sampleIndex = new StringBuilder()
                .append("{\n")
                .append("'ITEM000004':{\n")
                .append("\"name\": '电池',\n")
                .append("\"unit\": '个',\n")
                .append("\"price\": 2.00\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder()
                .append("[\n")
                .append("\"ITEM000004\"")
                .append("]")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        InputParser inputParser = new InputParser(indexFile, itemsFile);
        ArrayList<Item> items = inputParser.Parser().getItems();
        Item item = items.get(0);
        assertThat(item.getDiscount(), is(1.00));
    }

    @Test
    public void testParseJsonWhenHasPromotion() throws Exception {
        String sampleIndex = new StringBuilder()
                .append("{\n")
                .append("'ITEM000004':{\n")
                .append("\"name\": '电池',\n")
                .append("\"unit\": '个',\n")
                .append("\"price\": 2.00,\n")
                .append("\"promotion\":true\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder()
                .append("[\n")
                .append("\"ITEM000004\"")
                .append("]")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        InputParser inputParser = new InputParser(indexFile, itemsFile);
        ArrayList<Item> items = inputParser.Parser().getItems();
        Item item = items.get(0);
        assertThat(item.getPromotion(), is(true));
    }

    @Test
    public void testParseJsonWhenHasPromotionTheDiscoutIs() throws Exception {
        String sampleIndex = new StringBuilder()
                .append("{\n")
                .append("\"ITEM000004\":{\n")
                .append("\"name\": \"电池\",\n")
                .append("\"unit\": \"个\",\n")
                .append("\"price\": 2.00,\n")
                .append("\"promotion\":true\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder()
                .append("[\n")
                .append("\"ITEM000004\"")
                .append("]")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        InputParser inputParser = new InputParser(indexFile, itemsFile);
        ArrayList<Item> items = inputParser.Parser().getItems();
        Item item = items.get(0);
        assertThat(item.getDiscount(), is(1.00));
    }

    @Test
    public void testParseJsonWhenHasBothPromotionAndDiscout() throws Exception {
        String sampleIndex = new StringBuilder()
                .append("{\n")
                .append("'ITEM000004':{\n")
                .append("\"name\": '电池',\n")
                .append("\"unit\": '个',\n")
                .append("\"price\": 2.00,\n")
                .append("\"discount\":0.8,\n")
                .append("\"promotion\":true\n")
                .append("}\n")
                .append("}\n")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder()
                .append("[\n")
                .append("\"ITEM000004\"")
                .append("]")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        InputParser inputParser = new InputParser(indexFile, itemsFile);
        ArrayList<Item> items = inputParser.Parser().getItems();
        Item item = items.get(0);
        assertThat(item.getDiscount(), is(1.00));
    }

    @Test
    public void testUserNotFound() throws Exception{
        String sampleIndex = new StringBuilder()
                .append("{\n" +
                        "    \"ITEM000000\": {\n" +
                        "        \"name\": \"可口可乐\",\n" +
                        "        \"unit\": \"瓶\",\n" +
                        "        \"price\": 3.00,\n" +
                        "        \"vipDiscount\": 0.9\n" +
                        "    },\n" +
                        "    \"ITEM000001\": {\n" +
                        "        \"name\": \"雪碧\",\n" +
                        "        \"unit\": \"瓶\",\n" +
                        "        \"price\": 3.00,\n" +
                        "        \"discount\": 0.8,\n" +
                        "        \"vipDiscount\": 0.95\n" +
                        "    },\n" +
                        "    \"ITEM000002\": {\n" +
                        "        \"name\": \"电池\",\n" +
                        "        \"unit\": \"个\",\n" +
                        "        \"price\": 5.00\n" +
                        "    }\n" +
                        "}")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder()
                .append("{\n")
                .append("\"user\": \"USER0004\",")
                .append("\"items\":[\n" +
                        "\"ITEM000000\",\n" +
                        "\"ITEM000001\",\n" +
                        "\"ITEM000002\"\n" +
                        "]")
                .append("}")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        String sampleUsers = new StringBuilder()
                .append("{\n" +
                        "  \"USER0002\": {\n" +
                        "    \"name\": \"USER 002\",\n" +
                        "    \"isVip\": false,\n" +
                        "    \"point\": 0\n" +
                        "  },\n" +
                        "  \"USER0001\": {\n" +
                        "    \"name\": \"USER 001\",\n" +
                        "    \"isVip\": true,\n" +
                        "    \"point\": 20\n" +
                        "  },\n" +
                        "  \"USER0003\": {\n" +
                        "    \"name\": \"USER 003\",\n" +
                        "    \"isVip\": true,\n" +
                        "    \"point\": 0\n" +
                        "  }\n" +
                        "}")
                .toString();
        WriteToFile(usersFile, sampleUsers);

        InputParser inputParser = new InputParser(indexFile, itemsFile,usersFile);
        ArrayList<Item> items = inputParser.Parser().getItems();
        User user = inputParser.Parser().getUser();
        assertThat(items.size(), is(3));
        Item item = items.get(1);
        assertThat(item.getName(), is("雪碧"));
        assertThat(item.getBarcode(), is("ITEM000001"));
        assertThat(item.getUnit(), is("瓶"));
        assertThat(item.getPrice(), is(3.00));
        assertThat(item.getDiscount(), is(0.8));
        assertThat(user.getId(),is("USER0004"));
        assertThat(user.getName(),is("USER0004"));
        assertThat(user.isisVip(),is(false));
        assertThat(user.getPoint(),is(0));
    }

    @Test
    public void testItemNotFound() throws Exception{
        String sampleIndex = new StringBuilder()
                .append("{\n" +
                        "    \"ITEM000000\": {\n" +
                        "        \"name\": \"可口可乐\",\n" +
                        "        \"unit\": \"瓶\",\n" +
                        "        \"price\": 3.00,\n" +
                        "        \"vipDiscount\": 0.9\n" +
                        "    },\n" +
                        "    \"ITEM000001\": {\n" +
                        "        \"name\": \"雪碧\",\n" +
                        "        \"unit\": \"瓶\",\n" +
                        "        \"price\": 3.00,\n" +
                        "        \"discount\": 0.8,\n" +
                        "        \"vipDiscount\": 0.95\n" +
                        "    },\n" +
                        "    \"ITEM000002\": {\n" +
                        "        \"name\": \"电池\",\n" +
                        "        \"unit\": \"个\",\n" +
                        "        \"price\": 5.00\n" +
                        "    }\n" +
                        "}")
                .toString();
        WriteToFile(indexFile, sampleIndex);

        String sampleItems = new StringBuilder()
                .append("{\n")
                .append("\"user\": \"USER0004\",")
                .append("\"items\":[\n" +
                        "\"ITEM000000\",\n" +
                        "\"ITEM000001\",\n" +
                        "\"ITEM000005\"\n" +
                        "]")
                .append("}")
                .toString();
        WriteToFile(itemsFile, sampleItems);

        String sampleUsers = new StringBuilder()
                .append("{\n" +
                        "  \"USER0002\": {\n" +
                        "    \"name\": \"USER 002\",\n" +
                        "    \"isVip\": false,\n" +
                        "    \"point\": 0\n" +
                        "  },\n" +
                        "  \"USER0001\": {\n" +
                        "    \"name\": \"USER 001\",\n" +
                        "    \"isVip\": true,\n" +
                        "    \"point\": 20\n" +
                        "  },\n" +
                        "  \"USER0003\": {\n" +
                        "    \"name\": \"USER 003\",\n" +
                        "    \"isVip\": true,\n" +
                        "    \"point\": 0\n" +
                        "  }\n" +
                        "}")
                .toString();
        WriteToFile(usersFile, sampleUsers);

        InputParser inputParser = new InputParser(indexFile, itemsFile,usersFile);
        ArrayList<Item> items = inputParser.Parser().getItems();
        User user = inputParser.Parser().getUser();
        assertThat(items.size(), is(2));
        Item item = items.get(1);
        assertThat(item.getName(), is("雪碧"));
        assertThat(item.getBarcode(), is("ITEM000001"));
        assertThat(item.getUnit(), is("瓶"));
        assertThat(item.getPrice(), is(3.00));
        assertThat(user.getId(),is("USER0004"));
        assertThat(user.getName(),is("USER0004"));
        assertThat(user.isisVip(),is(false));
        assertThat(user.getPoint(),is(0));
    }


}
