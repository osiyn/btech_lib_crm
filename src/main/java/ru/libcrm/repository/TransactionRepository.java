package ru.libcrm.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.libcrm.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Transaction> rowMapper = this::mapRow;

    public Optional<Transaction> findById(UUID id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public List<Transaction> findByReaderAndStatus(UUID readerId, Transaction.TransactionStatus status) {
        String sql = "SELECT * FROM transactions WHERE reader_id = ? AND status = ? ORDER BY transaction_date DESC";
        return jdbcTemplate.query(sql, rowMapper, readerId, status.name());
    }

    public List<Transaction> findByStatus(Transaction.TransactionStatus status) {
        String sql = "SELECT * FROM transactions WHERE status = ? ORDER BY transaction_date DESC";
        return jdbcTemplate.query(sql, rowMapper, status.name());
    }

    @Transactional
    public Transaction save(Transaction transaction) {
        UUID id = transaction.getId() != null ? transaction.getId() : UUID.randomUUID();
        String sql = """
                INSERT INTO transactions (id, book_id, reader_id, transaction_date, return_date, due_date, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, id, transaction.getBookId(), transaction.getReaderId(),
                transaction.getTransactionDate(), transaction.getReturnDate(),
                transaction.getDueDate(), transaction.getStatus().name());
        transaction.setId(id);
        return transaction;
    }

    @Transactional
    public void update(Transaction transaction) {
        String sql = """
                UPDATE transactions SET return_date = ?, status = ? WHERE id = ?
                """;
        jdbcTemplate.update(sql, transaction.getReturnDate(), transaction.getStatus().name(), transaction.getId());
    }

    private Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Transaction.builder()
                .id(rs.getObject("id", UUID.class))
                .bookId(rs.getObject("book_id", UUID.class))
                .readerId(rs.getObject("reader_id", UUID.class))
                .transactionDate(rs.getTimestamp("transaction_date").toLocalDateTime())
                .returnDate(rs.getTimestamp("return_date").toLocalDateTime())
                .dueDate(rs.getTimestamp("due_date").toLocalDateTime())
                .status(Transaction.TransactionStatus.valueOf(rs.getString("status")))
                .build();
    }
}
