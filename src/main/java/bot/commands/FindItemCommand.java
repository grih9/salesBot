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
import database.Shop;
import parser.Parser;
import utils.Utils;

/**
 * Команда "Найти товар"
 */
public class FindItemCommand extends ServiceCommand {
    private static final String MESSAGE_FOR_SENDING_KEYBOARD_TO_RESEARCH = "Хотите выполнить повторный поиск?\n" +
            "Найти товар - поиск акционных товаров по названию\n" +
            "Отобразить товары - поиск акционных товаров по категориям\n" +
            "/shops - вернуться к выбору магазинов";
    String message = null;
    private final JDBCConnector jdbc;

    public FindItemCommand(String identifier, String description, JDBCConnector jdbc) {
        super(identifier, description);
        this.jdbc = jdbc;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, "Введите название товара");
    }

    public Boolean executePart2(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        List<Shop> selectedShops = jdbc.getSelectedShops(Utils.getUserName(user));

        if (selectedShops == null || selectedShops.isEmpty()) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Выберите одну или более торговую сеть");
            showKeyboard(absSender, chat, "Выберите интересующие магазины", 1);
            return false;
        }

        if (message == null || message.isEmpty()) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Введите название товара");
            return false;
        }

        List<Item> items;
        boolean hasItems = false;
        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, "Выполняется поиск");
        for (Shop shop: selectedShops) {
            items = Parser.findItemsByName(message.trim(), jdbc.getUserCity(Utils.getUserName(user)), shop);
            if (items == null || items.isEmpty()) {
                continue;
            }
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Результаты поиска для магазина " + shop.getName());
            hasItems = true;
            items.sort(Comparator.naturalOrder());
            for (Item item : items) {
                super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, item.toString());
            }
        }

        if (!hasItems) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Такого товара нет в каталогах акций выбранных магазинов");
            showKeyboard(absSender, chat, MESSAGE_FOR_SENDING_KEYBOARD_TO_RESEARCH, 2);
            return false;
        }

        showKeyboard(absSender, chat, MESSAGE_FOR_SENDING_KEYBOARD_TO_RESEARCH, 2);
        return true;
    }

    private void showKeyboard(AbsSender absSender, Chat chat, String text, int buttonsCount) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chat.getId()));
        sendMessage.setText(text);
        Keyboards keyboards = new Keyboards();
        List<String> commands = new ArrayList<>();
        if (buttonsCount == 2) {
            commands.add("Найти товар");
            commands.add("Отобразить товары");
        } else if (buttonsCount == 1) {
            commands.add("Выбрать магазины");
        }
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

    public String getMessage() {
        return message;
    }
}
