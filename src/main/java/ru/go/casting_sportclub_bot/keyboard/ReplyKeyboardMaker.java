package ru.go.casting_sportclub_bot.keyboard;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplyKeyboardMaker {
    public ReplyKeyboardMarkup menuKeyboard()
    {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton kb = new KeyboardButton("FAQ");
        KeyboardButton kb1 = new KeyboardButton("Задать свой вопрос");
        keyboardRow1.add(kb);
        keyboardRow1.add(kb1);
        KeyboardRow kr1 = new KeyboardRow();
        KeyboardButton directions = new KeyboardButton("Отделы");
        KeyboardButton castingDate = new KeyboardButton("О кастинге");
        kr1.add(directions);
        kr1.add(castingDate);
        rows.add(keyboardRow1);
        rows.add(kr1);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup askContact() {
        // Создаём кнопку
        KeyboardButton contactButton = new KeyboardButton("Отправить контакт");
        contactButton.setRequestContact(true); // ✅ запросит контакт
        // Размещаем кнопку в клавиатуре
        return creakeKeyboard(contactButton);
    }

    public ReplyKeyboardMarkup menuButton() {
        KeyboardButton menu = new KeyboardButton("Главное меню");
        return creakeKeyboard(menu);
    }

    private ReplyKeyboardMarkup creakeKeyboard(KeyboardButton menu) {
        KeyboardRow row = new KeyboardRow();
        row.add(menu);
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(keyboard);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        return markup;
    }
}
