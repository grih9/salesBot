package bot.keyboards;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class Keyboards {
    public synchronized void setButtonToCallCommand(SendMessage sendMessage, List<String> commands) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        for (String command: commands) {
            keyboardFirstRow.add(new KeyboardButton(command));
        }
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    /**
     *
     * @param sendMessage
     * @param isForShop : false - клавиатура для поиска по категрориям
     *                    true - клавиатура для выбора магазинов
     */
    public synchronized void setButtonToCallNumbers(SendMessage sendMessage, boolean isForShop) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();

        int k = isForShop ? 10 : 16;

        for (int i = 1; i <= k / 2; i++) {
            keyboardFirstRow.add(new KeyboardButton(String.valueOf(i)));
            keyboardSecondRow.add(new KeyboardButton(String.valueOf(k / 2 + i)));
        }

        keyboardThirdRow.add(new KeyboardButton("Назад"));
        keyboardThirdRow.add(new KeyboardButton(","));
        keyboardThirdRow.add(new KeyboardButton("Стереть"));
        keyboardThirdRow.add(new KeyboardButton("Далее"));
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
}
