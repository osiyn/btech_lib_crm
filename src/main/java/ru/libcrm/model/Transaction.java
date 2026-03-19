package ru.libcrm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private UUID id;
    private UUID bookId;
    private UUID readerId;
    private LocalDateTime transactionDate;
    private LocalDateTime returnDate;
    private LocalDateTime dueDate;
    private TransactionStatus status;

    public enum TransactionStatus { ACTIVE, RETURNED, OVERDUE }

    public boolean isOverdue() {
        return status == TransactionStatus.ACTIVE &&
                LocalDateTime.now().isAfter(dueDate);
    }

    public void returnBook() {
        this.returnDate = LocalDateTime.now();
        this.status = TransactionStatus.RETURNED;
    }
}
