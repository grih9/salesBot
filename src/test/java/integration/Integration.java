package integration;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import bot.Command;
import bot.commands.AbsSenderMock;
import database.JDBCConnector;
import database.Shop;

public class Integration {
    private static final User user = new User();
    private static final Chat chat = new Chat();
    private final JDBCConnector jdbcConnector = new JDBCConnector(false);

    @BeforeAll
    static void setUp() {
        user.setUserName("anilochka");
        chat.setId(1L);
    }

    @Test
    void testScenario7() {
        AbsSenderMock absSenderMock = new AbsSenderMock();
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("25,0,22", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.SHOW_ITEMS);
        absSenderMock.processNonCommandUpdate(prepareUpdate("/showitems", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("25,0,22", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario8() {
        AbsSenderMock absSenderMock = new AbsSenderMock();
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("1", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.FIND_ITEM);
        absSenderMock.processNonCommandUpdate(prepareUpdate("/finditem", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("хлеб", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario9() {
        AbsSenderMock absSenderMock = new AbsSenderMock();
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("25,0,22", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));

        absSenderMock.processNonCommandUpdate(prepareUpdate("/finditem", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("хлеб", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario10() {
        AbsSenderMock absSenderMock = new AbsSenderMock();
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("8,9", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.FIND_ITEM);
        absSenderMock.processNonCommandUpdate(prepareUpdate("/finditem", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario11() {
        AbsSenderMock absSenderMock = new AbsSenderMock();
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("25,0,22", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.FIND_ITEM);
        absSenderMock.processNonCommandUpdate(prepareUpdate("/finditem", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario12() {
        AbsSenderMock absSenderMock = new AbsSenderMock();
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("8,9,", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(prepareUpdate("Далее", chat, user)));

        List<Shop> numbers = new ArrayList<>();
        numbers.add(new Shop("О'КЕЙ","https://edadeal.ru/sankt-peterburg/retailers/okmarket-giper"));
        numbers.add(new Shop("Пятёрочка","https://edadeal.ru/sankt-peterburg/retailers/5ka"));
        Assertions.assertEquals(numbers, jdbcConnector.getSelectedShops(user.getUserName()));
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
