package io.github.elihuso.bfdmBotBackend.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.elihuso.bfdmBotBackend.Main;
import io.github.elihuso.bfdmBotBackend.data.InBounds;
import io.github.elihuso.bfdmBotBackend.logic.Streaming;
import io.github.elihuso.bfdmBotBackend.module.Logger;
import io.github.elihuso.bfdmBotBackend.module.style.LoggerLevel;

import java.io.IOException;
import java.io.OutputStream;

public class MainPostHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        Headers headers = httpExchange.getRequestHeaders();
        String headerAuth = headers.getFirst("Authentication");

        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
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
        Logger.Log(LoggerLevel.NOTIFICATION, request.replaceAll("\n", ""));
        JsonObject object = new Gson().fromJson(request, JsonObject.class);
        InBounds inBounds = new InBounds(object.get("method").getAsString(), object.get("id").getAsString());
        String method = inBounds.method;
        String id = inBounds.id;
        int status = 200;

        switch (method.toLowerCase()) {
            case "check":
                response = DataHandler.Check(id);
                break;
            case "add":
                if (headerAuth.equals(Main.auth)) response = DataHandler.Add(id);
                else {
                    status = 403;
                    response = "";
                    Logger.Log(LoggerLevel.WARNING, "Error auth: " + headerAuth);
                }
                break;
            case "del":
                if (headerAuth.equals(Main.auth)) response = DataHandler.Del(id);
                else {
                    status = 403;
                    response = "";
                    Logger.Log(LoggerLevel.WARNING, "Error auth: " + headerAuth);
                }
                break;
            default:
                response = "{\"result\":\"invalid method\"}";
        }
        httpExchange.sendResponseHeaders(status, response.getBytes().length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
