package ru.libcrm.view.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.libcrm.model.Book;
import ru.libcrm.model.Reader;
import ru.libcrm.model.Transaction;
import ru.libcrm.service.BookService;
import ru.libcrm.service.ReaderService;
import ru.libcrm.service.TransactionService;
import ru.libcrm.util.InputReader;
import ru.libcrm.util.UuidHelper;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IssueBookCommand implements Command {

    private final TransactionService transactionService;
    private final BookService bookService;
    private final ReaderService readerService;
    private final InputReader input;

    @Override
    public void execute() {
        System.out.println("\n-------------------------------------");
        System.out.println("           Выдача книги читателю       ");
        System.out.println("---------------------------------------");

        try {
            // Показать доступные книги
            System.out.println("\nДоступные книги:");
            List<Book> availableBooks = bookService.getAllBooks().stream()
                    .filter(Book::isAvailable)
                    .toList();

            if (availableBooks.isEmpty()) {
                System.out.println("Нет доступных книг для выдачи");
                return;
            }

            availableBooks.forEach(b ->
                    System.out.printf("  [%s] %s - %s (доступно: %d)%n",
                            UuidHelper.formatShort(b.getId()),
                            truncate(b.getTitle(), 25),
                            b.getAuthor(),
                            b.getItemsAvailable())
            );

            UUID bookId = input.readUuid("Введите ID книги: ");

            // Показать активных читателей
            System.out.println("\nАктивные читатели:");
            List<Reader> activeReaders = readerService.getAllReaders().stream()
                    .filter(r -> Boolean.TRUE.equals(r.getIsActive()))
                    .toList();

            if (activeReaders.isEmpty()) {
                System.out.println("Нет активных читателей");
                return;
            }

            activeReaders.forEach(r ->
                    System.out.printf("  [%s] %s %s (%s)%n",
                            UuidHelper.formatShort(r.getId()),
                            r.getLastName(),
                            r.getFirstName(),
                            r.getEmail())
            );

            UUID readerId = input.readUuid("Введите ID читателя: ");
            Integer days = input.readInteger("Срок выдачи в днях (по умолчанию 14): ", 14);

            Transaction transaction = transactionService.issueBook(bookId, readerId, days);

            System.out.println("\n Книга успешно выдана!");
            System.out.println("   Номер выдачи: " + transaction.getId());
            System.out.println("   Книга: " + bookService.getBookById(bookId).getTitle());
            System.out.println("   Читатель: " + readerService.getReaderById(readerId).getFullName());
            System.out.println("   Дата выдачи: " + transaction.getTransactionDate().toLocalDate());
            System.out.println("   Вернуть до: " + transaction.getDueDate().toLocalDate());

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Выдать книгу";
    }

    @Override
    public String getDescription() {
        return "Выдать книгу читателю на время";
    }

    @Override
    public String getCategory() {
        return "Операции";
    }

    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() > max ? text.substring(0, max - 3) + "..." : text;
    }
}
