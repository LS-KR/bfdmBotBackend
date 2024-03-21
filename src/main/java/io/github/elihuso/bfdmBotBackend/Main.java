package io.github.elihuso.bfdmBotBackend;

import com.sun.net.httpserver.HttpServer;
import io.github.elihuso.bfdmBotBackend.logic.BloomFilter;
import io.github.elihuso.bfdmBotBackend.logic.Streaming;
import io.github.elihuso.bfdmBotBackend.module.Logger;
import io.github.elihuso.bfdmBotBackend.module.style.LoggerLevel;
import io.github.elihuso.bfdmBotBackend.server.MainPostHandler;
import org.ini4j.Ini;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    @Option(name = "-c", usage = "Config Path")
    public static String ConfigPath = "./config.ini";
    @Option(name = "-d", usage = "Database location")
    public static String DatabasePath = "./list.bfdb";
    @Option(name = "-p", usage = "Port")
    public static int port = 8964;
    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static String encrypt = "SHA-256";
    public static int size = 2147483647;


    public static void main(String[] args) {
        List as = Arrays.asList(args);
        if (as.contains("-h")) {
            System.out.println("-c <config path>    Specify where the config file is, default as ./config.ini");
            System.out.println("-p <port>           Specify the port, default as 8964");
            System.out.println("-d <database port>  Specify where the database is, default as ./list.bfdb");
            return;
        }
        new Main().ParserMain(args);
    }

    public void ParserMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(80);
        try {
            parser.parseArgument(args);
        }
        catch (Exception ex) {
            Logger.Log(LoggerLevel.NEGATIVE, "Error in parse arguments");
            Logger.Log(LoggerLevel.WARNING, "use -h for help");
            return;
        }
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        }
        catch (IOException ex) {
            Logger.Log(LoggerLevel.NEGATIVE, "Error when creating server: " + ex.getMessage());
            return;
        }
        try {
            initialConfig();
        }
        catch (IOException ex) {
            Logger.Log(LoggerLevel.NEGATIVE, "Error when initial config: " + ex.getMessage());
            return;
        }
        try {
            initialDatabase();
        }
        catch (IOException ex) {
            Logger.Log(LoggerLevel.NEGATIVE, "Error when initial database: " + ex.getMessage());
            return;
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.Log(LoggerLevel.NEGATIVE, "No such algorithm: " + encrypt);
            Logger.Log(LoggerLevel.NEGATIVE, ex.getMessage());
            return;
        }
        server.createContext("/record.json", new MainPostHandler());
    }

    public static void initialConfig() throws IOException{
        File config = new File(ConfigPath);
        if (!config.exists()) {
            config.createNewFile();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream is = loader.getResourceAsStream("config.ini");
            String s = Streaming.ReadInputStream(is);
            Files.write(config.toPath().toAbsolutePath(), s.getBytes(StandardCharsets.UTF_8));
        }
        Ini ini = new Ini(config);
        if (ini.get("DEFAULT").containsKey("encrypt"))
            encrypt = ini.get("DEFAULT", "encrypt");
        if (ini.get("DEFAULT").containsKey("size"))
            size = Integer.getInteger(ini.get("DEFAULT", "size")).intValue();
    }

    public static void initialDatabase() throws IOException, NoSuchAlgorithmException {
        File db = new File(DatabasePath);
        if (!db.exists()) {
            db.createNewFile();
            BloomFilter bf = new BloomFilter(size, MessageDigest.getInstance(encrypt));
            bf.add("1249822768".getBytes(StandardCharsets.US_ASCII));
            bf.add("6514042007".getBytes(StandardCharsets.US_ASCII));
            bf.writeIntoFile(db);
        }
    }
}