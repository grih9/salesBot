package bot.commands;

import bot.NonCommand;
import bot.keyboards.Keyboards;
import database.JDBCConnector;
import database.Shop;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Команда "Выбрать магазины"
 */
public class ChooseShopsCommand extends ServiceCommand {
    ArrayList<Integer> numbers = new ArrayList<>();
    public ChooseShopsCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        JDBCConnector jdbcConnector = new JDBCConnector();

        List<Shop> shops = jdbcConnector.getShops(userName);
        StringBuilder message = new StringBuilder("Введите номера магазинов из списка через запятую");

        int iter = 1;
        for (Shop shop: shops) {
            message.append(String.format("%d %s\n", iter, shop.getName()));
            iter++;
        }

        NonCommand nonCommand = new NonCommand();
        int []numberList = {}; // TODO: = nonCommand... получает список номеров

        List<Shop> selectedShops = new LinkedList<>();
        for (int number: numberList) {
            selectedShops.add(shops.get(number - 1));
        }

        jdbcConnector.setSelectedShops(userName, selectedShops);
    }

//    отправка клавиатуры
//    SendMessage sendMessage = new SendMessage();
//    sendMessage.setChatId(String.valueOf(chat.getId()));
//    sendMessage.setText("Выберите одну или более торговую сеть");
//    Keyboards keyboards = new Keyboards();
//    keyboards.setButtonToCallNumbers(sendMessage, 1);
//    try {
//        absSender.execute(sendMessage);
//    } catch (TelegramApiException e) {
//        e.printStackTrace();
//    }

    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }
}
