package bot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import bot.NonCommand;
import database.JDBCConnector;
import utils.Utils;

/**
 * Команда "Старт"
 */
public class StartCommand extends ServiceCommand {
    public StartCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                "Давайте начнём!");
        JDBCConnector jdbcConnector = new JDBCConnector();
        jdbcConnector.addUser(userName);
        while (jdbcConnector.getUserCity(userName) == null) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Пожалуйста, введите город");
            NonCommand nonCommand = new NonCommand();
//            jdbcConnector.addCity(userName, /*nonCommand*/);
        }
    }
}
