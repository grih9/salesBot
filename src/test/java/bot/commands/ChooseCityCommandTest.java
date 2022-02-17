package bot.commands;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public class ChooseCityCommandTest {
    private User user = Mockito.mock(User.class);
    private Chat chat = Mockito.mock(Chat.class);
    private AbsSenderMock absSenderMock = new AbsSenderMock();
    private ChooseCityCommand chooseCityCommand;

    @Before
    public void setUp() {
        chooseCityCommand = new ChooseCityCommand("city", "Город");
        chooseCityCommand.execute(absSenderMock, user, chat, null);
    }

//    @Test
//    public void execute() {
//    }

    @Test
    public void executePart2() {
        chooseCityCommand.setMessage(null);
        Assert.assertFalse(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
        chooseCityCommand.setMessage("");
        Assert.assertFalse(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
        chooseCityCommand.setMessage("123");
        Assert.assertFalse(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
        chooseCityCommand.setMessage("авбр");
        Assert.assertFalse(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
        chooseCityCommand.setMessage("Санкт-Петербург");
        Assert.assertTrue(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
    }

    @Test
    public void setMessage() {
        String message = "message";
        chooseCityCommand.setMessage(message);
        Assert.assertEquals(message, chooseCityCommand.message);
    }
}
