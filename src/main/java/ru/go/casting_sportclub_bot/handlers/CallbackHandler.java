package ru.go.casting_sportclub_bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.go.casting_sportclub_bot.bot.CastingBot;
import ru.go.casting_sportclub_bot.keyboard.KeyboardFacade;
import ru.go.casting_sportclub_bot.model.BotStatus;
import ru.go.casting_sportclub_bot.model.Choice;
import ru.go.casting_sportclub_bot.service.DirectionService;
import ru.go.casting_sportclub_bot.service.UserFacade;
import ru.go.casting_sportclub_bot.service.UserStateService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CallbackHandler implements Handler{
    UserFacade uf;
    KeyboardFacade kf;
    DirectionService ds;

    public CallbackHandler(UserFacade uf, KeyboardFacade kf, DirectionService ds) {
        this.uf = uf;
        this.kf = kf;
        this.ds = ds;
    }
    @Override
    public BotApiMethod<?> handle(Update update, CastingBot bot) {
        long userID = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        BotStatus bt = uf.checkStatus(userID);

        switch (bt) {
            case FACULTY:
                String f = update.getCallbackQuery().getData();
                DeleteMessage dm = new DeleteMessage();
                dm.setChatId(userID);
                dm.setMessageId(messageId);
                try {
                    bot.execute(dm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                uf.newState(userID, BotStatus.CHOICES);
                uf.addProperty(userID, "faculties", f);

                SendMessage s = new SendMessage();
                s.setChatId(userID);
                s.setText("В каких направлениях деятельности спортклуба хотели бы себя попробовать? " +
                        "Выберите интересующие вас направления и нажмите Готово.");
                s.setReplyMarkup(kf.directions(ds.getSelections(userID)));
                return s;

            case CHOICES:
                String callbackData = update.getCallbackQuery().getData();
                if (callbackData.equals("Done")) {
                    // Получаем выбранные направления из Redis
                    Set<Choice> selections = ds.getSelections(userID);

                    // Редактируем исходное сообщение: текст + убираем клавиатуру
                    EditMessageText edit = new EditMessageText();
                    edit.setChatId(userID);
                    edit.setMessageId(messageId);
                    edit.setText("Опишите свой опыт организации мероприятий.");
                    edit.setReplyMarkup(null);
                    uf.newState(userID,BotStatus.EVENTMAKING);
                    return edit;

                } else {
                    Choice choice = Choice.valueOf(callbackData);

                    // проверяем, есть ли уже этот выбор в Redis
                    Set<Choice> current = ds.getSelections(userID);
                    if (current != null && current.contains(choice)) {
                        ds.removeSelection(userID, choice);
                        current.remove(choice);
                    } else {
                        ds.addSelection(userID, choice);
                        if (current == null) {
                            current = new HashSet<>();
                        }
                        current.add(choice);
                    }

                    // перерисовываем клавиатуру с отмеченными пунктами
                    EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
                    editMarkup.setChatId(userID);
                    editMarkup.setMessageId(messageId);
                    editMarkup.setReplyMarkup(kf.directions(current));

                    return editMarkup;
                }
        }

        return null; // fallback
    }
}
