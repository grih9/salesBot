package parser;

import java.util.*;
import java.util.concurrent.TimeUnit;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import bot.Item;
import database.City;
import database.Shop;

public class Parser {
    public static List<Item> findItemsByCategory(String category, City city, Shop shop) {
        List<Item> items = new ArrayList<>();

        WebDriver driver = null;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-debugging-port=9222");
        options.setBinary("/app/.chromedriver/bin/chromedriver");
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.addArguments("--headless");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("window-size=1800x900");
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36";
        options.addArguments("--user-agent=" + userAgent);
        try {
            driver = new ChromeDriver();
            driver.get(shop.getWebsite());

            switch (shop.getName()) {
                case ("Пятёрочка"):
                case ("Магнит"):
                case ("Ашан"):
                case ("Spar (Eurospar)"):
                case ("О'КЕЙ"):
                case ("Prisma"):
                case ("Лента"):
                case ("Карусель"):
                    items = findItemsByCathegoryEdadil(category, shop.getName(), city, driver);
                    break;
                case ("Перекрёсток"):
                    items = findItemsByCathegoryPerekryostok(category, city, driver);
                    break;
                case ("Дикси"):
                    items = findItemsByCathegoryDiksi(category, driver);
                    break;
            }
            driver.close();
        } catch (Exception e) {
            if (driver != null) {
                driver.close();
            }
            e.printStackTrace();
        }

        return items;
    }

    public static List<Item> findItemsByName(String itemName, City city, Shop shop) {
        List<Item> items = new ArrayList<>();

        WebDriver driver = null;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-debugging-port=9222");
        options.setBinary("/app/.chromedriver/bin/chromedriver");
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.addArguments("--headless");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("window-size=1800x900");
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36";
        options.addArguments("--user-agent=" + userAgent);
        List<Item> pItems;

        try {
            driver = new ChromeDriver();
            driver.get(shop.getWebsite());
            switch (shop.getName()) {
                case ("Перекрёсток"):
                    pItems = findItemsByNamePerekryostok(itemName, city, driver);
                    if (pItems != null) {
                        items.addAll(pItems);
                    }
                    break;
                case ("Магнит"):
                case ("Ашан"):
                case ("Spar (Eurospar)"):
                case ("О'КЕЙ"):
                case ("Prisma"):
                case ("Лента"):
                case ("Пятёрочка"):
                    items.addAll(findItemsByNameEdadil(itemName, shop.getName(), city, driver));
                    break;
                case ("Дикси"):
                    items.addAll(findItemsByNameDiksi(itemName, driver));
                    break;
                case ("Карусель"):
                    items.addAll(findItemsByNameKarusel(itemName, driver));
                    break;
            }
            driver.close();
        } catch (Exception e) {
            if (driver != null) {
                driver.close();
            }
            e.printStackTrace();
        }

        return items;
    }

    public static List<Item> findItemsByNameEdadilJsoup(String itemName, String shopName, City city, String  url) {
        List<Item> items = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("Магнит", "magnit-univer");
        map.put("Ашан", "auchan");
        map.put("Spar (Eurospar)", "eurospar");
        map.put("О'КЕЙ", "okmarket-giper");
        map.put("Prisma", "prismamarket_giper");
        map.put("Лента", "lenta-giper");

        Document doc = null;
        try {
            doc = Jsoup.connect("https://edadeal.ru/sankt-peterburg/retailers/lenta-giper?q=%D0%A5%D0%BB%D0%B5%D0%B1&retailer=lenta-giper&sort=aprice")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements element = doc.getElementsByClass("b-offer__root");

        element.get(0).getElementsByClass("b-image__root").get(0).getElementsByTag("img").get(0).attr("alt");
        return items;
    }

    public static List<Item> findItemsByNameEdadil(String itemName, String shopName, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, 10);

        Map<String, String> map = new HashMap<>();
        map.put("Магнит", "magnit-univer");
        map.put("Ашан", "auchan");
        map.put("Spar (Eurospar)", "eurospar");
        map.put("О'КЕЙ", "okmarket-giper");
        map.put("Prisma", "prismamarket_giper");
        map.put("Лента", "lenta-giper");

        driver.get(driver.getCurrentUrl() + "?q=" + itemName + "&sort=aprice&retailer=" + map.get(shopName));
        if (driver.findElements(By.className("p-not-found__big-header")).size() != 0) {
            return items;
        }
        if (driver.findElements(By.className("b-no-items-found__title")).size() != 0) {
            return items;
        }
        items = findItemsEdadil(driver, shopName);
        return items;
    }

