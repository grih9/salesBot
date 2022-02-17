package bot.commands;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import database.JDBCConnector;
import database.Shop;

public class FindItemCommandTest {
    private User user = Mockito.mock(User.class);
    private Chat chat = Mockito.mock(Chat.class);
    private AbsSenderMock absSenderMock = new AbsSenderMock();
    private FindItemCommand findItemCommand;

    @Before
    public void setUp() {
        findItemCommand = new FindItemCommand("finditem", "Найти товар");
        findItemCommand.execute(absSenderMock, user, chat, null);
        user.setUserName("testtesttest");
    }

//    @Test
//    public void execute() {
//    }

    @Test
    public void executePart2() {
        List<Shop> shops = new ArrayList<>();
        JDBCConnector.setSelectedShops(user.getUserName(), shops);
        findItemCommand.setMessage("хлеб");
        shops.add(new Shop("Дикси","https://dixy.ru"));
        JDBCConnector.setSelectedShops(user.getUserName(), shops);

        Assert.assertTrue(findItemCommand.executePart2(absSenderMock, user, chat, null));

        findItemCommand.setMessage(null);
        Assert.assertFalse(findItemCommand.executePart2(absSenderMock, user, chat, null));

        findItemCommand.setMessage("");
        Assert.assertFalse(findItemCommand.executePart2(absSenderMock, user, chat, null));

        findItemCommand.setMessage("abcdefghijklmnopqrstuvwxwz");
        Assert.assertFalse(findItemCommand.executePart2(absSenderMock, user, chat, null));

        shops = new ArrayList<>();
        JDBCConnector.setSelectedShops(user.getUserName(), shops);
    }

    @Test
    public void setMessage() {
        FindItemCommand findItemCommand = new FindItemCommand("finditem", "Найти товар");
        String message = "message";
        findItemCommand.setMessage(message);
        Assert.assertEquals(message, findItemCommand.message);
    }
}
