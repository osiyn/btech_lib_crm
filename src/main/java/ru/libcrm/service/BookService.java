package ru.libcrm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.libcrm.model.Book;
import ru.libcrm.model.exception.BookNotFoundException;
import ru.libcrm.repository.BookRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Book addBook(Book book) {
        validateBook(book);
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> searchBooksByTitle(String title) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }
        return bookRepository.findByTitle(title);
    }

    public List<Book> getPopularBooks(int limit) {
        return bookRepository.findPopular(limit);
    }

    private void validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Требуется название книги");
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Требуетс автор книги");
        }
        if (book.getItemsTotal() == null || book.getItemsTotal() <= 0) {
            throw new IllegalArgumentException("Кол-во должно быть > 0");
        }
    }
}
