package ru.go.casting_sportclub_bot.keyboard;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.go.casting_sportclub_bot.model.Faculties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InlineKeyboardMaker {
    public InlineKeyboardMaker() {
    }

    public InlineKeyboardMarkup faculties()
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        Faculties[] faculties = Arrays.stream(Faculties.values())
                .filter(c -> c.name().length()<8)
                .sorted(Comparator.comparingInt(c -> c.name().length()))
                .toArray(Faculties[]::new);
        Faculties[] faculties2 = Arrays.stream(Faculties.values())
                .filter(c -> c.name().length()>7)
                .sorted(Comparator.comparingInt(c -> c.name().length()))
                .toArray(Faculties[]::new);
        List<List<InlineKeyboardButton>> buttons = getLists(faculties, faculties2);
        markup.setKeyboard(buttons);
        return markup;
    }

    private static List<List<InlineKeyboardButton>> getLists(Faculties[] faculties, Faculties[] faculties2) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> rows = new ArrayList<>();
        for  (Faculties faculty : faculties)
        {
            InlineKeyboardButton button = new InlineKeyboardButton(faculty.getValue());
            button.setCallbackData(faculty.name());
            rows.add(button);
            if (rows.size()==2)
            {
                buttons.add(rows);
                rows = new ArrayList<>();
            }
        }
        for (Faculties faculty : faculties2) {
            InlineKeyboardButton button = new InlineKeyboardButton(faculty.getValue());
            button.setCallbackData(faculty.name());
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            buttons.add(row);
        }
        return buttons;
    }
}
