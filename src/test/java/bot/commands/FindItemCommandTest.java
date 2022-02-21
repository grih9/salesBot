package bot.commands;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import database.JDBCConnector;
import database.Shop;

public class FindItemCommandTest {
    private final User user = Mockito.mock(User.class);
    private final Chat chat = Mockito.mock(Chat.class);
    private final AbsSenderMock absSenderMock = new AbsSenderMock();
    private FindItemCommand findItemCommand;

    @BeforeEach
    public void setUp() {
        findItemCommand = new FindItemCommand("finditem", "Найти товар");
        findItemCommand.execute(absSenderMock, user, chat, null);
        user.setUserName("anilochka");
    }

//    @Test
//    public void execute() {
//    }

    @Test
    public void executePart2() {
        List<Shop> shops = new ArrayList<>();
        findItemCommand.setMessage("м");
        shops.add(new Shop("Дикси","https://dixy.ru"));
        JDBCConnector.setSelectedShops(user.getUserName(), shops);

        //Assertions.assertTrue(findItemCommand.executePart2(absSenderMock, user, chat, null));

        findItemCommand.setMessage(null);
        Assertions.assertFalse(findItemCommand.executePart2(absSenderMock, user, chat, null));

        findItemCommand.setMessage("");
        Assertions.assertFalse(findItemCommand.executePart2(absSenderMock, user, chat, null));

        findItemCommand.setMessage("abcdefghijklmnopqrstuvwxwz");
        Assertions.assertFalse(findItemCommand.executePart2(absSenderMock, user, chat, null));

        shops = new ArrayList<>();
        JDBCConnector.setSelectedShops(user.getUserName(), shops);
    }

    @Test
    public void setMessage() {
        FindItemCommand findItemCommand = new FindItemCommand("finditem", "Найти товар");
        String message = "message";
        findItemCommand.setMessage(message);
        Assertions.assertEquals(message, findItemCommand.message);
    }
}
