package ru.go.casting_sportclub_bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.go.casting_sportclub_bot.keyboard.KeyboardFacade;
import ru.go.casting_sportclub_bot.model.BotStatus;
import ru.go.casting_sportclub_bot.service.UserStateService;

@Component

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandHandler implements Handler{
    UserStateService uss;
    KeyboardFacade kf;

    public CommandHandler(UserStateService uss, KeyboardFacade kf) {
        this.uss = uss;
        this.kf = kf;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        String command = update.getMessage().getText().trim();
        SendMessage s = new SendMessage();
        long userID = update.getMessage().getChatId();
        if (command.equals("/start"))
        {
            if (!uss.hasUser(userID))
            {
                s.setText("Добро пожаловать в бот регистрации на кастинг в ССК «Сибры». Готовы познакомиться?\nДля начала разрешите передать свой контакт для связи руководителей с Вами. Без контактной информации мы не сможем связаться с Вами.");
                uss.newState(userID, BotStatus.CONTACT);
            }
            else
            {
                s.setText("Главное меню");
                s.setReplyMarkup(kf.menuKeyboard());
            }
        }
        return s;
    }
}
