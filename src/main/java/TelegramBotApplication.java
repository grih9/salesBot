import database.City;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import parser.Parser;

import java.util.ArrayList;
import java.util.Map;

public class TelegramBotApplication {
    private static final Map<String, String> getenv = System.getenv();

    public static void main(String[] args) {
        /*try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot(getenv.get("BOT_NAME"), getenv.get("BOT_TOKEN")));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/

        ArrayList<String> categories= new ArrayList<>();
        categories.add("Напитки");

        /*System.setProperty("webdriver.chrome.driver",
                "C:\\chromedriver_win32\\chromedriver.exe");*/
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        //options.addArguments("--disable-gpu");
        //options.addArguments("--window-size=1400,800");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.perekrestok.ru/cat");

        System.out.println(Parser.findItemsByNamePerekryostok("Молоко",
                new City("Санкт-Петербург", "Санкт-Петербург"), driver));
    }
}
