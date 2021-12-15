import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bot.Item;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import bot.TelegramBot;
import database.City;
import database.Shop;
import parser.Parser;

public class TelegramBotApplication {
    private static final Map<String, String> getenv = System.getenv();

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot(getenv.get("BOT_NAME"), getenv.get("BOT_TOKEN")));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
