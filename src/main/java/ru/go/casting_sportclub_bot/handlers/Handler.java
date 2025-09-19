package ru.go.casting_sportclub_bot.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.go.casting_sportclub_bot.bot.CastingBot;

import java.io.IOException;

public interface Handler {
    public BotApiMethod<?> handle(Update update, CastingBot bot) throws TelegramApiException, IOException;
}
