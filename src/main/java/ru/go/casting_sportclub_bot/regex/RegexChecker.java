package ru.go.casting_sportclub_bot.regex;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RegexChecker {
    private static final Pattern RU_PHONE = Pattern.compile("^7\\d{10}$");
    public boolean isAge(String age){
        try{
        int a = Integer.parseInt(age);
        return a>12 && a<60;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public boolean isPhone(String phone)
    {
        if (phone.isBlank())
        {
            return false;
        }
        String trimmed = phone.trim();
        return RU_PHONE.matcher(trimmed).matches();
    }
}
