package bot.commands;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import bot.keyboards.Keyboards;

import static org.junit.Assert.*;

public class ShowItemsCommandTest {
    private final ArrayList<Integer> numbers = new ArrayList<>();
    private User user = Mockito.mock(User.class);
    private Chat chat = Mockito.mock(Chat.class);
    private AbsSenderMock absSenderMock = new AbsSenderMock();
    private ShowItemsCommand showItemsCommand;

    @Before
    public void setUp() {
        showItemsCommand = new ShowItemsCommand("showitems", "Отобразить товары");
        showItemsCommand.execute(absSenderMock, user, chat, null);
    }

//    @Test
//    public void execute() {
//        ShowItemsCommand showItemsCommand = new ShowItemsCommand("showitems", "Отобразить товары");
//        showItemsCommand.execute(absSenderMock, user, chat, null);
//    }

    @Test
    public void execute2BoundaryValueAnalysis() {
        showItemsCommand.setNumbers(numbers);
        Assert.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(0));
        Assert.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(1));
        Assert.assertTrue(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(16));
        Assert.assertTrue(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(17));
        Assert.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
    }

    @Test
    public void execute2EquivalencePartitioning() {
        showItemsCommand.setNumbers(null);
        Assert.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(numbers);
        Assert.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(-10));
        Assert.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(10));
        Assert.assertTrue(showItemsCommand.execute2(absSenderMock, user, chat, null));
        showItemsCommand.setNumbers(setupNumber(25));
        Assert.assertFalse(showItemsCommand.execute2(absSenderMock, user, chat, null));
    }

    @Test
    public void setNumbers() {
        numbers.add(1);
        showItemsCommand.setNumbers(numbers);
        Assert.assertEquals(numbers, showItemsCommand.numbers);
    }

    private ArrayList<Integer> setupNumber(int a) {
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(a);
        return nums;
    }
}