    public static List<Item> findItemsByCathegoryEdadil(String cathegoryName, String shopName, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();
        cathegoryName = cathegoryName.toLowerCase();
        cathegoryName = cathegoryName.replaceAll("Товары для ", "");
        List<String> keywords = getKeywordsFromCathegories(cathegoryName);
        String sl = "сладости";
        if (cathegoryName.contains(sl.toLowerCase())) {
            keywords.add("кондитерские");
        }
        if (cathegoryName.contains("колбаса")) {
            keywords.add("мясные");
        }

        if (cathegoryName.contains("животных")) {
            keywords.add("зоотовары");
        }

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(visibilityOfElementLocated(By.cssSelector(".b-accordion__item1-title.b-accordion__item1-title_selected_false.b-accordion__item1-title_opened_false")));

        List<WebElement> cathegories = driver.
                findElements(By.cssSelector(".b-accordion__item1-title.b-accordion__item1-title_selected_false.b-accordion__item1-title_opened_false"));

        if (cathegories.isEmpty()) {
            return items;
        }

        List<String> cathegoriesNames = new ArrayList<>();
        List<String> cathegoriesHref = new ArrayList<>();
        for (WebElement webElement : cathegories) {
            cathegoriesNames.add(webElement.getText());
            cathegoriesHref.add(webElement.getAttribute("href"));
        }

        for (int i = 0; i < cathegories.size(); i++) {
            for (String s : keywords) {
                if (cathegoriesNames.get(i).toLowerCase().contains(s)) {
                    driver.get(cathegoriesHref.get(i));
                    return findItemsEdadil(driver, shopName);
                }
            }
        }

        driver.get(cathegoriesHref.get(0));

        wait.until(visibilityOfElementLocated(By.className("b-accordion__item2")));
        List<WebElement> subCathegories = driver.
                findElements(By.className("b-accordion__item2"));
        List<String> subCathegoriesNames = new ArrayList<>();
        List<String> subCathegoriesHref = new ArrayList<>();

        for (WebElement webElement : subCathegories) {
            subCathegoriesNames.add(webElement.findElement(By.tagName("a")).getText());
            subCathegoriesHref.add(webElement.findElement(By.tagName("a")).getAttribute("href"));
        }

        for (int j = 0; j < subCathegories.size(); j++) {
            for (String s : keywords) {
                if (subCathegoriesNames.get(j).toLowerCase().contains(s)) {
                    driver.get(subCathegoriesHref.get(j));
                    items.addAll(findItemsEdadil(driver, shopName));
                    break;
                }
            }
        }
        return items;
    }

    public static List<Item> findItemsEdadil(WebDriver driver, String shopName) {
        List<Item> items = new ArrayList<>();
        String url = driver.getCurrentUrl();
        int page = 1;
        int maxPage = 1;

        try {
            WebDriverWait wait2 = new WebDriverWait(driver, 5);
            wait2.until(visibilityOfElementLocated(By.xpath("//a[@class='b-no-items-found__body b-no-items-found__body-']")));
        } catch (Exception e) {
        }

        if (driver.findElements(By.xpath("//a[@class='b-no-items-found__body b-no-items-found__body-']")).size() != 0) {
            return items;
        }
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            WebDriverWait wait2 = new WebDriverWait(driver, 7);
            wait2.until(visibilityOfElementLocated(By.className("b-pagination__pages")));
        } catch (Exception e) {
        }

        if (driver.findElements(By.className("b-pagination__pages")).size() > 0) {
            List<WebElement> pageList = driver
                    .findElement(By.className("b-pagination__pages"))
                    .findElements(By.tagName("a"));
            if (pageList.size() != 0) {
                maxPage = Integer.parseInt(pageList.get(pageList.size() - 1).findElement(By.className("b-button__content")).getText());
            }
        }

