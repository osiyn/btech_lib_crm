package ru.libcrm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private UUID id;
    private String title;
    private String author;
    private Integer itemsTotal;
    private Integer itemsAvailable;

    public boolean isAvailable() {
        return itemsAvailable != null && itemsAvailable > 0;
    }

    public void increase() {
        if(itemsAvailable != null && itemsTotal != null && itemsAvailable < itemsTotal) {
            itemsAvailable++;
        }
    }

    public void decrease() {
        if(itemsAvailable != null && itemsAvailable > 0) {
            itemsAvailable--;
        }
    }
}
