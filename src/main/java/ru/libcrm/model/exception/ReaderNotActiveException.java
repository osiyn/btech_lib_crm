package ru.libcrm.model.exception;

import java.util.UUID;

public class ReaderNotActiveException extends RuntimeException {
    public ReaderNotActiveException(UUID readerId) {
        super("Читатель с id " + readerId + " не активен");
    }
}
