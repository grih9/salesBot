package bot.commands;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public class ChooseShopsCommandTest {
    private final ArrayList<Integer> numbers = new ArrayList<>();
    private static User user = Mockito.mock(User.class);
    private static Chat chat = Mockito.mock(Chat.class);
    private static AbsSenderMock absSenderMock = new AbsSenderMock();
    private static ChooseShopsCommand chooseShopsCommand;

    @BeforeAll
    static void setUp() {
        chooseShopsCommand = new ChooseShopsCommand("shops", "Выбрать магазины");
        chooseShopsCommand.execute(absSenderMock, user, chat, null);
    }

//    @Test
//    public void execute() {
//    }

    @Test
    public void execute2BoundaryValueAnalysis() {
        chooseShopsCommand.setNumbers(null);
        Assertions.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(numbers);
        Assertions.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(0));
        Assertions.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(1));
        Assertions.assertTrue(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(10));
        Assertions.assertTrue(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(11));
        Assertions.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
    }

    @Test
    public void execute2EquivalencePartitioning() {
        chooseShopsCommand.setNumbers(numbers);
        Assertions.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(-10));
        Assertions.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(5));
        Assertions.assertTrue(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
        chooseShopsCommand.setNumbers(setupNumber(15));
        Assertions.assertFalse(chooseShopsCommand.execute2(absSenderMock, user, chat, null));
    }

    @Test
    public void setNumbers() {
        numbers.add(1);
        ChooseShopsCommand chooseShopsCommand = new ChooseShopsCommand("shops", "Выбрать магазины");
        chooseShopsCommand.setNumbers(numbers);
        Assertions.assertEquals(numbers, chooseShopsCommand.numbers);
    }

    private ArrayList<Integer> setupNumber(int a) {
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(a);
        return nums;
    }
}
