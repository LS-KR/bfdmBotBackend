package io.github.elihuso.bfdmBotBackend.data;

import io.github.elihuso.bfdmBotBackend.data.interfaces.InBound;

public class InBounds implements InBound {
    public String method;
    public String id;

    public InBounds(String method, String id) {
        this.method = method;
        this.id = id;
    }
}
