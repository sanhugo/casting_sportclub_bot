package ru.go.casting_sportclub_bot.keyboard;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.go.casting_sportclub_bot.model.Choice;

import java.util.Set;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyboardFacade {
    ReplyKeyboardMaker keyboardMaker;
    InlineKeyboardMaker inlineKeyboardMaker;
    public KeyboardFacade(ReplyKeyboardMaker keyboardMarkup, InlineKeyboardMaker inlineKeyboardMaker) {
        this.keyboardMaker = keyboardMarkup;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
    }

    public ReplyKeyboardMarkup menuKeyboard() {
        return keyboardMaker.menuKeyboard();
    }
    public InlineKeyboardMarkup facultiesKeyboard() {
        return inlineKeyboardMaker.faculties();
    }
    public ReplyKeyboardMarkup askContact(){
        return keyboardMaker.askContact();
    }

    public InlineKeyboardMarkup directions(Set<Choice> set) {
        return inlineKeyboardMaker.directions(set);
    }
    public ReplyKeyboardMarkup menuButton()
    {
        return keyboardMaker.menuButton();
    }
    public ReplyKeyboardMarkup adminKeyboard() {
        return keyboardMaker.adminKeyboard();
    }
}
