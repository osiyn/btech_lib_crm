package ru.libcrm.model.exception;

import java.util.UUID;

public class ReaderNotFoundException extends RuntimeException {
    public ReaderNotFoundException(UUID readerId) {
        super("Читатель с id " + readerId + " не найден");
    }
}
