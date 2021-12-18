package bot.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import bot.keyboards.Keyboards;
import database.JDBCConnector;
import utils.Utils;

/**
 * Команда "Город"
 */
public class ChooseCityCommand extends ServiceCommand {
    char[] message = null;
    public ChooseCityCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                "Введите Ваш город");
        JDBCConnector jdbcConnector = new JDBCConnector();
        try {
            while (message == null || message.length == 0) {
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
        }
        while (!jdbcConnector.addCity(userName, Arrays.toString(message).trim())) {
            message = null;
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Город не найден, пожалуйста, повторите ввод");
            try {
                while (message == null || message.length == 0) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
            }
        }
        message = null;

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

    public void setMessage(char[] message) {
        this.message = message;
    }
}
