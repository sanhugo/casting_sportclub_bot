package ru.go.casting_sportclub_bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.go.casting_sportclub_bot.keyboard.KeyboardFacade;
import ru.go.casting_sportclub_bot.model.BotStatus;
import ru.go.casting_sportclub_bot.service.UserFacade;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandHandler{
    UserFacade uf;
    KeyboardFacade kf;

    public CommandHandler(UserFacade uf, KeyboardFacade kf) {
        this.uf = uf;
        this.kf = kf;
    }

    public BotApiMethod<?> handle(Update update) {
        System.out.println("GOT MESSAGE!");
        String command = update.getMessage().getText().trim();
        SendMessage s = new SendMessage();
        long userID = update.getMessage().getChatId();
        s.setChatId(userID);
        if (command.equals("/start"))
        {
            System.out.println("I GOT THIS!");
            System.out.println(uf.hasUser(userID));
            if (!uf.hasUser(userID))
            {
                if (!uf.isAdmin(userID)) {
                    s.setText("Добро пожаловать в бот регистрации на кастинг в ССК «Сибры». Готовы познакомиться?\nДля начала разрешите передать свой контакт для связи руководителей с Вами. Без контактной информации мы не сможем связаться с Вами.");
                    s.setReplyMarkup(kf.askContact());
                    uf.newState(userID, BotStatus.CONTACT);
                }
                else
                {
                    s.setText("Меню админа");
                    s.setReplyMarkup(kf.adminKeyboard());
                }
            }
            else
            {
                s.setText("Главное меню");
                s.setReplyMarkup(kf.menuKeyboard());
                uf.newState(userID,BotStatus.READY);
            }
        }
        return s;
    }
}
