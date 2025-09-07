package ru.go.casting_sportclub_bot.model;

public enum Choice {
    CYBER("Киберспорт"),
    MEDIA("Медиаменеджмент"),
    TECH("Техническое сопровождение"),
    CHEER("Чирлидинг"),
    EVENT("Организация мероприятий"),
    SPONSOR("Работа со спонсорами"),
    MARKET("Маркетинг"),
    SPORTSMAN("Спортсмен"),
    MANAGER("Менеджер по видам спорта"),
    ACTIVE("Активист"),
    FANCLUB("Клуб болельщиков");

    private final String value;

    Choice(String value) {
        this.value = value;
    }
}
