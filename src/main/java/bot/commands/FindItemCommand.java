package bot.commands;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import bot.Item;
import bot.NonCommand;
import database.JDBCConnector;
import database.Shop;
import parser.Parser;
import utils.Utils;

/**
 * Команда "Найти товар"
 */
public class FindItemCommand extends ServiceCommand {
    List<Shop> shops; // TODO: добавление выбранных магазинов

    public FindItemCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, "Введите название товара");

        NonCommand nonCommand = new NonCommand();
        String itemName = null; // TODO: = nonCommand... получает и передает название

        JDBCConnector jdbcConnector = new JDBCConnector();
        Parser.setCity(jdbcConnector.getUserCity(Utils.getUserName(user)));
        Parser.setShops(shops);

        List<Item> items = Parser.findItemsByName(itemName);
        if (items == null || items.isEmpty()) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Такого товара нет в каталогах акций выбранных магазинов");
            return;
        }
        for (Item item : items) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, item.toString());
            // TODO: добавить отправку изображения
        }
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }
}
