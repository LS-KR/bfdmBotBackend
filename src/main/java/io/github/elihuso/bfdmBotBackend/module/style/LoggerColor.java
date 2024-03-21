package io.github.elihuso.bfdmBotBackend.module.style;

public enum LoggerColor {
    BLACK(0),
    RED(1),
    GREEN(2),
    YELLOW(3),
    BLUE(4),
    MAGENTA(5),
    CYAN(6),
    WHITE(7),
    BRIGHT_BLACK(30),
    BRIGHT_RED(31),
    BRIGHT_GREEN(32),
    BRIGHT_YELLOW(33),
    BRIGHT_BLUE(34),
    BRIGHT_MAGENTA(35),
    BRIGHT_CYAN(36),
    BRIGHT_WHITE(37);

    private final int value;

    LoggerColor(final int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }
}
