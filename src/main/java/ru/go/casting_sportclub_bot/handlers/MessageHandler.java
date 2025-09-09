package ru.go.casting_sportclub_bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.go.casting_sportclub_bot.keyboard.KeyboardFacade;
import ru.go.casting_sportclub_bot.model.BotStatus;
import ru.go.casting_sportclub_bot.regex.RegexChecker;
import ru.go.casting_sportclub_bot.service.UserCardService;
import ru.go.casting_sportclub_bot.service.UserStateService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageHandler implements Handler{
    UserStateService usr;
    KeyboardFacade kf;
    UserCardService ucs;
    RegexChecker rc;

    public MessageHandler(UserStateService usr, KeyboardFacade kf, UserCardService ucs, RegexChecker rc) {
        this.usr = usr;
        this.kf = kf;
        this.ucs = ucs;
        this.rc = rc;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        SendMessage s = new SendMessage();
        long userID=update.getMessage().getChatId();
        s.setChatId(userID);
        switch (usr.checkStatus(userID))
        {
            case CONTACT:
                Contact contact = update.getMessage().getContact();
                String phone = contact.getPhoneNumber();
                usr.addProperty(userID,"phone",phone);
                usr.newState(userID, BotStatus.NAME);
                s.setText("Введите имя");
                break;
            case NAME:
                String name = update.getMessage().getText().trim();
                usr.addProperty(userID,"name",name);
                usr.newState(userID,BotStatus.SURNAME);
                s.setText("Введите фамилию");
                break;
            case SURNAME:
                String surname = update.getMessage().getText().trim();
                usr.addProperty(userID,"surname",surname);
                usr.newState(userID,BotStatus.AGE);
                s.setText("Сколько Вам лет?");
                break;
            case AGE:
                String age = update.getMessage().getText().trim();
                if (rc.isAge(age))
                {
                    usr.addProperty(userID,"age",age);
                    usr.newState(userID,BotStatus.FACULTY);
                    s.setText("Выберите факультет");
                    s.setReplyMarkup(kf.facultiesKeyboard());
                    break;
                }
                else{
                    s.setText("Неверно введена дата. Попробуйте еще раз");
                }
            case EVENTMAKING:
                String skills_event = update.getMessage().getText().trim();
                if (skills_event.isBlank()) {
                    s.setText("Вспомните любой опыт, связанный с организацией мероприятий (не только спортивных). Напишите об этом");
                }
                else
                {
                    usr.addProperty(userID, "eventmaking", skills_event);
                    usr.newState(userID,BotStatus.EVENTPART);
                    s.setText("Был ли у вас опыт участия в мероприятиях?");
                }
                break;
            case EVENTPART:
                String participation = update.getMessage().getText().trim();
                if (participation.isBlank())
                {
                    s.setText("Вспомните все спортивные мероприятия, в которых Вы участвовали. Даже если участвовали без призового места)");
                }
                else
                {
                    usr.addProperty(userID,"eventpart",participation);
                    usr.newState(userID,BotStatus.READY);
                    ucs.saveUserCard(userID);
                }
                break;
        }
        return s;
    }
}
