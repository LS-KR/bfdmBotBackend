package io.github.elihuso.bfdmBotBackend.logic;

import java.util.UUID;

public class Randoms {
    public static String RandomString(int length) {
        char[] charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_".toCharArray();
        String s = "";
        for (int i = 0; i < length; ++i) {
            s += charset[(int) (Math.random() * charset.length)];
        }
        return s;
    }

    public static UUID RandomUUID() {
        return UUID.randomUUID();
    }
}
