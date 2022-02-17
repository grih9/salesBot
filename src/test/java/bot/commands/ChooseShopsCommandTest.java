package bot.commands;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public class ChooseShopsCommandTest {
    private final ArrayList<Integer> numbers = new ArrayList<>();
    private User user = Mockito.mock(User.class);
    private Chat chat = Mockito.mock(Chat.class);
    private AbsSenderMock absSenderMock = new AbsSenderMock();
    private ChooseShopsCommand chooseShopsCommand;

    @Before
    public void setUp() {
        chooseShopsCommand = new ChooseShopsCommand("shops", "Выбрать магазины");
        chooseShopsCommand.execute(absSenderMock, user, chat, null);
    }

//    @Test
//    public void execute() {
//    }

    @Test
    public void execute2BoundaryValueAnalysis() {
        chooseShopsCommand.setNumbers(null);
        Assert.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(numbers);
        Assert.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(0));
        Assert.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(1));
        Assert.assertTrue(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(10));
        Assert.assertTrue(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(11));
        Assert.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
    }

    @Test
    public void execute2EquivalencePartitioning() {
        chooseShopsCommand.setNumbers(numbers);
        Assert.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(-10));
        Assert.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(5));
        Assert.assertTrue(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(15));
        Assert.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
    }

    @Test
    public void setNumbers() {
        numbers.add(1);
        ChooseShopsCommand chooseShopsCommand = new ChooseShopsCommand("shops", "Выбрать магазины");
        chooseShopsCommand.setNumbers(numbers);
        Assert.assertEquals(numbers, chooseShopsCommand.numbers);
    }

    private ArrayList<Integer> setupNumber(int a) {
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(a);
        return nums;
    }
}
