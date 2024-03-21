package io.github.elihuso.bfdmBotBackend.module.style;

public enum LoggerGround {
    FOREGROUND(30),
    BACKGROUND(40);

    private final int value;

    LoggerGround(final int v) {
        this.value = v;
    }

    public int getValue() {
        return value;
    }
}
