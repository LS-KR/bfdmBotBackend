package io.github.elihuso.bfdmBotBackend.server;

import io.github.elihuso.bfdmBotBackend.module.Logger;
import io.github.elihuso.bfdmBotBackend.module.style.LoggerLevel;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {
    public static List<String> CommandParser(String command) {
        char[] charset = command.toCharArray();
        char inStr = '\0';
        String s = "";
        List<String> args = new ArrayList<>();
        for (int i = 0; i < charset.length; ++i) {
            if ((charset[i] == ' ') && (inStr == '\0')) {
                if (args.isEmpty() && s.isEmpty()) continue;
                args.add(s);
                s = "";
                continue;
            }
            if ((charset[i] == '"') || (charset[i] == '\'')) {
                if (inStr == '\0')
                    inStr = charset[i];
                else
                    inStr = '\0';
                continue;
            }
            s += charset[i];
        }
        args.add(s);
        return args;
    }

    public static void RunCommand(String[] args) {
        switch (args[0].toLowerCase()) {
            case "check":
                if (args.length < 2) {
                    Logger.Log(LoggerLevel.WARNING, "Error in argument: expect 1, got 0.");
                }
                Logger.Log(LoggerLevel.NOTIFICATION, DataHandler.Check(args[1]));
                break;
            case "add":
                if (args.length < 2) {
                    Logger.Log(LoggerLevel.WARNING, "Error in argument: expect 1, got 0.");
                }
                Logger.Log(LoggerLevel.NOTIFICATION, DataHandler.Add(args[1]));
                break;
            case "del":
                if (args.length < 2) {
                    Logger.Log(LoggerLevel.WARNING, "Error in argument: expect 1, got 0.");
                }
                Logger.Log(LoggerLevel.NOTIFICATION, DataHandler.Del(args[1]));
                break;
            default:
                Logger.Log(LoggerLevel.WARNING, "Unknown Command");
        }
    }
}
