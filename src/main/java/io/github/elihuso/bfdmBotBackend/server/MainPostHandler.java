package io.github.elihuso.bfdmBotBackend.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.elihuso.bfdmBotBackend.Main;
import io.github.elihuso.bfdmBotBackend.data.InBounds;
import io.github.elihuso.bfdmBotBackend.logic.BloomFilter;
import io.github.elihuso.bfdmBotBackend.logic.Streaming;
import io.github.elihuso.bfdmBotBackend.module.Logger;
import io.github.elihuso.bfdmBotBackend.module.style.LoggerLevel;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MainPostHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        if (!httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
            httpExchange.sendResponseHeaders(403, response.getBytes().length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
            return;
        }
        String request = Streaming.ReadInputStream(httpExchange.getRequestBody());
        Logger.Log(LoggerLevel.NOTIFICATION, request);
        JsonObject object = new Gson().fromJson(request, JsonObject.class);
        InBounds inBounds = new InBounds(object.get("method").getAsString(), object.get("id").getAsString());
        try {
            BloomFilter bf = new BloomFilter(Main.size, MessageDigest.getInstance(Main.encrypt));
            bf.loadFromFile(Main.DatabasePath);
            if (bf.mightHas(inBounds.id.getBytes(StandardCharsets.US_ASCII))) {
                response = "{\"result\":true}";
            }
            else {
                response = "{\"result\":false}";
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
