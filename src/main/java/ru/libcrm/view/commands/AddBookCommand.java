package ru.libcrm.view.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.libcrm.model.Book;
import ru.libcrm.service.BookService;
import ru.libcrm.util.InputReader;

@Component
@RequiredArgsConstructor
public class AddBookCommand implements Command {
    private final BookService bookService;
    private final InputReader input;

    @Override
    public void execute() {
        System.out.println("\n------------------------------------");
        System.out.println("           Добавление книги           ");
        System.out.println("--------------------------------------");

        try {
            String title = input.readString("Название книги: ");
            if (title.isBlank()) {
                System.out.println("Название не может быть пустым!");
                return;
            }

            String author = input.readString("Автор: ");
            if (author.isBlank()) {
                System.out.println("Автор не может быть пустым!");
                return;
            }

            Integer copies = input.readInteger("Количество экземпляров: ", 1);

            Book book = Book.builder()
                    .title(title.trim())
                    .author(author.trim())
                    .itemsTotal(copies)
                    .itemsAvailable(copies)
                    .build();

            Book saved = bookService.addBook(book);

            System.out.println("\n Книга успешно добавлена!");
            System.out.println("   ID: " + saved.getId());
            System.out.println("   Название: " + saved.getTitle());
            System.out.println("   Автор: " + saved.getAuthor());
            System.out.println("   Доступно: " + saved.getItemsAvailable() + "/" + saved.getItemsTotal());

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Добавить книгу";
    }

    @Override
    public String getDescription() {
        return "Добавить новую книгу в каталог";
    }

    @Override
    public String getCategory() {
        return "Книги";
    }
}
