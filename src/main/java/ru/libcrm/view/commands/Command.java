package ru.libcrm.view.commands;

import org.springframework.stereotype.Component;

@Component
public interface Command {
    /**
     * Выполнение команды
     * @throws Exception если команда не может быть выполнена
     */
    void execute() throws Exception;

    /**
     * Название команды для отображения в меню
     */
    String getName();

    /**
     * Описание команды
     */
    default String getDescription() {
        return "";
    }

    /**
     * Ключ команды для поиска
     */
    default String getKey() {
        return getClass().getSimpleName().replace("Command", "").toLowerCase();
    }

    /**
     * Категория команды
     */
    default String getCategory() {
        return "Общее";
    }

    /**
     * Требует ли команда подтверждения
     */
    default boolean requiresConfirmation() {
        return false;
    }
}
