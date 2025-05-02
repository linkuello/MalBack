package com.ms.mal_back.entity.enums;

public enum CertificationType {
    VETERINARY_LICENSE("Ветеринарная лицензия"),
    SELLER_LICENSE("Лицензия продавца"),
    BREED_REGISTRATION("Регистрация породы"),
    CUSTOM("Произвольный документ");

    private final String displayName;

    CertificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
