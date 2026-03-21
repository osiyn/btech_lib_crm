package ru.libcrm.util;

import lombok.extern.slf4j.Slf4j;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class InputReader {

    private final LineReader reader;

    public InputReader() {
        try {
            Terminal terminal = TerminalBuilder.terminal();
            reader = LineReaderBuilder.builder().terminal(terminal).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize console reader", e);
        }
    }

    public String readString(String prompt) {
        return reader.readLine(prompt + " ").trim();
    }

    public Integer readInteger(String prompt, Integer min, Integer max) {
        while (true) {
            try {
                String input = reader.readLine(prompt + " ").trim();
                int value = Integer.parseInt(input);
                if ((min == null || value >= min) && (max == null || value <= max)) {
                    return value;
                }
                System.out.println("Value must be between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }

    public Integer readInteger(String prompt, Integer defaultValue) {
        String input = reader.readLine(prompt + (defaultValue != null ? " [" + defaultValue + "]" : "") + " ").trim();
        if (input.isEmpty() && defaultValue != null) {
            return defaultValue;
        }
        return Integer.parseInt(input);
    }

    /**
     * Чтение UUID с валидацией
     */
    public UUID readUuid(String prompt) {
        while (true) {
            try {
                String input = reader.readLine(prompt + " ").trim();
                return UuidHelper.parseUuid(input, "ID");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Hint: UUID format example: 550e8400-e29b-41d4-a716-446655440000");
            }
        }
    }

    /**
     * Чтение UUID с опцией копирования из вывода
     */
    public UUID readUuidWithHint(String prompt, String hint) {
        if (hint != null && !hint.isEmpty()) {
            System.out.println("Hint: " + hint);
        }
        return readUuid(prompt);
    }
}