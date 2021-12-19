package bot.commands;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import bot.keyboards.Keyboards;
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
        String botInfo = "Давайте начнём!\n" +
                "С помощью бота Вы можете искать акционные товары\n\n" +
                "Команды:\n" +
                "/city - Изменить город\n" +
                "/finditem - Найти акционный товар по названию\n" +
                "/showitems - Найти акции по категориям товаров";
        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, botInfo);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chat.getId()));
        sendMessage.setText("Для начала выберите свой город");
        Keyboards keyboards = new Keyboards();
        List<String> commands = new ArrayList<>();
        commands.add("Выбрать город");
        keyboards.setButtonToCallCommand(sendMessage, commands);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
