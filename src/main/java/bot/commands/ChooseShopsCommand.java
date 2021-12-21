package bot.commands;

import bot.keyboards.Keyboards;
import database.JDBCConnector;
import database.Shop;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

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
        StringBuilder msg = new StringBuilder("Выберите одну или более торговую сеть\n");
        int iter = 1;
        for (Shop shop : shops) {
            msg.append(String.format("%d %s\n", iter, shop.getName()));
            iter++;
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chat.getId()));
        sendMessage.setText(msg.toString());
        Keyboards keyboards = new Keyboards();
        keyboards.setButtonToCallNumbers(sendMessage, true);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Boolean execute2(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        JDBCConnector jdbcConnector = new JDBCConnector();
        List<Shop> shops = jdbcConnector.getShops(userName);
        List<Shop> selectedShops = new ArrayList<>();

        if (numbers.size() == 0) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    "Выберите одну или более торговую сеть");
            return false;
        }

        LinkedHashSet<Integer> noDuplArray = new LinkedHashSet<>(numbers);

        for (int elem : noDuplArray) {
            System.out.println("elem " + elem);
            if (elem > shops.size()) {
                super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                        "Номера сети " + elem + " нет в списке");
                return false;
            }

            selectedShops.add(shops.get(elem - 1));
        }

        jdbcConnector.setSelectedShops(userName, selectedShops);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chat.getId()));
        sendMessage.setText("Магазины успешно выбраны");
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

    public static ArrayList<Integer> justUniques(ArrayList<Integer> list) {
        Collections.sort(list);
        ArrayList<Integer> res = new ArrayList<>();

        res.add(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            if (!Objects.equals(list.get(i), list.get(i - 1))) {
                res.add(list.get(i));
            }
        }
        return res;
    }

    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }
}
