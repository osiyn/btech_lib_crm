package ru.libcrm.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Book Model Tests")
public class BookTest {

    @Nested
    @DisplayName("Создание объектов")
    class BuilderTests {

        @Test
        @DisplayName("Создание книг через Builder")
        void buildBook_withAllFields() {
            UUID id = UUID.randomUUID();

            Book book = Book.builder()
                    .id(id)
                    .title("Test Test")
                    .author("Dev Dev")
                    .itemsTotal(5)
                    .itemsAvailable(3)
                    .build();

            assertThat(book.getId()).isEqualTo(id);
            assertThat(book.getTitle()).isEqualTo("Test Test");
            assertThat(book.getAuthor()).isEqualTo("Dev Dev");
            assertThat(book.getItemsTotal()).isEqualTo(5);
            assertThat(book.getItemsAvailable()).isEqualTo(3);
        }
    }
}
