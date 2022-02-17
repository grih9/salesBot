package bot;

import org.junit.Assert;
import org.junit.Test;

public class TelegramBotTest {
    private final String NAME = "name";
    private final String TOKEN = "token";
    private TelegramBot telegramBot = new TelegramBot(NAME, TOKEN);

    @Test
    public void getBotToken() {
        Assert.assertEquals(TOKEN, telegramBot.getBotToken());
    }

    @Test
    public void getBotUsername() {
        Assert.assertEquals(NAME, telegramBot.getBotUsername());
    }

    @Test
    public void testIsNumeric() {
        Assert.assertTrue(telegramBot.isNumeric("12"));
        Assert.assertFalse(telegramBot.isNumeric("1,2"));
        Assert.assertFalse(telegramBot.isNumeric("abc"));
    }
/*
    @Test
    public void processNonCommandUpdate() {
    }*/
}
