package io.github.elihuso.bfdmBotBackend.server;

import io.github.elihuso.bfdmBotBackend.Main;
import io.github.elihuso.bfdmBotBackend.logic.BloomFilter;
import io.github.elihuso.bfdmBotBackend.module.Logger;
import io.github.elihuso.bfdmBotBackend.module.style.LoggerLevel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class DataHandler {
    public static String Check(String id) {
        try {
            BloomFilter bf = new BloomFilter(Main.size, MessageDigest.getInstance(Main.encrypt));
            bf.loadFromFile(Main.DatabasePath);
            if (bf.mightHas(id.getBytes(StandardCharsets.US_ASCII))) {
                return "{\"result\":true}";
            }
            else {
                return "{\"result\":false}";
            }
        }
        catch (Exception e) {
            Logger.Log(LoggerLevel.WARNING, "Failed to check '" + id + "': " + e.getMessage());
            return "{\"result\":\"failed: " + e.getMessage() + "\"}";
        }
    }

    public static String Add(String id) {
        try {
            BloomFilter bf = new BloomFilter(Main.size, MessageDigest.getInstance(Main.encrypt));
            bf.loadFromFile(Main.DatabasePath);
            bf.add(id.getBytes(StandardCharsets.US_ASCII));
            bf.writeIntoFile(Main.DatabasePath);
            return "{\"result\":\"success\"}";
        }
        catch (Exception e) {
            Logger.Log(LoggerLevel.WARNING, "Failed to add '" + id + "': " + e.getMessage());
            return "{\"result\":\"failed: " + e.getMessage() + "\"}";
        }
    }

    public static String Del(String id) {
        try {
            BloomFilter bf = new BloomFilter(Main.size, MessageDigest.getInstance(Main.encrypt));
            bf.loadFromFile(Main.DatabasePath);
            bf.del(id.getBytes(StandardCharsets.US_ASCII));
            bf.writeIntoFile(Main.DatabasePath);
            return "{\"result\":\"success\"}";
        }
        catch (Exception e) {
            Logger.Log(LoggerLevel.WARNING, "Failed to del '" + id + "': " + e.getMessage());
            return "{\"result\":\"failed: " + e.getMessage() + "\"}";
        }
    }
}
