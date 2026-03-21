package ru.libcrm.view.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.libcrm.model.Book;
import ru.libcrm.service.BookService;
import ru.libcrm.util.UuidHelper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListBooksCommand implements Command {

    private final BookService bookService;

    @Override
    public void execute() {
        System.out.println("\n------------------------------------");
        System.out.println("           Список всех книг           ");
        System.out.println("--------------------------------------");

        try {
            List<Book> books = bookService.getAllBooks();

            if (books.isEmpty()) {
                System.out.println("В библиотеке пока нет книг");
                return;
            }

            System.out.printf("%-4s %-30s %-25s %-8s %-10s%n",
                    "ID", "Название", "Автор", "Доступно", "Всего");
            System.out.println("─".repeat(85));

            for (Book book : books) {
                String status = book.isAvailable() ? "ok" : "none";
                System.out.printf("%-4s %-30s %-25s %-8s %-10s%n",
                        UuidHelper.formatShort(book.getId()),
                        truncate(book.getTitle(), 28),
                        truncate(book.getAuthor(), 23),
                        status + " " + book.getItemsAvailable(),
                        book.getItemsTotal());
            }

            System.out.println("─".repeat(85));
            System.out.println("Всего книг: " + books.size());

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Показать все книги";
    }

    @Override
    public String getDescription() {
        return "Отобразить каталог всех книг";
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
