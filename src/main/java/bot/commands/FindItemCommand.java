package bot.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import bot.Item;
import bot.keyboards.Keyboards;
import database.JDBCConnector;
import parser.Parser;
import utils.Utils;

/**
 * Команда "Найти товар"
 */
public class FindItemCommand extends ServiceCommand {
    String message = null;
    public FindItemCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, "Введите название товара");
    }

    public void executePart2(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        JDBCConnector jdbcConnector = new JDBCConnector();

        List<Item> items = Parser.findItemsByName(message.trim(), jdbcConnector.getUserCity(Utils.getUserName(user)),
                jdbcConnector.getSelectedShops(Utils.getUserName(user)));
        if (items == null || items.isEmpty()) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Такого товара нет в каталогах акций выбранных магазинов");
            return;
        }
        items.sort(Comparator.comparing(Item::getSalePrice));
        for (Item item : items) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, item.toString());
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chat.getId()));
        Keyboards keyboards = new Keyboards();
        List<String> commands = new ArrayList<>();
        commands.add("/finditem");
        commands.add("/showitems");
        keyboards.setButtonToCallCommand(sendMessage, commands);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
