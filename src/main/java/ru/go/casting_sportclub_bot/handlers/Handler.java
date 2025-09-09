package ru.go.casting_sportclub_bot.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    public BotApiMethod<?> handle(Update update);
}
