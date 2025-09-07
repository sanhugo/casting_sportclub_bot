package ru.go.casting_sportclub_bot.model;

import lombok.Getter;

@Getter
public enum Faculties {
    FIA("ФИЯ"),
    FIJ("ФИЖ"),
    IMIT("ИМИТ"),
    FMENITO("ФМЕНиТО"),
    OPSISPO("ОПСиСПО"),
    OGEO("ОГЭО"),
    MIEL("МИЭЛ"),
    SAF("САФ"),
    UI("ЮИ"),
    ISN("ИСН"),
    FBKI("ФБКИ"),
    BSF("БПФ"),
    COLLEGE("Колледж"),
    GEOGRAPHY("Географический"),
    GEOLOGY("Геологический"),
    CHEM("Химический"),
    PHYS("Физфак"),
    HIST("Исторический"),
    PSYCH("Факультет психологии");

    private final String value;

    Faculties(String value) {
        this.value = value;
    }
}
