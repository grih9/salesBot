package integration;

import bot.Command;
import bot.commands.AbsSenderMock;
import database.JDBCConnector;
import org.junit.jupiter.api.*;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;

public class UserIntegrationIT {
    private static final User user = new User();
    private static final Chat chat = new Chat();
    private static final String username = "testUserr";
    private static final JDBCConnector jdbcConnector = new JDBCConnector(false);

    @BeforeAll
    static void allSetUp() {
        user.setUserName(username);
        chat.setId(1L);
    }

    @BeforeEach
    void eachSetUp() {
        AbsSenderMock absSenderMock = new AbsSenderMock();
        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.START);
        absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate("/start", chat, user));
    }

    @AfterEach
    void clean() {
        Assertions.assertTrue(jdbcConnector.deleteUser(username));
    }

    @Test
    void testScenario1() {
        Assertions.assertNotNull(jdbcConnector.getUserId(username));
    }

    @Test
    void testScenario2() { AbsSenderMock absSenderMock = new AbsSenderMock();
        String city = "Санкт-Петербург";

        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CITY);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/city", chat, user));
        Assertions.assertTrue(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate(city, chat, user)));

        Assertions.assertEquals(jdbcConnector.getUserCity(username).getName(), city);
    }

    @Test
    void testScenario3() {
        String city = "Санкт";
        AbsSenderMock absSenderMock = new AbsSenderMock();

        AbsSenderMock.userNumbers.put(chat.getId(), new ArrayList<>());
        AbsSenderMock.userCommand.put(chat.getId(), Command.CITY);

        absSenderMock.processNonCommandUpdate(Util.prepareUpdate("/city", chat, user));
        Assertions.assertFalse(absSenderMock.processNonCommandUpdateBoolean(Util.prepareUpdate(city, chat, user)));

        Assertions.assertNull(jdbcConnector.getUserCity(username));
    }
}
