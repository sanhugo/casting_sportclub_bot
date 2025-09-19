package ru.go.casting_sportclub_bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.go.casting_sportclub_bot.bot.CastingBot;

import java.io.IOException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramFacade {
    CallbackHandler callbackHandler;
    MessageHandler messageHandler;
    CommandHandler commandHandler;

    public TelegramFacade(CallbackHandler callbackHandler, MessageHandler messageHandler, CommandHandler commandHandler) {
        this.callbackHandler = callbackHandler;
        this.messageHandler = messageHandler;
        this.commandHandler = commandHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update, CastingBot bot) throws TelegramApiException, IOException {
        if (update.hasCallbackQuery())
        {
            return callbackHandler.handle(update,bot);
        }
        else
        {
            if (update.hasMessage()&&update.getMessage().hasText()&&update.getMessage().getText().startsWith("/"))
            {
                return commandHandler.handle(update);
            }
            return messageHandler.handle(update,bot);
        }
    }
}