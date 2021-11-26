package bot;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

import bot.commands.*;

public final class TelegramBot extends TelegramLongPollingCommandBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    //Класс для обработки сообщений, не являющихся командой
    private final NonCommand nonCommand;

    /**
     * Настройки для разных пользователей. Ключ - уникальный id чата, значение - id города пользователя
     */
    private static Map<Long, Long> userSettings;

    public TelegramBot(String botName, String botToken) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        //создаём вспомогательный класс для работы с сообщениями, не являющимися командами
        this.nonCommand = new NonCommand();
        //регистрируем команды
        register(new StartCommand("start", "Старт"));
        register(new ChooseCityCommand("city", "Город"));
        userSettings = new HashMap<>();
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    /**
     * Ответ на запрос, не являющийся командой
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = getUserName(msg);

//        String answer = nonCommand.nonCommandExecute(chatId, userName, msg.getText());
//        setAnswer(chatId, userName, answer);
    }

    /**
     * Получение города по id чата. Если ранее для этого чата в ходе сеанса работы бота настройки не были установлены,
     * вызывается команда выбора города
     */
    public Long getUserCity(Long chatId) {
        Map<Long, Long> userSettings = getUserSettings();
        Long city = userSettings.get(chatId);
        if (city == null) {
            setAnswer(chatId, "Пожалуйста, выполните команду /city");
        }
        return city;
    }

    /**
     * Формирование имени пользователя
     * @param msg сообщение
     */
    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    /**
     * Отправка ответа
     * @param chatId id чата
     * @param text текст ответа
     */
    private void setAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static Map<Long, Long> getUserSettings() {
        return userSettings;
    }
}
