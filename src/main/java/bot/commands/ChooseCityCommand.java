package bot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import bot.NonCommand;
import database.JDBCConnector;
import utils.Utils;

/**
 * Команда "Город"
 */
public class ChooseCityCommand extends ServiceCommand {
    public ChooseCityCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                "Пожалуйста, введите город");
        JDBCConnector jdbcConnector = new JDBCConnector();
        while (jdbcConnector.getUserCity(userName) == null) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Пожалуйста, введите город");
            NonCommand nonCommand = new NonCommand();
//            jdbcConnector.addCity(userName, /*nonCommand*/);
        }
    }
}
