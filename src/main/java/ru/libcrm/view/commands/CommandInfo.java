package ru.libcrm.view.commands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommandInfo {
    private String key;
    private String name;
    private String description;
    private String category;
    private boolean requiresConfirmation;
    private int shortcut;
}
