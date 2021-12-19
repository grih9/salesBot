package bot.commands;

import bot.Item;
import bot.NonCommand;
import database.City;
import database.JDBCConnector;
import database.Shop;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import parser.Parser;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Команда "Отобразить товары"
 */
public class ShowItemsCommand extends ServiceCommand {
    String message = null;//поле теперь не нужно
    ArrayList<Integer> numbers = new ArrayList<>();

    public ShowItemsCommand(String identifier, String description) {
        super(identifier, description);
    }

    public void setMessage(String message) { //теперь не нужно
        this.message = message;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        JDBCConnector jdbcConnector = new JDBCConnector();
        List<String> categories = jdbcConnector.getCategories();

        StringBuilder msg = new StringBuilder("Введите категории товаров через запятую");
        int iter = 1;
        for (String category : categories) {
            msg.append(String.format("%d %s\n", iter, category));
            iter++;
        }

        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, msg.toString());

        //    отправка клавиатуры
        //    SendMessage sendMessage = new SendMessage();
        //    sendMessage.setChatId(String.valueOf(chat.getId()));
        //    sendMessage.setText("Выберите категории товаров");
        //    Keyboards keyboards = new Keyboards();
        //    keyboards.setButtonToCallNumbers(sendMessage, 0);
        //    try {
        //        absSender.execute(sendMessage);
        //    } catch (TelegramApiException e) {
        //        e.printStackTrace();
        //    }
    }

    public void execute2(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        JDBCConnector jdbcConnector = new JDBCConnector();
        List<String> categories = jdbcConnector.getCategories();
        List<Shop> selectedShops = jdbcConnector.getSelectedShops(userName);
        City city = jdbcConnector.getUserCity(userName);

        NonCommand nonCommand = new NonCommand();
        int []numberList = nonCommand.getNumbers(message);
        for (Shop shop : selectedShops) {
            super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                    shop.getName());
            for (int i : numberList) {
                try {
                    super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                            categories.get(i-1));
                } catch (IndexOutOfBoundsException e) {
                    super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                            "Нет категории с номером: " + i + ". Пожалуйста, вводите номера " +
                                    "категорий товаров через запятую");
                }
                List<Item> items = Parser.findItemsByCategory(categories.get(i-1), city, shop);
                for (Item item : items) {
                    super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName, item.toString());
                }
            }
        }
    }

    public void setNumbers(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }
}
