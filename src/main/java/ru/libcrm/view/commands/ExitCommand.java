package ru.libcrm.view.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("\n-------------------------------------");
        System.out.println("           До свидания!               ");
        System.out.println("        Спасибо за использование!     ");
        System.out.println("---------------------------------------");
    }

    @Override
    public String getName() {
        return "Выход";
    }

    @Override
    public String getDescription() {
        return "Завершить работу приложения";
    }

    @Override
    public String getCategory() {
        return "Системные";
    }

    @Override
    public boolean requiresConfirmation() {
        return true;
    }
}
