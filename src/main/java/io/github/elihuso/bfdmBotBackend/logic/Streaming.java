package io.github.elihuso.bfdmBotBackend.logic;

import java.io.InputStream;
import java.util.Scanner;

public class Streaming {
    public static String ReadInputStream(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine()).append(String.format("%n"));
        }
        scanner.close();
        return builder.toString();
    }
}
