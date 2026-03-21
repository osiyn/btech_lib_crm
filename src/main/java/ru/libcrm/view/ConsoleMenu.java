package ru.libcrm.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.libcrm.view.commands.Command;
import ru.libcrm.view.commands.CommandInvoker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsoleMenu {
    private final List<Command> commands;
    private final CommandInvoker commandInvoker;

    private boolean running = true;

    public void show() {
        printHeader();

        while (running) {
            printMenu();
            handleInput();
        }
    }

    private void printHeader() {
        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("                                                        ");
        System.out.println("           БИБЛИОТЕЧНАЯ СИСТЕМА УПРАВЛЕНИЯ              ");
        System.out.println("                                                        ");
        System.out.println("               Library CRM v1.0                         ");
        System.out.println("                                                        ");
        System.out.println("--------------------------------------------------------");
        System.out.println();
    }

    private void printMenu() {
        System.out.println("\n------------------------------------------------------");
        System.out.println("                        МЕНЮ                            ");
        System.out.println("---------------------------------------------------------");

        for (int i = 0; i < commands.size(); i++) {
            System.out.printf("  %d. %-45s%n",
                    i + 1, commands.get(i).getName());
        }

        System.out.println("\n------------------------------------------------------");
        System.out.printf("  Команд: %-45s %n", commands.size());
        System.out.println("---------------------------------------------------------");
        System.out.print("\n▶  Выберите команду (1-" + commands.size() + "): ");
    }

    private void handleInput() {
        try {
            String input = System.console() != null ?
                    System.console().readLine() :
                    new java.util.Scanner(System.in).nextLine();

            input = input.trim();

            // Пустой ввод
            if (input.isEmpty()) {
                return;
            }

            // Специальные команды
            if (handleSpecialCommands(input)) {
                return;
            }

            // Выполнение команды по номеру
            if (input.matches("\\d+")) {
                int commandNumber = Integer.parseInt(input);
                if (commandNumber >= 1 && commandNumber <= commands.size()) {
                    commandInvoker.execute(commands.get(commandNumber - 1));
                } else {
                    System.out.println("Команда не найдена. Введите число от 1 до " + commands.size());
                }
                return;
            }

            System.out.println("Неизвестная команда. Введите число или 'help'");

        } catch (Exception e) {
            log.error("Ошибка обработки ввода", e);
            System.out.println("Произошла ошибка при обработке ввода");
        }
    }

    private boolean handleSpecialCommands(String input) {
        switch (input.toLowerCase()) {
            case "help", "h", "?":
                printHelp();
                return true;
            case "clear", "cls":
                clearScreen();
                return true;
            case "exit", "quit", "q":
                commandInvoker.execute(getExitCommand());
                exit();
                return true;
            default:
                return false;
        }
    }

    private Command getExitCommand() {
        return commands.stream()
                .filter(c -> c.getClass().getSimpleName().equals("ExitCommand"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ExitCommand not found"));
    }

    private void printHelp() {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("                           СПРАВКА                       ");
        System.out.println("----------------------------------------------------------");
        System.out.println();
        System.out.println("  Числа (1-" + commands.size() + ") - Выполнить команду по номеру");
        System.out.println("  help, h, ?    - Показать эту справку");
        System.out.println("  clear, cls    - Очистить экран");
        System.out.println("  exit, quit, q - Выход из приложения");
        System.out.println();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printHeader();
    }

    public void stop() {
        running = false;
    }

    public void exit() {
        running = false;
//        appShutdown.run();
    }

    private record IndexedCommand(int number, Command command) {}
}
