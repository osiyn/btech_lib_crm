package ru.libcrm.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.libcrm.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Book> rowMapper = this::mapRow;

    public Optional<Book> findById(UUID id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public List<Book> findAll() {
        String sql = "SELECT * FROM books OREDER BY title";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Book> findByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title ? ORDER BY title";
        return jdbcTemplate.query(sql, rowMapper, "%" + title + "%");
    }

    @Transactional
    public Book save(Book book) {
        UUID id = book.getId() != null ? book.getId() : UUID.randomUUID();

        String sql = """
                INSERT INTO books (id, title, author, items_total, items_available)
                VALUES (?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, id, book.getTitle(), book.getAuthor(),
                book.getItemsTotal(), book.getItemsAvailable());
        book.setId(id);
        return book;
    }

    @Transactional
    public void updateItems(UUID bookId, int available) {
        String sql = "UPDATE books SET items_available = ? WHERE id = ?";
        jdbcTemplate.update(sql, available, bookId);
    }

    public List<Book> findPopular(int limit) {
        String sql = """
                SELECT b.*, COUNT(t.id) as transaction_count
                FROM books b
                LEFT JOIN transactions t ON b.id = t.book_id
                GROUP BY b.id
                ORDER BY transaction_count DESC, b.title
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, rowMapper, limit);
    }

    private Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Book.builder()
                .id(rs.getObject("id", UUID.class))
                .title(rs.getString("title"))
                .author(rs.getString("author"))
                .itemsTotal(rs.getInt("items_total"))
                .itemsAvailable(rs.getInt("items_available"))
                .build();
    }
}
