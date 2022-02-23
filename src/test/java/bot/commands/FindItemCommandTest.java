package bot.commands;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import database.JDBCConnector;
import database.Shop;

public class FindItemCommandTest {
    private User user = new User();
    private Chat chat = Mockito.mock(Chat.class);
    private final AbsSenderMock absSenderMock = new AbsSenderMock();
    private FindItemCommand findItemCommand = new FindItemCommand("finditem", "Найти товар");

    @BeforeEach
    public void setUp() {
        findItemCommand.execute(absSenderMock, user, chat, null);
        user.setUserName("anilochka");
        List<Shop> shops = new ArrayList<>();
        shops.add(new Shop("Дикси","https://dixy.ru"));
        JDBCConnector.setSelectedShops(user.getUserName(), shops);
    }

    @Test
    public void executePart2() {
        findItemCommand.setMessage("сок");
        Assertions.assertTrue(findItemCommand.executePart2(absSenderMock, user, chat, null));

        findItemCommand.setMessage(null);
        Assertions.assertFalse(findItemCommand.executePart2(absSenderMock, user, chat, null));

        findItemCommand.setMessage("");
        Assertions.assertFalse(findItemCommand.executePart2(absSenderMock, user, chat, null));

        findItemCommand.setMessage("abcdefghijklmnopqrstuvwxwz");
        Assertions.assertFalse(findItemCommand.executePart2(absSenderMock, user, chat, null));
    }

    @Test
    public void setMessage() {
        FindItemCommand findItemCommand = new FindItemCommand("finditem", "Найти товар");
        String message = "message";
        findItemCommand.setMessage(message);
        Assertions.assertEquals(message, findItemCommand.message);
    }
}
