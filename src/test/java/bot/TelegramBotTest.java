package bot;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class TelegramBotTest {
    private final String NAME = "name";
    private final String TOKEN = "token";
    private final TelegramBot telegramBot = new TelegramBot(NAME, TOKEN);
    private static final User user = new User();
    private static final Chat chat = new Chat();

    @BeforeAll
    static void setUp() {
        user.setUserName("anilochka");
        chat.setId(1L);
    }

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

    @Test
    public void testProcessNonCommandUpdateCommands() {
        Update update = prepareUpdate("/start", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals(Command.START, TelegramBot.userCommand.get(1L));

        update = prepareUpdate("/city", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals(Command.CITY, TelegramBot.userCommand.get(1L));

        update = prepareUpdate("/shops", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals(Command.CHOSE_SHOPS, TelegramBot.userCommand.get(1L));

        update = prepareUpdate("/finditem", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals(Command.FIND_ITEM, TelegramBot.userCommand.get(1L));

        update = prepareUpdate("/showitems", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals(Command.SHOW_ITEMS, TelegramBot.userCommand.get(1L));
    }

    @Test
    public void testProcessNonCommandUpdateText() {
        Update update = prepareUpdate("text", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertNull(TelegramBot.userCommand.get(1L));
        Assertions.assertNull(TelegramBot.userNumbers.get(1L));
    }

    @Test
    public void testProcessNonCommandUpdateTextCity() {
        TelegramBot.userCommand.put(1L, Command.CITY);
        Update update = prepareUpdate("text", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals("text", telegramBot.chooseCityCommand.getMessage());
    }

    @Test
    public void testProcessNonCommandUpdateTextFindItem() {
        TelegramBot.userCommand.put(1L, Command.FIND_ITEM);
        Update update = prepareUpdate("text", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals("text", telegramBot.findItemCommand.getMessage());
    }

    @Test
    public void testProcessNonCommandUpdateTextChooseShops() {
        TelegramBot.userCommand.put(1L, Command.CHOSE_SHOPS);
        TelegramBot.userNumbers.put(1L, new ArrayList<>());
        Update update = prepareUpdate("0", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals("0", TelegramBot.userNumbers.get(1L).get(0).toString());

        TelegramBot.userNumbers.put(1L, new ArrayList<>());
        update = prepareUpdate("0,2", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals("0", TelegramBot.userNumbers.get(1L).get(0).toString());
        Assertions.assertEquals("2", TelegramBot.userNumbers.get(1L).get(1).toString());

        update = prepareUpdate("Стереть", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals(1, TelegramBot.userNumbers.get(1L).size());
        Assertions.assertEquals("0", TelegramBot.userNumbers.get(1L).get(0).toString());

        update = prepareUpdate("Далее", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals(1, telegramBot.chooseShopsCommand.getNumbers().size());
        Assertions.assertEquals("0", telegramBot.chooseShopsCommand.getNumbers().get(0).toString());

        update = prepareUpdate("Назад", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertNull(TelegramBot.userCommand.get(1L));
        Assertions.assertTrue(TelegramBot.userNumbers.get(1L).isEmpty());
    }

    @Test
    public void testProcessNonCommandUpdateTextShowItems() {
        TelegramBot.userCommand.put(1L, Command.SHOW_ITEMS);
        TelegramBot.userNumbers.put(1L, new ArrayList<>());
        Update update = prepareUpdate("0", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals("0", TelegramBot.userNumbers.get(1L).get(0).toString());

        TelegramBot.userNumbers.put(1L, new ArrayList<>());
        update = prepareUpdate("0,2", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals("0", TelegramBot.userNumbers.get(1L).get(0).toString());
        Assertions.assertEquals("2", TelegramBot.userNumbers.get(1L).get(1).toString());

        update = prepareUpdate("Стереть", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals(1, TelegramBot.userNumbers.get(1L).size());
        Assertions.assertEquals("0", TelegramBot.userNumbers.get(1L).get(0).toString());

        update = prepareUpdate("Далее", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertEquals(1, telegramBot.showItemsCommand.getNumbers().size());
        Assertions.assertEquals("0", telegramBot.showItemsCommand.getNumbers().get(0).toString());

        update = prepareUpdate("Назад", chat, user);
        telegramBot.processNonCommandUpdate(update);
        Assertions.assertNull(TelegramBot.userCommand.get(1L));
        Assertions.assertTrue(TelegramBot.userNumbers.get(1L).isEmpty());
    }

    private Update prepareUpdate(String text, Chat chat, User user) {
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        message.setText(text);
        message.setFrom(user);
        update.setMessage(message);
        return update;
    }
}
