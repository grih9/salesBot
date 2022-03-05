package integration;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import bot.Command;
import bot.commands.AbsSenderMock;
import database.JDBCConnector;
import database.Shop;

public class IntegrationIT {
    private static final User user = new User();
    private static final Chat chat = new Chat();
    private final JDBCConnector jdbcConnector = new JDBCConnector(false);
    private final AbsSenderMock absSenderMock = new AbsSenderMock();

    @BeforeAll
    static void setUp() {
        user.setUserName("anilochka");
        chat.setId(1L);
    }

    @Test
    void testScenario4() {
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("3", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("6", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("10", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.SHOW_ITEMS);
        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/showitems", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("1", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("4", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("12", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario5() {
        user.setUserName("testUser");

        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.START);
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("/start", chat, user));

        String city = "Санкт-Петербург";

        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CITY);
        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/city", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate(city, chat, user));

        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("-1", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("15", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("3", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.SHOW_ITEMS);
        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/showitems", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("1", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("4", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("12", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        jdbcConnector.deleteUser("testUser");
        user.setUserName("anilochka");
    }

    @Test
    void testScenario6() {
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("3", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("6", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("10", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.SHOW_ITEMS);
        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/showitems", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("-1", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("4", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("20", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario7() {
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("25,0,22", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.SHOW_ITEMS);
        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/showitems", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("25,0,22", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario8() {
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("1", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.FIND_ITEM);
        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/finditem", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("хлеб", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario9() {
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("25,0,22", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/finditem", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("хлеб", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario10() {
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("8,9", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.FIND_ITEM);
        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/finditem", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario11() {
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("25,0,22", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        AbsSenderMock.userCommand.put(chat.getId(), Command.FIND_ITEM);
        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/finditem", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));
    }

    @Test
    void testScenario12() {
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CHOSE_SHOPS);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/shops", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("8,9,", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));

        List<Shop> numbers = new ArrayList<>();
        numbers.add(new Shop("О'КЕЙ","https://edadeal.ru/sankt-peterburg/retailers/okmarket-giper"));
        numbers.add(new Shop("Пятёрочка","https://edadeal.ru/sankt-peterburg/retailers/5ka"));
        Assertions.assertEquals(numbers, jdbcConnector.getSelectedShops(user.getUserName()));
    }

    @Test
    void testScenario19() {
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());

        List<Shop> numbers = new ArrayList<>();
        numbers.add(new Shop("О'КЕЙ","https://edadeal.ru/sankt-peterburg/retailers/okmarket-giper"));
        jdbcConnector.setSelectedShops(user.getUserName(), numbers);

        AbsSenderMock.userCommand.put(chat.getId(), Command.SHOW_ITEMS);
        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/showitems", chat, user));
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("8,8,8,8,5,8", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("Далее", chat, user)));
    }
}
