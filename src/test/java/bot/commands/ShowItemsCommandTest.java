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


public class ShowItemsCommandTest {
    private ArrayList<Integer> numbers = new ArrayList<>();
    private User user = new User();
    private Chat chat = Mockito.mock(Chat.class);
    private final AbsSenderMock absSenderMock = new AbsSenderMock();
    private ShowItemsCommand showItemsCommand = new ShowItemsCommand("showitems", "Отобразить товары");

    @BeforeEach
    public void setUp() {
        showItemsCommand.execute(absSenderMock, user, chat, null);
        user.setUserName("anilochka");
        List<Shop> shops = new ArrayList<>();
        shops.add(new Shop("Дикси","https://dixy.ru"));
        JDBCConnector.setSelectedShops(user.getUserName(), shops);
    }

    @Test
    public void execute2BoundaryValueAnalysis() {
        showItemsCommand.setNumbers(numbers);
        Assertions.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(0));
        Assertions.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(1));
        Assertions.assertTrue(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(16));
        Assertions.assertTrue(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(17));
        Assertions.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
    }

    @Test
    public void execute2EquivalencePartitioning() {
        showItemsCommand.setNumbers(null);
        Assertions.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(numbers);
        Assertions.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(-10));
        Assertions.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(10));
        Assertions.assertTrue(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(25));
        Assertions.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
    }

    @Test
    public void setNumbers() {
        numbers.add(1);
        showItemsCommand.setNumbers(numbers);
        Assertions.assertEquals(numbers, showItemsCommand.numbers);
    }

    private ArrayList<Integer> setupNumber(int a) {
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(a);
        return nums;
    }
}
