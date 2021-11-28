package bot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import utils.Utils;

/**
 * Команда "Город"
 */
public class ChooseCityCommand extends ServiceCommand {
    public ChooseCityCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        super.sendAnswer(absSender, chat.getId(), super.getCommandIdentifier(), userName,
                "Пожалуйста, введите город");
        //обработка введенного значения
        //ищем по имени в бд, если есть, обновляем id города пользователя, если нет, просим заново ввести город
    }
}
