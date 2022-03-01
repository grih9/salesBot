package bot.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import bot.Command;
import bot.NonCommand;
import bot.TelegramBot;
import bot.keyboards.Keyboards;
import database.JDBCConnector;
import utils.Utils;

public class AbsSenderMock extends TelegramLongPollingCommandBot {
    private final JDBCConnector jdbc = new JDBCConnector(false);
    StartCommand startCommand = new StartCommand("start", "Старт", this.jdbc);
    ChooseCityCommand chooseCityCommand = new ChooseCityCommand("city", "Город", this.jdbc);
    ChooseShopsCommand chooseShopsCommand = new ChooseShopsCommand("shops", "Выбрать магазины", this.jdbc);
    FindItemCommand findItemCommand = new FindItemCommand("finditem", "Найти товар", this.jdbc);
    ShowItemsCommand showItemsCommand = new ShowItemsCommand("showitems", "Отобразить товары", this.jdbc);

    public static Map<Long, Command> userCommand;
    public static Map<Long, ArrayList<Integer>> userNumbers;

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
    }

    public boolean processNonCommandUpdateBoolean(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = Utils.getUserName(msg);

        if (msg.getText() == null || msg.getText().isEmpty()) {
            return false;
        }
        switch (msg.getText().trim()) {
            case "/start":
                userCommand.put(chatId, Command.START);
                userNumbers.put(chatId, new ArrayList<>());
                startCommand.execute(this, update.getMessage().getFrom(), update.getMessage().getChat(), null);
                break;
            case "/city":
            case "Выбрать город":
                userCommand.put(chatId, Command.CITY);
                chooseCityCommand.execute(this, update.getMessage().getFrom(), update.getMessage().getChat(), null);
                break;
            case "/shops":
            case "Выбрать магазины":
                userCommand.put(chatId, Command.CHOSE_SHOPS);
                chooseShopsCommand.execute(this, update.getMessage().getFrom(), update.getMessage().getChat(), null);
                break;
            case "/finditem":
            case "Найти товар":
                userCommand.put(chatId, Command.FIND_ITEM);
                findItemCommand.execute(this, update.getMessage().getFrom(), update.getMessage().getChat(), null);
                break;
            case "/showitems":
            case "Отобразить товары":
                userCommand.put(chatId, Command.SHOW_ITEMS);
                showItemsCommand.execute(this, update.getMessage().getFrom(), update.getMessage().getChat(), null);
                break;
            default: //получили текст
                if (userCommand.get(chatId) == null) {
                    break;
                }
                if (userCommand.get(chatId).equals(Command.CITY)) { // получили город
                    chooseCityCommand.setMessage(msg.getText());
                    // Если город успешно записан в БД - текущая команда пользователя обнуляется
                    if (chooseCityCommand.executePart2(this, update.getMessage().getFrom(), update.getMessage().getChat(), null)) {
                        userCommand.put(chatId, null);
                    } else {
                        return false;
                    }
                } else if (userCommand.get(chatId).equals(Command.FIND_ITEM)) { // получили название товара
                    findItemCommand.setMessage(msg.getText());
                    userCommand.put(chatId, null);
                    return findItemCommand.executePart2(this, update.getMessage().getFrom(), update.getMessage().getChat(), null);
                } else if (userCommand.get(chatId).equals(Command.CHOSE_SHOPS)) { // получили результат нажатия кнопки клавиатуры
                    if (TelegramBot.isNumeric(msg.getText())) {
                        userNumbers.get(chatId).add(Integer.valueOf(msg.getText()));
                    } else if (msg.getText().equals(",")) {
                        break;
                    } else if (msg.getText().equals("Стереть") && userNumbers.get(chatId).size() > 0) {
                        userNumbers.get(chatId).remove(userNumbers.get(chatId).size() - 1);
                    } else if (msg.getText().contains(",")) {
                        NonCommand nonComand = new NonCommand();
                        if (nonComand.checkValid(msg.getText())) {
                            int[] array = nonComand.getNumbers(msg.getText());
                            for (int elem : array) {
                                userNumbers.get(chatId).add(elem);
                            }
                        } else {
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(String.valueOf(chatId));
                            sendMessage.setText("Пожалуйста, введите номера торговых сетей через запятую" +
                                    " или воспользуйтесь клавиатурой для ввода чисел");
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    } else if (msg.getText().equals("Далее")) {
                        chooseShopsCommand.setNumbers(userNumbers.get(chatId));
                        userNumbers.put(chatId, new ArrayList<>()); // передали числа команде и очищаем мапу для пользователя
                        if (chooseShopsCommand.execute2(this, update.getMessage().getFrom(), update.getMessage().getChat(), null)) {
                            userCommand.put(chatId, null);
                        } else {
                            return false;
                        }
                    } else if (msg.getText().equals("Назад")) {
                        userNumbers.put(chatId, new ArrayList<>()); // передали числа команде и очищаем мапу для пользователя
                        userCommand.put(chatId, null);
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(String.valueOf(chatId));
                        sendMessage.setText("Выберите интересующие магазины");
                        Keyboards keyboards = new Keyboards();
                        List<String> commands = new ArrayList<>();
                        commands.add("Выбрать магазины");
                        keyboards.setButtonToCallCommand(sendMessage, commands);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (userCommand.get(chatId).equals(Command.SHOW_ITEMS)) { // получили список номеров категорий (он запрашивается в команде showItems)
                    if (TelegramBot.isNumeric(msg.getText())) {
                        userNumbers.get(chatId).add(Integer.valueOf(msg.getText()));
                    } else if (msg.getText().equals(",")) {
                        break;
                    } else if (msg.getText().contains(",")) {
                        NonCommand nonComand = new NonCommand();
                        if (nonComand.checkValid(msg.getText())) {
                            int[] array = nonComand.getNumbers(msg.getText());
                            for (int elem : array) {
                                userNumbers.get(chatId).add(elem);
                            }
                        } else {
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(String.valueOf(chatId));
                            sendMessage.setText("Пожалуйста, введите номера категорий товаров через запятую " +
                                    "или воспользуйтесь клавиатурой для ввода чисел");
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    } else if (msg.getText().equals("Стереть") && userNumbers.get(chatId).size() > 0) {
                        userNumbers.get(chatId).remove(userNumbers.get(chatId).size() - 1);
                    } else if (msg.getText().equals("Далее")) {
                        showItemsCommand.setNumbers(userNumbers.get(chatId));
                        userNumbers.put(chatId, new ArrayList<>()); // передали числа команде и очищаем мапу для пользователя
                        if (showItemsCommand.execute2(this, update.getMessage().getFrom(), update.getMessage().getChat(), null)) {
                            userCommand.put(chatId, null);
                        } else {
                            return false;
                        }
                    } else if (msg.getText().equals("Назад")) {
                        userNumbers.put(chatId, new ArrayList<>()); // передали числа команде и очищаем мапу для пользователя
                        userCommand.put(chatId, null);
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(String.valueOf(chatId));
                        sendMessage.setText("Хотите выполнить повторный поиск?\n" +
                                "Найти товар - поиск акционных товаров по названию" +
                                "Отобразить товары - поиск акционных товаров по категориям\n" +
                                "/shops - вернуться к выбору магазинов");
                        Keyboards keyboards = new Keyboards();
                        List<String> commands = new ArrayList<>();
                        commands.add("Найти товар");
                        commands.add("Отобразить товары");
                        keyboards.setButtonToCallCommand(sendMessage, commands);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
        return true;
    }

    @Override
    public String getBotToken() {
        return null;
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        return null;
    }
}
