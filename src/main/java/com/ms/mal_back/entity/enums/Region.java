package com.ms.mal_back.entity.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Region {
    БАТКЕНСКАЯ_ОБЛАСТЬ("Баткенская область"),
    ОШСКАЯ_ОБЛАСТЬ("Ошская область"),
    ЖАЛАЛ_АБАДСКАЯ_ОБЛАСТЬ("Жалал-Абадская область"),
    НАРЫНСКАЯ_ОБЛАСТЬ("Нарынская область"),
    ТАЛАССКАЯ_ОБЛАСТЬ("Таласская область"),
    ЧУЙСКАЯ_ОБЛАСТЬ("Чуйская область"),
    ИССЫК_КУЛЬСКАЯ_ОБЛАСТЬ("Иссык-Кульская область"),
    ГОРОД_ОШ("Город Ош"),
    ГОРОД_БИШКЕК("Город Бишкек");

    private final String displayName;

    Region(String displayName) {
        this.displayName = displayName;
    }

    public static Region fromDisplayName(String name) {
        return Arrays.stream(Region.values())
                .filter(r -> r.getDisplayName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown region: " + name));
    }
}
