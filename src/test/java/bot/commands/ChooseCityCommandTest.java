package bot.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public class ChooseCityCommandTest {
    private static User user = Mockito.mock(User.class);
    private static Chat chat = Mockito.mock(Chat.class);
    private static AbsSenderMock absSenderMock = new AbsSenderMock();
    private static ChooseCityCommand chooseCityCommand;

    @BeforeAll
    static void setUp() {
        chooseCityCommand = new ChooseCityCommand("city", "Город");
        chooseCityCommand.execute(absSenderMock, user, chat, null);
    }

//    @Test
//    public void execute() {
//    }

    @Test
    public void executePart2() {
        chooseCityCommand.setMessage(null);
        Assertions.assertFalse(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
        chooseCityCommand.setMessage("");
        Assertions.assertFalse(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
        chooseCityCommand.setMessage("123");
        Assertions.assertFalse(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
        chooseCityCommand.setMessage("авбр");
        Assertions.assertFalse(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
        chooseCityCommand.setMessage("Санкт-Петербург");
        Assertions.assertTrue(chooseCityCommand.executePart2(absSenderMock, user, chat, null));
    }

    @Test
    public void setMessage() {
        String message = "message";
        chooseCityCommand.setMessage(message);
        Assertions.assertEquals(message, chooseCityCommand.message);
    }
}
