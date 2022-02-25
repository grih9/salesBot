package bot.commands;

import database.JDBCConnector;
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
    private static final String MESSAGE = "message";
    private static final JDBCConnector jdbc = new JDBCConnector(false);

    @BeforeAll
    static void setUp() {
        chooseCityCommand = new ChooseCityCommand("city", "Город", jdbc);
        chooseCityCommand.execute(absSenderMock, user, chat, null);
        chooseCityCommand.setMessage(MESSAGE);
    }

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
    public void getMessage() {
        Assertions.assertEquals(MESSAGE, chooseCityCommand.getMessage());
    }

    @Test
    public void setMessage() {
        String message = "message1";
        chooseCityCommand.setMessage(message);
        Assertions.assertEquals(message, chooseCityCommand.getMessage());
        chooseCityCommand.setMessage(MESSAGE);
    }
}
