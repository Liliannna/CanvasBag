package com.project.canvasBag.erroritem.code;

import lombok.Getter;

@Getter
public enum ErrorField {
    INVALID_NAME("от 2 до 20 символов, только буквы русского алфавита", "Название"),
    INVALID_COLOR("от 2 до 20 символов, только буквы русского алфавита", "Цвет"),
    INVALID_COMMENT("до 200 символов", "Комментарий"),
    INVALID_WIDTH("от 2 до 6 знаков", "Ширина"),
    INVALID_LENGTH("от 2 до 6 знаков", "Длинна"),
    INVALID_THICKNESS("до 5 знаков", "Плотность"),
    INVALID_PRICE("от 2 до 6 знаков", "Цена"),
    INVALID_SIZE("до 5 знаков", "Размер"),
    INVALID_FILE("Файл не найден или поврежден", "");

    private final String message;
    private final String field;

    ErrorField(String message, String field) {
        this.message = message;
        this.field = field;
    }

}
