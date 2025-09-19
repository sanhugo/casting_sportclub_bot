package ru.go.casting_sportclub_bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.go.casting_sportclub_bot.bot.CastingBot;
import ru.go.casting_sportclub_bot.documents.FormDoc;
import ru.go.casting_sportclub_bot.keyboard.KeyboardFacade;
import ru.go.casting_sportclub_bot.model.BotStatus;
import ru.go.casting_sportclub_bot.model.Choice;
import ru.go.casting_sportclub_bot.regex.RegexChecker;
import ru.go.casting_sportclub_bot.service.UserCardService;
import ru.go.casting_sportclub_bot.service.UserFacade;
import ru.go.casting_sportclub_bot.service.UserStateService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.FormatFlagsConversionMismatchException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageHandler implements Handler{
    UserFacade uf;
    KeyboardFacade kf;
    RegexChecker rc;
    FormDoc fd;

    public MessageHandler(KeyboardFacade kf, UserFacade uf, RegexChecker rc, FormDoc fd) {
        this.uf = uf;
        this.kf = kf;
        this.rc = rc;
        this.fd = fd;
    }

    @Override
    public BotApiMethod<?> handle(Update update, CastingBot bot) throws TelegramApiException, IOException {
        if (update.hasMessage()){
        SendMessage s = new SendMessage();
        long userID=update.getMessage().getChatId();
        s.setChatId(userID);
        if (uf.isAdmin(userID)){
            String com = update.getMessage().getText().trim();
            switch (com)
            {
                case "Посмотреть регистрации":
                    StringBuffer t = new StringBuffer("Всего регистраций: ");
                    t.append(uf.checkNotes()).append("\n").append("Из них сегодня: ").append(uf.checkTodayNotes());
                    s.setText(t.toString());
                    s.setReplyMarkup(kf.adminKeyboard());
                    break;
                case "Регистрации по отделам":
                    Choice[] c = Choice.values();
                    StringBuffer e = new StringBuffer("Распределение регистраций по направлениям:\n");
                    for (Choice d:c)
                    {
                        e.append(d.getValue()).append(": ").append(uf.checkRegs(d));
                    }
                    s.setText(e.toString());
                    s.setReplyMarkup(kf.adminKeyboard());
                    break;
                case "Выгрузить файл":
                    SendDocument doc = new SendDocument();
                    doc.setChatId(userID);
                    doc.setDocument(new InputFile(fd.exportAllToExcel(),"Регистрации "+ LocalDate.now()));
                    try {
                        bot.execute(doc);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                    s.setText("Актуальная информация по регистрациям");
                    s.setReplyMarkup(kf.adminKeyboard());
                    break;
            }
        }
        else switch (uf.checkStatus(userID))
        {
            case CONTACT:
                Contact contact = update.getMessage().getContact();
                if (contact!=null){
                String phone = contact.getPhoneNumber();
                uf.addProperty(userID,"phone",phone);
                uf.newState(userID, BotStatus.NAME);
                s.setText("Введите имя");}
                else if (update.getMessage().hasText())
                {
                    String phone = update.getMessage().getText();
                    if (rc.isPhone(phone))
                    {
                        uf.addProperty(userID,"phone",phone);
                        uf.newState(userID, BotStatus.NAME);
                        s.setText("Введите имя");
                    }
                    else
                    {
                        s.setText("Номер не прочитался. Пожалуйста, введите вручную номер в формате 7XXXXXXXXXX.");
                    }
                }
                else
                {
                    s.setText("Номер не прочитался. Пожалуйста, введите вручную номер в формате 7XXXXXXXXXX.");
                }
                break;
            case NAME:
                String name = update.getMessage().getText().trim();
                uf.addProperty(userID,"name",name);
                uf.newState(userID,BotStatus.SURNAME);
                s.setText("Введите фамилию");
                break;
            case SURNAME:
                String surname = update.getMessage().getText().trim();
                uf.addProperty(userID,"surname",surname);
                uf.newState(userID,BotStatus.COURSE);
                s.setText("На каком курсе Вы учитесь?");
                break;
            case COURSE:
                String course = update.getMessage().getText().trim();
                uf.addProperty(userID,"course",course);
                uf.newState(userID,BotStatus.AGE);
                s.setText("Сколько Вам лет?");
                break;
            case AGE:
                String age = update.getMessage().getText().trim();
                if (rc.isAge(age))
                {
                    uf.addProperty(userID,"age",age);
                    uf.newState(userID,BotStatus.FACULTY);
                    s.setText("Выберите факультет");
                    s.setReplyMarkup(kf.facultiesKeyboard());
                }
                else{
                    s.setText("Неверно введен возраст. Попробуйте еще раз");
                }
                break;
            case FACULTY:
                s.setText("Выберите свое подразделение.");
            case CHOICES:
                s.setText("Выберите, пожалуйста, все опции и нажмите Готово.");
                break;
            case EVENTMAKING:
                String skills_event = update.getMessage().getText().trim();
                if (skills_event.isBlank()) {
                    s.setText("Вспомните любой опыт, связанный с организацией мероприятий (не только спортивных). Напишите об этом");
                }
                else
                {
                    uf.addProperty(userID, "eventmaking", skills_event);
                    uf.newState(userID,BotStatus.EVENTPART);
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
                    uf.addProperty(userID,"eventpart",participation);
                    uf.newState(userID,BotStatus.READY);
                    uf.saveUserCard(userID);
                    s.setText("Вы зарегистрированы на кастинг! В подарок - стикеры ССК!\nГлавное меню");
                    s.setReplyMarkup(kf.menuKeyboard());
                    SendSticker sticker = new SendSticker();
                    sticker.setChatId(userID);
                    sticker.setSticker(new InputFile("CAACAgIAAxkBAAECeVdozEzw8VM5EvGhdBM0adQ6LbK8TwAC7oYAAuQ1WUpEcJncEHBAdzYE"));
                    bot.execute(sticker);
                }
                break;
            case READY:
                String com = update.getMessage().getText().trim();
                switch (com)
                {
                    case "Главное меню":
                        s.setReplyMarkup(kf.menuKeyboard());
                        s.setText("Вы находитесь на главном меню.");
                        break;
                    case "FAQ":
                        s.setReplyMarkup(kf.menuButton());
                        s.setText("В данном разделе приведены часто задаваемые вопросы. ОБЯЗАТЕЛЬНО прочтите, прежде чем задать свой вопрос.");
                        break;
                    case "Отделы":
                        s.setText("Данный раздел находится на доработке");
                        s.setReplyMarkup(kf.menuButton());
                        break;
                    case "О кастинге":
                        s.setText("Данный раздел находится на доработке. Ожидайте информацию");
                        s.setReplyMarkup(kf.menuButton());
                        break;
                    case "Задать свой вопрос":
                        s.setText("Подписался на спортклуб?");
                        s.setReplyMarkup(kf.menuButton());
                        break;
                }
        }
        return s;
        }
        return null;
    }
}
