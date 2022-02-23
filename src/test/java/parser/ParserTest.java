package parser;

import bot.Item;
import database.City;
import database.Shop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

class ParserTest {
    private static City city;
    private static Shop shop;
    private static Shop shop2;
    private static WebDriver driver;
    private final String category = "Хлеб и выпечка";
    private final String itemName = "Хлеб";
    private final String incorrectCategory = "Blabla";
    private final String incorrectItemName = "smth";

    @BeforeAll
    static void setProperties() {
        String cityName = "Москва";
        String cityRegion = "region";
        city = new City(cityName, cityRegion);
        String shopName = "Дикси";
        String shopWebSite = "https://dixy.ru";
        shop = new Shop(shopName, shopWebSite);
        String shopName2 = "Магнит";
        String shopWebSite2 = "https://edadeal.ru/sankt-peterburg/retailers/magnit-univer";
        shop2 = new Shop(shopName2, shopWebSite2);
        //System.setProperty("webdriver.chrome.driver", "D:\\Testing\\chromedriver3\\chromedriver.exe");
        driver = null;
    }

    @Test
    void findItemsByCategory() {
        List<Item> items = Parser.findItemsByCategory(category, city, shop);
        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void findItemByCategoryNegative() {
        List<Item> items = Parser.findItemsByCategory(incorrectCategory, city, shop);
        Assertions.assertTrue(items.isEmpty());
    }

    @Test
    void findItemsByName() {
        List<Item> items = Parser.findItemsByName(itemName, city, shop);
        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void findItemsByNameNegative() {
        List<Item> items = Parser.findItemsByName(incorrectItemName, city, shop);
        Assertions.assertTrue(items.isEmpty());
    }

    @Test
    void findItemsByNameEdadil() {
        driver = new ChromeDriver();
        driver.get(shop2.getWebsite());
        List<Item> items = Parser.findItemsByNameEdadil(itemName, shop2.getName(), city, driver);
        driver.quit();
        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void findItemsByCathegoryEdadil() {
        driver = new ChromeDriver();
        driver.get(shop2.getWebsite());
        List<Item> items = Parser.findItemsByCathegoryEdadil(category, shop2.getName(), city, driver);
        driver.quit();
        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void findItemsByCathegoryEdadilNegative() {
        driver = new ChromeDriver();
        driver.get(shop2.getWebsite());
        List<Item> items = Parser.findItemsByCathegoryEdadil(incorrectCategory, shop2.getName(), city, driver);
        driver.quit();
        Assertions.assertTrue(items.isEmpty());
    }

    @Test
    void findItemsEdadil() {
        driver = new ChromeDriver();
        driver.get(shop2.getWebsite() + "?q=" + itemName + "&sort=aprice&retailer=" + "magnit-univer");
        List<Item> items = Parser.findItemsEdadil(driver, shop2.getName());
        driver.quit();
        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void findItemsByNameDiksi() {
        driver = new ChromeDriver();
        driver.get(shop.getWebsite());
        List<Item> items = Parser.findItemsByNameDiksi(itemName, driver);
        driver.quit();
        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void findItemsByNameDiksiNegative() {
        driver = new ChromeDriver();
        driver.get(shop.getWebsite());
        List<Item> items = Parser.findItemsByNameDiksi(incorrectItemName, driver);
        driver.quit();
        Assertions.assertTrue(items.isEmpty());
    }

    @Test
    void findItemsByCathegoryDiksi() {
        driver = new ChromeDriver();
        driver.get(shop.getWebsite());
        List<Item> items = Parser.findItemsByCathegoryDiksi(category, driver);
        driver.quit();
        Assertions.assertFalse(items.isEmpty());
    }

    @Test
    void findItemsByCathegoryDiksiNegative() {
        driver = new ChromeDriver();
        driver.get(shop.getWebsite());
        List<Item> items = Parser.findItemsByCathegoryDiksi(incorrectCategory, driver);
        driver.quit();
        Assertions.assertTrue(items.isEmpty());
    }

    @Test
    void getKeywordsFromCathegories() {
        ArrayList<String> keywords = Parser.getKeywordsFromCathegories(category);
        Assertions.assertFalse(keywords.isEmpty());
    }

    @Test
    void getKeywordsFromCathegories2() {
        ArrayList<String> keywords = Parser.getKeywordsFromCathegories(category);
        ArrayList<String> keywords2 = new ArrayList<>();
        keywords2.add("Хлеб");
        keywords2.add("выпечка");
        Assertions.assertEquals(keywords2, keywords);
    }
}