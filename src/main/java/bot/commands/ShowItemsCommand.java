package bot.commands;

import bot.Item;
import bot.NonCommand;
import database.City;
import database.JDBCConnector;
import database.Shop;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parser.Parser;
import utils.Utils;

import java.util.List;

/**
 * Команда "Отобразить товары"
 */
public class ShowItemsCommand extends ServiceCommand {
    public ShowItemsCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        JDBCConnector jdbcConnector = new JDBCConnector();
        List<String> categories = jdbcConnector.getCategories();
        List<Shop> selectedShops = jdbcConnector.getSelectedShops(userName);
        City city = jdbcConnector.getUserCity(userName);

        StringBuilder message = new StringBuilder("Введите категории товаров через запятую");
        int iter = 1;
        for (String category: categories) {
            message.append(String.format("%d %s\n", iter, category));
            iter++;
        }

        NonCommand nonCommand = new NonCommand();
        int []numberList = {}; // TODO: = nonCommand... получает список номеров

        for (Shop shop : selectedShops) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    shop.getName());
            for (int i : numberList) {
                // TODO: поймать исключение и прописать ошибку "нет категории с таким номером"
                super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                        categories.get(i-1));
                List<Item> items = Parser.findItemsByCategory(categories.get(i-1), city, shop);
                for (Item item : items) {
                    super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, item.toString());
                    // TODO: добавить отправку изображения
                }
            }
        }
    }
}
