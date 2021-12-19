package bot.commands;

import java.util.ArrayList;
import java.util.List;

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
    String message = null;
    public ChooseCityCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, "Введите Ваш город");
    }

    public boolean executePart2(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        JDBCConnector jdbcConnector = new JDBCConnector();
        if (!jdbcConnector.addCity(userName,message.trim())) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Город не найден, пожалуйста, повторите ввод");
            return false;
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chat.getId()));
        sendMessage.setText("Выберите интересующие магазины");
        Keyboards keyboards = new Keyboards();
        List<String> commands = new ArrayList<>();
        commands.add("Выбрать магазины");
        keyboards.setButtonToCallCommand(sendMessage, commands);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