        while (page <= maxPage) {

            try {
                WebDriverWait wait2 = new WebDriverWait(driver, 1);
                wait2.until(visibilityOfElementLocated(By.xpath("//a[@class='b-no-items-found__body b-no-items-found__body-']")));
            } catch (Exception e) {
            }

            if (driver.findElements(By.xpath("//a[@class='b-no-items-found__body b-no-items-found__body-']")).size() != 0) {
                return items;
            }

            wait.until(visibilityOfElementLocated(By.className("p-retailer__base-column")));
            List<WebElement> webElements = driver
                    .findElements(By.className("b-offer__root"));

            for (WebElement element : webElements) {
                wait.until(ExpectedConditions.visibilityOf(element));
                Item item = new Item();

                item.setImageURL(element.findElement(By.className("b-image__root"))
                        .findElement(By.tagName("img")).getAttribute("src"));

                String salesDate = element.findElement(By.className("b-offer__dates")).getText();
                salesDate = salesDate.substring(3);

                item.setSaleEndDate(salesDate);

                item.setName(element.findElement(By.className("b-image__root")).findElement(By.tagName("img")).getAttribute("alt")
                        .replaceAll(" со скидкой", "").replaceAll("\\*", ""));
                if (element.findElements(By.className("b-offer__price-old")).size() > 0) {
                    String price = element.findElement(By.className("b-offer__price-old")).getText();
                    price = price.replace(",", ".");

                    if (price.contains("₽")) {
                        price = price.substring(0, price.indexOf(" ₽"));
                    }

                    if (price.contains("От ")) {
                        item.setPrice(price.substring(3));
                    } else {
                        item.setPrice(price);
                    }
                }

                String salePrice = element.findElement(By.className("b-offer__price-new")).getText();
                salePrice = salePrice.replace(",", ".");

                if (salePrice.contains("₽")) {
                    salePrice = salePrice.substring(0, salePrice.indexOf(" ₽"));
                }
                item.setSalePrice(salePrice);
                item.setShopName(shopName);
                items.add(item);
            }
            page++;
            if (page <= maxPage) {
                driver.get(url + "&page=" + page);
            }
        }
        return items;
    }

    public static List<Item> findItemsByNameKarusel(String itemName, WebDriver driver) {
        List<Item> items = new ArrayList<>();
        int page = 1;

        driver.get("https://karusel.ru/catalog/search/?q=" + itemName + "&page=" + page);
        while (driver.findElements(By.xpath("//*[@class='card card--none promo-catalog-product card--with-hover card--fit-content']")).size() > 0) {
            items.addAll(findItemsKarusel(driver));
            page++;
            driver.get("https://karusel.ru/catalog/search/?q=" + itemName + "&page=" + page);
        }
        items.sort(Comparator.naturalOrder());
        return items;
    }

    public static List<Item> findItemsKarusel(WebDriver driver) {
        List<Item> items = new ArrayList<>();

        List<WebElement> webElements = driver
                .findElements(By.xpath("//*[@class='card card--none promo-catalog-product card--with-hover card--fit-content']"));

        for (int i = 0; i < webElements.size(); i++) {
            Item item = new Item();
            WebElement element = driver
                    .findElements(By.xpath("//*[@class='card card--none promo-catalog-product card--with-hover card--fit-content']"))
                    .get(i);

            item.setImageURL(element.findElement(By.xpath("//*[@class='karusel-image karusel-image-is-loaded']"))
                    .getCssValue("background-image").replace("url(\"", "").replace("\")", ""));
            String salesDate = element.
                    findElement(By.xpath("//*[@class='karusel-badge karusel-badge--attention promo-catalog-product__date']"))
                    .getText();
            salesDate = salesDate.substring(salesDate.indexOf(" ") + 1);
            item.setSaleEndDate(salesDate);
            String name = element.findElement(By.className("promo-catalog-product__name")).getText();

            if (name.endsWith("...")) {
                WebElement parent = element.findElement(By.xpath("./.."));
                String url = driver.getCurrentUrl();
                driver.get(parent.getAttribute("href"));
                WebDriverWait wait = new WebDriverWait(driver, 20);
                wait.until(visibilityOfElementLocated(By.cssSelector(".karusel-page-wrapper.karusel-page-wrapper--content")));
                name = driver.findElement(By.cssSelector(".karusel-page-wrapper.karusel-page-wrapper--content"))
                        .findElement(By.tagName("h2")).getText();
                driver.get(url);
                wait.until(visibilityOfElementLocated(By.xpath("//*[@class='card card--none promo-catalog-product card--with-hover card--fit-content']")));
                element = driver
                        .findElements(By.xpath("//*[@class='card card--none promo-catalog-product card--with-hover card--fit-content']"))
                        .get(i);
            }

            item.setName(name);

            item.setPrice(element.findElement(By.className("promo-catalog-product__price"))
                    .findElement(By.tagName("s")).getText());
            item.setSalePrice(element.findElement(By.className("promo-catalog-product__price"))
                    .findElement(By.tagName("b")).getText().replace(" ₽", ""));
            item.setShopName("Карусель");
            items.add(item);
        }
        return items;
    }

    public static List<Item> findItemsByCathegoryKarusel(String cathegoryName, WebDriver driver) {
        List<Item> items = new ArrayList<>();

        cathegoryName = cathegoryName.toLowerCase();
        cathegoryName = cathegoryName.replaceAll("Товары для ", "");
        List<String> keywords = getKeywordsFromCathegories(cathegoryName);
        if (cathegoryName.contains("колбаса")) {
            keywords.add("колбасы");
        }
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(visibilityOfElementLocated(By.cssSelector(".promo-catalog-menu-list.promo-catalog-menu-list--root")));

       /* wait.until(ExpectedConditions.
                elementToBeClickable(By.xpath("//*[@class='app-button app-button--transparent app-button--rounded app-button--regular promo-catalog-menu-list__more']")));
*/
        driver.findElement(By.cssSelector(".promo-catalog-menu-list.promo-catalog-menu-list--root"))
                .findElement(By.tagName("button")).click();

        List<WebElement> cathegories = driver.findElements(By.xpath("//*[@class='karusel-link promo-catalog-menu-list__link']"));
        int cathegoriesListSize = cathegories.size();
        if (cathegories.isEmpty()) {
            return null;
        }

        List<String> cathegoriesNames = new ArrayList<>();
        List<String> cathegoriesHref = new ArrayList<>();
        for (WebElement webElement : cathegories) {
            cathegoriesNames.add(webElement.getText());
            cathegoriesHref.add(webElement.getAttribute("href"));
        }

        for (int i = 0; i < cathegoriesListSize; i++) {
            boolean isChosen = false;
            for (String s : keywords) {
                if (cathegoriesNames.get(i).toLowerCase().contains(s)) {
                    isChosen = true;
                    break;
                }
            }
            if (!isChosen) {
                continue;
            }
            driver.get(cathegoriesHref.get(i));

            wait.until(visibilityOfElementLocated(By.xpath("//*[@class='promo-catalog-menu-list__container promo-catalog-menu-list__sub']")));

            int subCathegoriesSize = driver
                    .findElement(By.xpath("//*[@class='promo-catalog-menu-list__container promo-catalog-menu-list__sub']"))
                    .findElements(By.tagName("a")).size();

            for (int j = 0; j < subCathegoriesSize; j++) {
                driver.get(driver
                        .findElement(By.xpath("//*[@class='promo-catalog-menu-list__container promo-catalog-menu-list__sub']"))
                        .findElements(By.tagName("a"))
                        .get(j).getAttribute("href"));

                items.addAll(findItemsKarusel(driver));
            }
            break;
        }
        items.sort(Comparator.naturalOrder());
        return items;
    }

    public static List<Item> findItemsByNamePyatyorochka(String itemName, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();
        driver.findElement(By.cssSelector("input[placeholder='Поиск по товарам']")).sendKeys(itemName, Keys.ENTER);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            WebElement webElement = driver.findElement(By.className("items-list"));
            wait.until(visibilityOfElementLocated(By.xpath("//div[contains(., '" + itemName + "')]")));
            List<WebElement> webElements = webElement.findElements(By.tagName("img"));
            if (webElements.isEmpty()) {
                return null;
            }
            for (WebElement element : webElements) {
                Item item = new Item();
                element = element.findElement(By.xpath("./..")).findElement(By.xpath("./.."));
                item.setImageURL(element.findElement(By.tagName("img")).getAttribute("src"));
                item.setSaleEndDate(element.findElement(By.className("item-date")).getText());
                item.setName(element.findElement(By.tagName("img")).getAttribute("alt"));
                item.setPrice(element.findElement(By.className("price-regular")).getText() + " р.");
                item.setSalePrice(element.findElement(By.className("from")).findElement(By.xpath("./..")).getText() + " р.");
                item.setShopName("Пятёрочка");
                items.add(item);
            }
        } catch (Exception e) {
            return null;
        }
        return items;
    }

    public static List<Item> findItemsByNameDiksi(String itemName, WebDriver driver) {
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
        try {
            driver.findElement(By.id("onlyDiscount")).sendKeys(Keys.SPACE);
            wait.until(visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div/main/div[1]/div/div/div[6]/div/div[1]/div/div")));
            WebElement webElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div/main/div[1]/div/div/div[6]/div/div[1]/div/div"));
            List<WebElement> webElements = webElement.findElements(By.tagName("a"));
            for (WebElement element : webElements) {
                Item item = new Item();
                element = element.findElement(By.xpath("./.."));
                if (element.findElements(By.className("price-old")).isEmpty()) {
                    continue;
                }
                item.setImageURL(element.findElement(By.className("product-card__image-wrapper")).findElement(By.tagName("img"))
                        .getAttribute("src"));
                item.setName(element.findElement(By.className("product-card__title")).getText());
                item.setPrice(element.findElement(By.className("price-old")).getText());
                item.setSalePrice(element.findElement(By.className("price-new")).getText());
                item.setShopName("Перекрёсток");
                items.add(item);
            }
        } catch (Exception e) {
            return null;
        }
        return items;
    }

    public static List<Item> findItemsByCathegoryDiksi(String cathegory, WebDriver driver) {
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
                List<Item> items10 = parserItemsDiksi(driver);
                items.addAll(items10);
                driver.get("https://dixy.ru/catalog/gotovye-zavtraki-semechki-sukhofrukty/");
                while (driver.findElements(By.xpath("/html/body/section[3]/div/a")).size() > 0) {
                    driver.findElement(By.xpath("/html/body/section[3]/div/a")).click();
                }
                List<Item> items11 = parserItemsDiksi(driver);
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

    private static List<Item> parserItemsDiksi(WebDriver driver) {
        List<Item> items = new ArrayList<>();
        List<WebElement> webElements = driver.findElements(By.className("dixyCatalogItem"));
        for (WebElement element : webElements) {
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

    public static List<Item> findItemsByCathegoryPerekryostok(String cathegoryS, City city, WebDriver driver) {
        List<Item> items = new ArrayList<>();
        List<String> keywords = getKeywordsFromCathegories(cathegoryS);
        driver.get("https://www.perekrestok.ru/cat");
        List<WebElement> cathegoriesList = driver.findElement(By.className("catalog__list"))
                .findElements(By.className("category-card__title"));
        if (cathegoriesList.isEmpty()) {
            return null;
        }
        int k = cathegoriesList.size() - 1;
        for (int i = 0; i < k; i++) {
            WebElement cathegory = driver.findElement(By.className("catalog__list"))
                    .findElements(By.className("category-card__title")).get(i);
            if (cathegory == null) {
                continue;
            }
            boolean isChosen = false;
            for (String s : keywords) {
                if (cathegory.getText().contains(s)) {
                    isChosen = true;
                    break;
                }
            }
            if (!isChosen) {
                continue;
            }
            driver.get(cathegory.findElement(By.xpath("./..")).findElement(By.xpath("./..")).getAttribute("href"));
            List<WebElement> subCathegories = driver.findElement(By.className("category-filter-item__content"))
                    .findElements(By.tagName("a"));
            if (subCathegories.isEmpty()) {
                continue;
            }
            int count = subCathegories.size() - 1;
            for (int j = 0; j < count; j++) {
                WebElement subCathegory = driver.findElement(By.className("category-filter-item__content"))
                        .findElements(By.tagName("a")).get(j);
                if (subCathegory == null) {
                    continue;
                }
                driver.get(subCathegory.getAttribute("href"));
                WebDriverWait wait = new WebDriverWait(driver, 10);
                wait.until(visibilityOfElementLocated(By.id("onlyDiscount")));
                driver.findElement(By.id("onlyDiscount")).sendKeys(Keys.SPACE);
                try {
                    wait.until(visibilityOfElementLocated(By.className("product-card__image")));
                } catch (Exception e) {
                    continue;
                }
                List<WebElement> webElements = driver.findElements(By.className("product-card__image"));
                if (webElements.isEmpty()) {
                    continue;
                }
                for (WebElement element : webElements) {
                    Item item = new Item();
                    element = element.findElement(By.xpath("./..")).findElement(By.xpath("./.."));
                    if (element.findElements(By.className("price-old")).isEmpty()) {
                        continue;
                    }
                    item.setImageURL(element.findElement(By.className("product-card__image-wrapper"))
                            .findElement(By.tagName("img")).getAttribute("src"));
                    item.setName(element.findElement(By.className("product-card__title")).getText());
                    item.setPrice(element.findElement(By.className("price-old")).getText());
                    item.setSalePrice(element.findElement(By.className("price-new")).getText());
                    item.setShopName("Перекрёсток");
                    items.add(item);
                }
            }
            driver.get("https://www.perekrestok.ru/cat");
        }
        return items;
    }

    public static ArrayList<String> getKeywordsFromCathegories(String cathegory) {
        ArrayList<String> keywords = new ArrayList<>();

        if (cathegory.contains("Товары для")) {
            keywords.add(cathegory);
        } else {
            String[] tmp = cathegory.split(" ");
            for (String s1 : tmp) {
                if (!s1.equals("и") && !s1.equals("продукты")) {
                    if (s1.endsWith(",")) {
                        s1 = s1.replaceAll(",", "");
                    }
                    keywords.add(s1);
                }
            }
        }

        return keywords;
    }
}
