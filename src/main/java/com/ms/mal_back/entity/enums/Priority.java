package com.ms.mal_back.entity.enums;

import lombok.Getter;

@Getter

public enum Priority {
    STANDARD("Стандартное"),
    FEATURED("Выделенное"),
    PREMIUM("Премиум");

    private final String label;

    Priority(String label) {
        this.label = label;
    }

}
