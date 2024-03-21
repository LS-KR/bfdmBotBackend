package io.github.elihuso.bfdmBotBackend.data.interfaces;

public interface InBound {
    String method = null;
    String id = null;

    public default String getMethod() {
        return method;
    }

    public default String getId() {
        return id;
    }
}
