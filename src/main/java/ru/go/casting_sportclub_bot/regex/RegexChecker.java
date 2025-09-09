package ru.go.casting_sportclub_bot.regex;

import org.springframework.stereotype.Component;

@Component
public class RegexChecker {
    public boolean isAge(String age){
        int a = Integer.parseInt(age);
        return a>12 && a<60;
    }
}
