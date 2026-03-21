package ru.libcrm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.libcrm.model.Book;
import ru.libcrm.model.Reader;
import ru.libcrm.model.Transaction;
import ru.libcrm.model.exception.*;
import ru.libcrm.repository.BookRepository;
import ru.libcrm.repository.ReaderRepository;
import ru.libcrm.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final TransactionRepository transactionRepository;

    private static final int DEFAULT_LOAN_DAYS = 14;

    public Transaction issueBook(UUID bookId, UUID readerId, Integer loanDays) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new ReaderNotFoundException(readerId));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException(bookId);
        }
        if (Boolean.FALSE.equals(reader.getIsActive())) {
            throw new ReaderNotActiveException(readerId);
        }

        Transaction transaction = Transaction.builder()
                .bookId(bookId)
                .readerId(readerId)
                .transactionDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(loanDays != null ? loanDays : DEFAULT_LOAN_DAYS))
                .status(Transaction.TransactionStatus.ACTIVE)
                .build();

        transactionRepository.save(transaction);
        book.decrease();
        bookRepository.updateItems(bookId, book.getItemsAvailable());

        return transaction;
    }

    public Transaction returnBook(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        if(transaction.getStatus() != Transaction.TransactionStatus.ACTIVE) {
            throw new IllegalStateException("Книга уже возвращена");
        }

        transaction.returnBook();
        transactionRepository.update(transaction);

        Book book = bookRepository.findById(transaction.getBookId())
                .orElseThrow(() -> new BookNotFoundException(transaction.getBookId()));

        book.increase();
        bookRepository.updateItems(book.getId(), book.getItemsAvailable());

        return transaction;
    }

    public List<Transaction> getActiveTransactionsByReader(UUID readerId) {
        return transactionRepository.findByReaderAndStatus(readerId, Transaction.TransactionStatus.ACTIVE);
    }

    public List<Transaction> getAllActiveTransactions() {
        return transactionRepository.findByStatus(Transaction.TransactionStatus.ACTIVE);
    }

}
