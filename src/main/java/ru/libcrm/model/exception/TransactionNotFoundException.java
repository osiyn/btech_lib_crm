package ru.libcrm.model.exception;

import java.util.UUID;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(UUID transactionId) {
        super("Транзакция с id " + transactionId + " не найден");
    }
}
