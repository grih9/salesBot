package parser;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import bot.Item;
import database.City;
import database.Shop;

public class Parser {
    public static List<Item> findItemsByName(String itemName, City city, List<Shop> shops) {
        List<Item> items = new ArrayList<>();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        for (Shop shop : shops) {
            driver.get(shop.getWebsite());
            switch (shop.getName()) {
                case ("Пятёрочка"):
                    items = findItemsByNamePyatyorochka(itemName, city, driver);
                    break;
                case ("Перекрёсток"):
                    items = findItemsByNamePerekryostok(itemName, city, driver);
                    break;
                case ("Магнит"):
                    items = findItemsByNameMagnit(itemName, city, driver);
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

    public static List<Item> findItemsByNamePyatyorochka(String itemName, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();

        return items;
    }

    public static List<Item> findItemsByNamePerekryostok(String itemName, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();
        driver.findElement(By.cssSelector("input[name='search']")).sendKeys(itemName, Keys.ENTER);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(visibilityOfElementLocated(By.id("onlyDiscount")));
        driver.findElement(By.id("onlyDiscount")).sendKeys(Keys.SPACE);
        wait.until(visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div/main/div[1]/div/div/div[6]/div/div[1]/div/div")));
        WebElement webElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div/main/div[1]/div/div/div[6]/div/div[1]/div/div"));
        List<WebElement> webElements = webElement.findElements(By.tagName("a"));
        for (WebElement element: webElements) {
            Item item = new Item();
            element = element.findElement(By.xpath("./.."));
            if (element.findElements(By.className("price-old")).isEmpty()) {
                continue;
            }
            item.setImageURL(element.findElement(By.className("product-card__image-wrapper")).findElement(By.tagName("img")).getAttribute("src"));
            item.setName(element.findElement(By.className("product-card__title")).getText());
            item.setPrice(element.findElement(By.className("price-old")).getText());
            item.setSalePrice(element.findElement(By.className("price-new")).getText());
            item.setShopName("Перекрёсток");
            items.add(item);
            System.out.println(item.toString());
        }
        return items;
    }

    public static List<Item> findItemsByNameMagnit(String itemName, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();

        return items;
    }

    public static List<Item> findItemsByCathegoryPyatyorochka(String itemName, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();

        return items;
    }

    public static List<Item> findItemsByCathegoryPerekryostok(ArrayList<String> cathegories, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();
        List<String> keywords = getKeywordsFromCathegories(cathegories);

        List<WebElement> cathegoriesList = driver.findElement(By.className("catalog__list")).findElements(By.className("category-card__title"));
        if (cathegoriesList.isEmpty()) {
            return null;
        }
        int k = cathegoriesList.size() - 1;
        for (int i = 0; i < k; i++) {
            WebElement cathegory = driver.findElement(By.className("catalog__list")).findElements(By.className("category-card__title")).get(i);
            if (cathegory == null) {
                continue;
            }
            boolean isChosen = false;
            for (String s: keywords) {
                if (cathegory.getText().contains(s)) {
                    isChosen = true;
                    break;
                }
            }
            if (!isChosen) {
                continue;
            }
            driver.get(cathegory.findElement(By.xpath("./..")).findElement(By.xpath("./..")).getAttribute("href"));
            List<WebElement> subCathegories = driver.findElement(By.className("category-filter-item__content")).findElements(By.tagName("a"));
            if (subCathegories.isEmpty()) {
                continue;
            }
            int count = subCathegories.size() - 1;
            for (int j = 0; j < count; j++) {
                WebElement subCathegory = driver.findElement(By.className("category-filter-item__content")).findElements(By.tagName("a")).get(j);
                if (subCathegory == null) {
                    continue;
                }
                driver.get(subCathegory.getAttribute("href"));
                WebDriverWait wait = new WebDriverWait(driver, 10);
                wait.until(visibilityOfElementLocated(By.id("onlyDiscount")));
                driver.findElement(By.id("onlyDiscount")).sendKeys(Keys.SPACE);
                wait.until(visibilityOfElementLocated(By.className("product-card__image")));
                List<WebElement> webElements = driver.findElements(By.className("product-card__image"));
                if (webElements.isEmpty()) {
                    continue;
                }
                for (WebElement element: webElements) {
                    Item item = new Item();
                    element = element.findElement(By.xpath("./..")).findElement(By.xpath("./.."));
                    if (element.findElements(By.className("price-old")).isEmpty()) {
                        continue;
                    }
                    item.setImageURL(element.findElement(By.className("product-card__image-wrapper")).findElement(By.tagName("img")).getAttribute("src"));
                    item.setName(element.findElement(By.className("product-card__title")).getText());
                    item.setPrice(element.findElement(By.className("price-old")).getText());
                    item.setSalePrice(element.findElement(By.className("price-new")).getText());
                    item.setShopName("Перекрёсток");
                    items.add(item);
                    System.out.println(item.toString());
                }
            }
            driver.get("https://www.perekrestok.ru/cat");
        }
        driver.close();
        return items;
    }

    public static List<Item> findItemsByCathegoryMagnit(String itemName, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();

        return items;
    }

    public static ArrayList<String> getKeywordsFromCathegories(ArrayList<String> cathegories) {
        ArrayList<String> keywords = new ArrayList<>();
        cathegories.stream()
                .forEach(s -> {
                    if (s.contains("Товары для")) {
                        keywords.add(s);
                    } else {
                        String[] tmp = s.split(" ");
                        for (String s1 : tmp) {
                            if (!s1.contains("и") && !s1.contains("продукты")) {
                                if (s1.endsWith(",")) {
                                    s1.replaceAll(",", "");
                                }
                                keywords.add(s1);
                            }
                        }
                    }
                });
        return keywords;
    }
}
