package parser;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import bot.Item;
import database.City;
import database.Shop;

public class Parser {
    public List<Item> findItemsByName(String itemName, City city, List<Shop> shops) {
        List<Item> items = new ArrayList<>();
        WebDriver driver = new ChromeDriver();
        for (Shop shop : shops) {
            driver.get(shop.getWebsite());
            switch (shop.getName()) {
                case ("Пятёрочка"):
                    //
                    break;
                case ("Перекрёсток"):
                    //
                    break;
                case ("Магнит"):
                    //
                    break;
                case ("Ашан"):
                    //
                    break;
                case ("Дикси"):
                    //
                    break;
                case ("Spar"):
                    //
                    break;
                case ("О'КЕЙ"):
                    //
                    break;
                case ("Лента"):
                    //
                    break;
                case ("Карусель"):
                    //
                    break;
                case ("Prisma"):
                    //
                    break;
            }
        }
        return items;
    }
}
