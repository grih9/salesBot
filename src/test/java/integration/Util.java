package integration;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class Util {
    public static Update prepareUpdate(String text, Chat chat, User user) {
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        message.setText(text);
        message.setFrom(user);
        update.setMessage(message);
        return update;
    }
}
