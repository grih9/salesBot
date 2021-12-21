package bot.commands;

import bot.Item;
import bot.keyboards.Keyboards;
import database.City;
import database.JDBCConnector;
import database.Shop;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import parser.Parser;
import utils.Utils;

import java.util.*;

/**
 * Команда "Отобразить товары"
 */
public class ShowItemsCommand extends ServiceCommand {
    private static final String MESSAGE_FOR_SENDING_KEYBOARD_TO_RESEARCH = "Хотите выполнить повторный поиск?\n" +
            "Найти товар - поиск акционных товаров по названию" +
            "Отобразить товары - поиск акционных товаров по категориям\n" +
            "/shops - вернуться к выбору магазинов";
    ArrayList<Integer> numbers = new ArrayList<>();

    public ShowItemsCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        JDBCConnector jdbcConnector = new JDBCConnector();
        List<String> categories = jdbcConnector.getCategories();

        StringBuilder msg = new StringBuilder("Введите категории товаров\n");
        int iter = 1;
        for (String category : categories) {
            msg.append(String.format("%d %s\n", iter, category));
            iter++;
        }

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

        LinkedHashSet<Integer> noDuplArray = new LinkedHashSet<>(numbers);
        numbers = new ArrayList<>(noDuplArray);

        for (Shop shop : selectedShops) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    shop.getName());
            boolean hasItems = false;
            for (int i : numbers) {
                System.out.println("findElem " + i);
                if (i > categories.size()) {
                    super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                            "Номера категории " + i + " нет в списке");
                    return false;
                }
                super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                            categories.get(i-1));
                System.out.println("cat " + categories.get(i-1));
                System.out.println("name " + shop.getName());
                System.out.println("website " + shop.getWebsite());
                List<Item> items = Parser.findItemsByCategory(categories.get(i-1), city, shop);
                if (items.isEmpty()) {
                    super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                            "Акционных товаров в данной категории нет");
                    continue;
                }
                hasItems = true;
                items.sort(Comparator.naturalOrder());
                for (Item item : items) {
                    super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, item.toString());
                }
            }
            if (!hasItems) {
                super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                        "Акционных товаров в данной сети нет");
            }
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chat.getId()));
        sendMessage.setText(MESSAGE_FOR_SENDING_KEYBOARD_TO_RESEARCH);
        Keyboards keyboards = new Keyboards();
        List<String> commands = new ArrayList<>();
        commands.add("Найти товар");
        commands.add("Отобразить товары");
        keyboards.setButtonToCallCommand(sendMessage, commands);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }
}
