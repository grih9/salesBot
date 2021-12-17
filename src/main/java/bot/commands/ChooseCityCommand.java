package bot.commands;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import bot.NonCommand;
import bot.keyboards.Keyboards;
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
                "Введите Ваш город");
        JDBCConnector jdbcConnector = new JDBCConnector();
        if (jdbcConnector.getUserCity(userName) == null) {
            NonCommand nonCommand = new NonCommand();
            Message message = new Message();
            message.getText();
//       if (!jdbcConnector.addCity(userName, /*nonCommand*/)) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Город не найден, пожалуйста, повторите ввод");
//        }
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chat.getId()));
        Keyboards keyboards = new Keyboards();
        List<String> commands = new ArrayList<>();
        commands.add("/shops");
        keyboards.setButtonToCallCommand(sendMessage, commands);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            //e.printStackTrace();
        }
    }
}
