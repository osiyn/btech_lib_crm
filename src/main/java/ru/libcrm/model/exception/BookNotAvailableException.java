package ru.libcrm.model.exception;

import java.util.UUID;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(UUID bookId) {
        super("Книга с id " + bookId + " не доступна");
    }
}
