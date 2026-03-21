package ru.libcrm.util;

import java.util.UUID;

public class UuidHelper {

    /**
     * Парсит UUID из строки с обработкой ошибок
     */
    public static UUID parseUuid(String input, String fieldName) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        try {
            return UUID.fromString(input.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid " + fieldName + " format. Expected UUID, got: " + input, e);
        }
    }

    /**
     * Форматирует UUID для вывода (короткая версия)
     */
    public static String formatShort(UUID uuid) {
        if (uuid == null) return "N/A";
        String str = uuid.toString();
        return str.length() > 8 ? str.substring(0, 8) + "..." : str;
    }

    /**
     * Генерирует новый UUID v4
     */
    public static UUID generate() {
        return UUID.randomUUID();
    }
}
