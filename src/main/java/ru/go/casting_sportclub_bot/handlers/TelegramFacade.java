package ru.go.casting_sportclub_bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.go.casting_sportclub_bot.bot.CastingBot;
import ru.go.casting_sportclub_bot.service.UserFacade;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramFacade {
    CallbackHandler callbackHandler;
    MessageHandler messageHandler;
    CommandHandler commandHandler;

    public TelegramFacade(CallbackHandler callbackHandler, MessageHandler messageHandler, CommandHandler commandHandler, UserFacade userFacade) {
        this.callbackHandler = callbackHandler;
        this.messageHandler = messageHandler;
        this.commandHandler = commandHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update, CastingBot bot) {
        if (update.hasCallbackQuery())
        {
            return callbackHandler.handle(update,bot);
        }
        else
        {
            String str = update.getMessage().getText().trim();
            if (str.startsWith("/"))
            {
                return commandHandler.handle(update);
            }
            else return messageHandler.handle(update);
        }
    }
}
