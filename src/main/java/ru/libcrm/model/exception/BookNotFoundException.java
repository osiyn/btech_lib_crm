package ru.libcrm.model.exception;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(UUID bookId) {
        super("Книга с id " + bookId + " не найдена");
    }
}
