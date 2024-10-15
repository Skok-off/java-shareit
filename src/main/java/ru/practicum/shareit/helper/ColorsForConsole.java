package ru.practicum.shareit.helper;

public enum ColorsForConsole {
    ANSI_RESET("\u001B[0m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_HIGH_YELLOW("\u001B[93m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m");

    private final String code;

    ColorsForConsole(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
