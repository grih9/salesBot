package bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TelegramBotTest {
    private final String NAME = "name";
    private final String TOKEN = "token";
    private TelegramBot telegramBot = new TelegramBot(NAME, TOKEN);

    @Test
    public void getBotToken() {
        Assertions.assertEquals(TOKEN, telegramBot.getBotToken());
    }

    @Test
    public void getBotUsername() {
        Assertions.assertEquals(NAME, telegramBot.getBotUsername());
    }

    @Test
    public void testIsNumeric() {
        Assertions.assertTrue(telegramBot.isNumeric("12"));
        Assertions.assertFalse(telegramBot.isNumeric("1,2"));
        Assertions.assertFalse(telegramBot.isNumeric("abc"));
    }
/*
    @Test
    public void processNonCommandUpdate() {
    }*/
}
