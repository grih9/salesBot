package bot.commands;

import bot.Item;
import bot.NonCommand;
import bot.keyboards.Keyboards;
import database.City;
import database.JDBCConnector;
import database.Shop;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import parser.Parser;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Команда "Отобразить товары"
 */
public class ShowItemsCommand extends ServiceCommand {
    String message = null;//поле теперь не нужно
    ArrayList<Integer> numbers = new ArrayList<>();

    public ShowItemsCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        //String userName = Utils.getUserName(user);
        JDBCConnector jdbcConnector = new JDBCConnector();
        List<String> categories = jdbcConnector.getCategories();

        StringBuilder msg = new StringBuilder("Введите категории товаров\n");
        int iter = 1;
        for (String category : categories) {
            msg.append(String.format("%d %s\n", iter, category));
            iter++;
        }

        //super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, msg.toString());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chat.getId()));
        sendMessage.setText(msg.toString());
        Keyboards keyboards = new Keyboards();
        keyboards.setButtonToCallNumbers(sendMessage, false);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Boolean execute2(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        JDBCConnector jdbcConnector = new JDBCConnector();
        List<String> categories = jdbcConnector.getCategories();
        List<Shop> selectedShops = jdbcConnector.getSelectedShops(userName);
        City city = jdbcConnector.getUserCity(userName);

        if (numbers.size() == 0) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Выберите одну или более категорию");
            return false;
        }

        numbers = ChooseShopsCommand.justUniques(numbers);

        for (Shop shop : selectedShops) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    shop.getName());
            for (int i : numbers) {
                if (i > categories.size()) {
                    super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                            "Нет категории с таким номером: " + i );
                    return false;
                }
                super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                            categories.get(i-1));
                List<Item> items = Parser.findItemsByCategory(categories.get(i-1), city, shop);
                for (Item item : items) {
                    super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, item.toString());
                }
            }
        }
        return true;
    }


    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }
}
