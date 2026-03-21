package ru.libcrm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.libcrm.view.ConsoleMenu;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class Main implements CommandLineRunner {
    private final ConsoleMenu consoleMenu;
    private final ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            consoleMenu.show();
        } catch (Exception e) {
            System.out.println("Ошибка в работе приложения: " + e.getMessage());
        } finally {
            context.close();
        }
    }
}