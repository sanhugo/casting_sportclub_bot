package ru.go.casting_sportclub_bot.keyboard;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.go.casting_sportclub_bot.model.Choice;
import ru.go.casting_sportclub_bot.model.Faculties;

import java.util.*;

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

    public InlineKeyboardMarkup directions(Set<Choice> selected) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        Choice[] c = Choice.values();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Choice choice:c)
        {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton ikb = new InlineKeyboardButton();
            String text = selected.contains(choice) ? "✅ " + choice.getValue() : choice.getValue();
            ikb.setText(text);
            ikb.setCallbackData(choice.name());

            row.add(ikb);
            keyboard.add(row);
        }
        InlineKeyboardButton done = new InlineKeyboardButton("Готово!");
        done.setCallbackData("Done");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(done);
        keyboard.add(row);
        markup.setKeyboard(keyboard);
        return markup;
    }
}
