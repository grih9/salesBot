package parser;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import bot.Item;
import database.City;
import database.Shop;

public class Parser {
    public static List<Item> findItemsByCategory(String category, City city, Shop shop){
        List<Item> items = new ArrayList<>();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        driver.get(shop.getWebsite());

            switch (shop.getName()) {
                case ("Пятёрочка"):
                    //items = findItemsByNamePyatyorochka(itemName, city, driver);
                    break;
                case ("Перекрёсток"):
                    //items = findItemsByCathegoryPerekryostok(category, city, driver);
                    break;
                case ("Магнит"):
                    //items = findItemsByNameMagnit(itemName, city, driver);
                    break;
                case ("Ашан"):
                    //
                    break;
                case ("Дикси"):
                    items = findItemsByCathegoryDiksi(category, driver);
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

        driver.close();
        return items;
    }

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
                    items = findItemsByNameDiksi(itemName, driver);
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

    public static List<Item> findItemsByNameDiksi(String itemName, WebDriver driver){
        List<Item> items;
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.findElement(By.xpath("/html/body/header/div/div/ul[2]/li[5]/a/p")).click();
        wait.until(visibilityOfElementLocated(By.xpath("/html/body/header/div/div/div/form/input")));
        driver.findElement(By.xpath("/html/body/header/div/div/div/form/input")).sendKeys(itemName, Keys.ENTER);
        while (driver.findElements(By.xpath("/html/body/section[2]/div/a")).size() > 0) {
            driver.findElement(By.xpath("/html/body/section[2]/div/a")).click();
        }
        items = parserItemsDiksi(driver);
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

    public static  List<Item> findItemsByCathegoryDiksi(String cathegory, WebDriver driver) {
        List<Item> items = new ArrayList<>();
        switch (cathegory) {
                case "Молочные продукты и яйца":
                    driver.get("https://dixy.ru/catalog/molochnaya-gastronomiya/");
                    while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                        driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                    }
                    List<Item> items1 = parserItemsDiksi(driver);
                    items.addAll(items1);
                    driver.get("https://dixy.ru/catalog/myaso-yaytso/");
                    List<Item> items5 = parserItemsDiksi(driver);
                    List<Item> items6 = new ArrayList<>();
                    for (Item i : items5) {
                        if (i.getName().contains("яйца") || i.getName().contains("яйцо")) {
                            items6.add(i);
                        }
                    }
                    items.addAll(items6);
                    driver.get("https://dixy.ru/catalog/maslo-syry/");
                    while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                        driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                    }
                    List<Item> items2 = parserItemsDiksi(driver);
                    items.addAll(items2);
                    break;
                case "Хлеб и выпечка":
                    driver.get("https://dixy.ru/catalog/khleb-torty/");
                    while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                        driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                    }
                    items = parserItemsDiksi(driver);
                    break;
                case "Мясо и птица":
                    driver.get("https://dixy.ru/catalog/myaso-yaytso/");
                    while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                        driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                    }
                    List<Item> items3 = parserItemsDiksi(driver);
                    List<Item> items7 = new ArrayList<>();
                    for (Item i : items3) {
                        if (!i.getName().contains("яйца") && !i.getName().contains("яйцо")) {
                            items7.add(i);
                        }
                    }
                    items.addAll(items7);
                    break;
                case "Колбаса и сосиски":
                    driver.get("https://dixy.ru/catalog/myasnaya-gastronomiya/");
                    while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                        driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                    }
                    items = parserItemsDiksi(driver);
                    break;
                case "Замороженные продукты":
            case "Рыба и морепродукты":
                driver.get("https://dixy.ru/catalog/kulinariya-zamorozka-morozhenoe/");
                    while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                        driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                    }
                    items = parserItemsDiksi(driver);
                    break;
            case "Бакалея":
                    driver.get("https://dixy.ru/catalog/krupy-zavtraki-spetsii/");
                    while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                        driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                    }
                    List<Item> items10= parserItemsDiksi(driver);
                    items.addAll(items10);
                    driver.get("https://dixy.ru/catalog/gotovye-zavtraki-semechki-sukhofrukty/");
                    while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                        driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                    }
                    List<Item> items11= parserItemsDiksi(driver);
                    items.addAll(items11);
                    break;
            case "Сладости":
                driver.get("https://dixy.ru/catalog/konditerskie-izdeliya/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                items = parserItemsDiksi(driver);
                break;
            case "Чай, кофе и какао":
                driver.get("https://dixy.ru/catalog/kofe-chay/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                items = parserItemsDiksi(driver);
                break;
            case "Напитки":
                driver.get("https://dixy.ru/catalog/napitki/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                items = parserItemsDiksi(driver);
                break;
            case "Товары для животных":
                driver.get("https://dixy.ru/catalog/tovary-dlya-zhivotnykh/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                items = parserItemsDiksi(driver);
                break;
            case "Гигиена и уход":
                driver.get("https://dixy.ru/catalog/krasota/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                items = parserItemsDiksi(driver);
                break;
            case "Товары для детей":
                driver.get("https://dixy.ru/catalog/tovary-dlya-detey/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                items = parserItemsDiksi(driver);
                break;
            case "Товары для дома":
                driver.get("https://dixy.ru/catalog/chistota/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                items = parserItemsDiksi(driver);
                break;
            case "Консервы и соленья":
                driver.get("https://dixy.ru/catalog/konservy-sousy/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                items = parserItemsDiksi(driver);
                break;
            case "Фрукты и овощи":
                driver.get("https://dixy.ru/catalog/ovoshchi-i-frukty/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                items = parserItemsDiksi(driver);
                break;
            default:
                break;
            }


        return items;
    }

    private static List<Item> parserItemsDiksi(WebDriver driver){
        List<Item> items = new ArrayList<>();
        List<WebElement> webElements = driver.findElements(By.className("dixyCatalogItem"));
        for (WebElement element: webElements) {
            Item item = new Item();
            item.setImageURL(element.findElement(By.className("dixyCatalogItem__picplacer")).findElement(By.tagName("img")).getAttribute("src"));
            //item.setName(element.findElement(By.className("dixyCatalogItem__title")).getText());
            item.setName(element.findElement(By.className("dixyCatalogItem__picplacer")).findElement(By.tagName("img")).getAttribute("alt"));
            String priceRub = element.findElement(By.className("dixyCatalogItemPrice__new")).findElement(By.tagName("p")).getText();
            String priceKop = element.findElement(By.className("dixyCatalogItemPrice__kopeck")).getText();
            String finalPrice = priceRub + "." + priceKop;
            item.setSalePrice(finalPrice);
            try {
                item.setPrice(element.findElement(By.className("dixyCatalogItemPrice__oldprice")).getText());
            } catch (NoSuchElementException e) {
                //
            }
            String date = element.findElement(By.className("dixyCatalogItem__term")).getText();
            item.setSaleBeginDate(date.substring(0, 10));
            item.setSaleEndDate(date.substring(13, 23));
            item.setShopName("Дикси");
            items.add(item);
        }
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
