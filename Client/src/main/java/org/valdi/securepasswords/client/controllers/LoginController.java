package org.valdi.securepasswords.client.controllers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.valdi.securepasswords.client.DesktopBootstrap;
import org.valdi.securepasswords.client.config.DesktopConfig;
import org.valdi.securepasswords.client.config.ServerEntry;
import org.valdi.securepasswords.core.ServerPaths;
import org.valdi.securepasswords.core.auth.Authenticated;
import org.valdi.securepasswords.core.auth.Authorization;
import org.valdi.securepasswords.core.auth.Unauthenticated;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

public class LoginController {
    private final Gson gson;

    private DesktopConfig config;
    private File configFile;

    @FXML
    private ChoiceBox<ServerEntry> serverChoice;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    public LoginController() throws IOException {
        this.gson = new Gson()
                .newBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        this.loadConfig();
    }

    public void loadConfig() throws IOException {
        this.configFile = new File(DesktopBootstrap.getParentFolder(), "config.json");
        if (!configFile.exists()) {
            configFile.createNewFile();
            this.config = new DesktopConfig();
            try (FileWriter fw = new FileWriter(configFile);
                 JsonWriter writer = new JsonWriter(fw)) {
                this.gson.toJson(this.config, DesktopConfig.class, writer);
            }
            return;
        }

        this.saveConfig();
    }

    public void saveConfig() throws IOException {
        try (FileReader fr = new FileReader(configFile);
             JsonReader reader = new JsonReader(fr)) {
            this.config = this.gson.fromJson(reader, DesktopConfig.class);
        }
    }

    @FXML
    private void initialize() {
        this.serverChoice.setConverter(new StringConverter<>() {
            @Override
            public String toString(ServerEntry server) {
                return server.getName();
            }

            @Override
            public ServerEntry fromString(String name) {
                return config.getServers().stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
            }
        });
        this.serverChoice.getItems().addAll(config.getServers());
    }

    @FXML
    private void addServer() {
        // TODO add server
    }

    @FXML
    private void onLogin() throws IOException, InterruptedException {
        if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
            // TODO warning username | password null
            return;
        }

        ServerEntry server = serverChoice.getSelectionModel().getSelectedItem();
        if (server == null) {
            // TODO warning server null
            return;
        }

        Authorization authorization = new Authorization();
        authorization.setUsername(usernameField.getText().trim());
        authorization.setPassword(passwordField.getText());

        // TODO load ip:port from config
        String body = gson.toJson(authorization);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(server.createURI(ServerPaths.AUTHENTICATION))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        System.out.println("Handling response...");
        int code = response.statusCode();
        body = response.body();

        if (code == HttpURLConnection.HTTP_OK) {
            Authenticated auth = gson.fromJson(body, Authenticated.class);
            System.out.println(auth);
            System.out.println("Login success! " + new Date(auth.getDate()));

            FXMLLoader loader = new FXMLLoader(DesktopBootstrap.class.getResource("main-view.fxml"));
            loader.setControllerFactory((clazz) -> new MainController(server, auth));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            Stage primaryStage = (Stage) this.loginButton.getScene().getWindow();
            stage.getIcons().addAll(primaryStage.getIcons());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);

            primaryStage.hide();
            stage.setTitle("SecurePasswords");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } else if (code == HttpURLConnection.HTTP_FORBIDDEN) {
            Unauthenticated auth = gson.fromJson(body, Unauthenticated.class);
            System.out.println(auth);
            // TODO error warning
        }
    }

    @FXML
    private void onEnter(KeyEvent event) throws IOException, InterruptedException {
        if (event.getCode() != KeyCode.ENTER) {
            return;
        }

        this.onLogin();
    }

}