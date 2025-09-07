package ru.go.casting_sportclub_bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.go.casting_sportclub_bot.bot.CastingBot;
import ru.go.casting_sportclub_bot.handlers.TelegramFacade;

@Configuration
public class BotConfig {
    private final TelegramBotConfig botConfig;

    public BotConfig(TelegramBotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getWebHookPath()).build();
    }

    @Bean
    public CastingBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        CastingBot bot = new CastingBot(telegramFacade, setWebhook,botConfig.getBotToken());
        bot.setBotToken(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getUserName());
        bot.setBotPath(botConfig.getWebHookPath());
        return bot;
    }
}
