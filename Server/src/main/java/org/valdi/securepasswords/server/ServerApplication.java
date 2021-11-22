package org.valdi.securepasswords.server;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.valdi.securepasswords.core.ServerPaths;
import org.valdi.securepasswords.server.config.ServerConfig;
import org.valdi.securepasswords.server.handlers.PasswordsHandler;
import org.valdi.securepasswords.server.handlers.AuthenticationHandler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ServerApplication {
    private final Gson gson;

    private ServerConfig config;
    private File configFile;

    private final WebServer server;
    private final Database database;

    public ServerApplication() throws IOException {
        this.gson = new Gson()
                .newBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        this.loadConfig();

        this.database = new Database();
        this.setupDatabase();

        this.server = new WebServer();
        this.setupServer();
        this.startServer();
    }

    public void loadConfig() throws IOException {
        this.configFile = new File(ServerBootstrap.getParentFolder(), "config.json");
        if (!configFile.exists()) {
            configFile.createNewFile();
            this.config = new ServerConfig();
            try (FileWriter fw = new FileWriter(configFile);
                 JsonWriter writer = new JsonWriter(fw)) {
                this.gson.toJson(this.config, ServerConfig.class, writer);
            }
            return;
        }

        this.saveConfig();
    }

    public void saveConfig() throws IOException {
        try (FileReader fr = new FileReader(configFile);
             JsonReader reader = new JsonReader(fr)) {
            this.config = this.gson.fromJson(reader, ServerConfig.class);
        }
    }

    public void setupDatabase() {
        this.getDatabase().setUsername(config.getDbUsername());
        this.getDatabase().setPassword(config.getDbPassword());
        this.getDatabase().setUrl(config.getDbUrl());

        // Test connection
        try(Connection conn = this.getDatabase().getConnection()) {
            System.out.println("Successfully connected to database " + this.getDatabase().getUrl());
        } catch (SQLException e) {
            System.err.println("Can't connect to database!");
            e.printStackTrace();
        }
    }

    public void setupServer() {
        this.getServer().bind(config.getHost(), config.getPort());

        // Register contexts
        this.getServer().createContext(ServerPaths.AUTHENTICATION, new AuthenticationHandler(this));
        this.getServer().createContext(ServerPaths.PASSWORDS, new PasswordsHandler(this));
    }

    public void startServer() {
        System.out.println("Starting server on " + this.server.getAddress());
        this.getServer().start();
    }

    public WebServer getServer() {
        return server;
    }

    public Database getDatabase() {
        return database;
    }

}
