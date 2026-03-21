package ru.libcrm.view.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.libcrm.model.Book;
import ru.libcrm.service.BookService;
import ru.libcrm.util.InputReader;
import ru.libcrm.util.UuidHelper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchBookCommand implements Command {

    private final BookService bookService;
    private final InputReader input;

    @Override
    public void execute() {
        System.out.println("\n------------------------------------");
        System.out.println("           Поиск книги                ");
        System.out.println("--------------------------------------");

        try {
            String query = input.readString("Введите название для поиска: ");

            if (query.isBlank()) {
                System.out.println("❌ Поисковый запрос не может быть пустым");
                return;
            }

            List<Book> results = bookService.searchBooksByTitle(query);

            if (results.isEmpty()) {
                System.out.println("Книги по запросу \"" + query + "\" не найдены");
                return;
            }

            System.out.println("\nНайдено книг: " + results.size());
            System.out.printf("%-4s %-35s %-25s %-8s%n",
                    "ID", "Название", "Автор", "Доступно");
            System.out.println("─".repeat(80));

            for (Book book : results) {
                System.out.printf("%-4s %-35s %-25s %-8s%n",
                        UuidHelper.formatShort(book.getId()),
                        truncate(book.getTitle(), 33),
                        truncate(book.getAuthor(), 23),
                        book.getItemsAvailable());
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Найти книгу";
    }

    @Override
    public String getDescription() {
        return "Поиск книги по названию";
    }

    @Override
    public String getCategory() {
        return "Книги";
    }

    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() > max ? text.substring(0, max - 3) + "..." : text;
    }
}
