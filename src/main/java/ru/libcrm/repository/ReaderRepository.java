package ru.libcrm.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.libcrm.model.Reader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReaderRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reader> rowMapper = this::mapRow;

    public Optional<Reader> findById(UUID id) {
        String sql = "SELECT * FROM readers WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public List<Reader> findAll() {
        return jdbcTemplate.query("SELECT * FROM readers ORDER BY last_name, first_name", rowMapper);
    }

    public Optional<Reader> findByEmail(String email) {
        String sql = "SELECT * FROM readers WHERE email = ?";
        return jdbcTemplate.query(sql, rowMapper, email).stream().findFirst();
    }

    @Transactional
    public Reader save(Reader reader) {
        UUID id = reader.getId() != null ? reader.getId() : UUID.randomUUID();
        String sql = """
                INSERT INTO readers (id, first_name, last_name, email, phone, registration_date, is_active)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, id, reader.getFirstName(), reader.getLastName(),
                reader.getEmail(), reader.getPhone(), reader.getRegistrationDate(),
                reader.getIsActive() != null ? reader.getIsActive() : true);
        reader.setId(id);
        return reader;
    }

    private Reader mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Reader.builder()
                .id(rs.getObject("id", UUID.class))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .registrationDate(rs.getTimestamp("registration_date").toLocalDateTime())
                .isActive(rs.getBoolean("is_active"))
                .build();
    }

}
