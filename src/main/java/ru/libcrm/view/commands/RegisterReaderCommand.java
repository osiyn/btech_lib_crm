package ru.libcrm.view.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.libcrm.model.Reader;
import ru.libcrm.service.ReaderService;
import ru.libcrm.util.InputReader;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RegisterReaderCommand implements Command {

    private final ReaderService readerService;
    private final InputReader input;

    @Override
    public void execute() {
        System.out.println("\n-------------------------------------");
        System.out.println("           Регистрация читателя        ");
        System.out.println("---------------------------------------");

        try {
            String firstName = input.readString("Имя: ");
            String lastName = input.readString("Фамилия: ");
            String email = input.readString("Email: ");
            String phone = input.readString("Телефон (опционально): ");

            Reader reader = Reader.builder()
                    .firstName(firstName.trim())
                    .lastName(lastName.trim())
                    .email(email.trim())
                    .phone(phone.isBlank() ? null : phone.trim())
                    .registrationDate(LocalDateTime.now())
                    .isActive(true)
                    .build();

            Reader saved = readerService.registerReader(reader);

            System.out.println("\n Читатель успешно зарегистрирован!");
            System.out.println("   ID: " + saved.getId());
            System.out.println("   ФИО: " + saved.getFullName());
            System.out.println("   Email: " + saved.getEmail());
            System.out.println("   Дата регистрации: " + saved.getRegistrationDate().toLocalDate());

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Зарегистрировать читателя";
    }

    @Override
    public String getDescription() {
        return "Добавить нового читателя в систему";
    }

    @Override
    public String getCategory() {
        return "Читатели";
    }
}
