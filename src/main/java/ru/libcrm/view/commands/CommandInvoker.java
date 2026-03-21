package ru.libcrm.view.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandInvoker {
    /**
     * Выполнить команду
     */
    public boolean execute(Command command) {
        try {
            log.info("Выполнение команды: {}", command.getName());

            // Подтверждение для критических операций
            if (command.requiresConfirmation()) {
                System.out.print("Вы уверены? (y/n): ");
                String confirm = System.console() != null ?
                        System.console().readLine() : "y";
                if (!confirm.equalsIgnoreCase("y")) {
                    System.out.println("Отменено");
                    return false;
                }
            }

            // Выполнение команды
            long startTime = System.currentTimeMillis();
            command.execute();
            long duration = System.currentTimeMillis() - startTime;

            log.info("Команда '{}' выполнена за {} мс", command.getName(), duration);
            return true;

        } catch (Exception e) {
            log.error("Ошибка выполнения команды: {}", command.getName(), e);
            System.out.println("❌ Произошла ошибка: " + e.getMessage());
            return false;
        }
    }

    /**
     * Выполнить команду по ключу (для специальных команд)
     */
    public boolean execute(String commandKey) {
        // Используется только для специальных команд типа "exit"
        return false;
    }
}
