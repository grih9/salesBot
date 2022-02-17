package bot.commands;

import java.io.Serializable;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AbsSenderMock extends TelegramLongPollingCommandBot {
    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
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
