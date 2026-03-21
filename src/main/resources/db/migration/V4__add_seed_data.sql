-- Книги
INSERT INTO books (title, author, items_total, items_available) VALUES
    ('Effective Java', 'Joshua Bloch', 3, 3),
    ('Clean Code', 'Robert C. Martin', 2, 2),
    ('Design Patterns', 'Gang of Four',2, 2),
    ('Spring in Action', 'Craig Walls',3, 3);

-- Читатели
INSERT INTO readers (first_name, last_name, email, phone) VALUES
    ('Иван', 'Петров', 'ivan.petrov@example.com', '+7-900-111-22-33'),
    ('Анна', 'Сидорова', 'anna.sidorova@example.com', '+7-900-444-55-66'),
    ('Дмитрий', 'Козлов', 'dmitry.kozlov@example.com', '+7-900-777-88-99');