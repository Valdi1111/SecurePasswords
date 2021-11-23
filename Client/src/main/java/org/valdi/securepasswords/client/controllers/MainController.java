package org.valdi.securepasswords.client.controllers;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.valdi.securepasswords.client.DesktopApplication;
import org.valdi.securepasswords.client.DesktopBootstrap;
import org.valdi.securepasswords.client.config.ServerEntry;
import org.valdi.securepasswords.core.ServerPaths;
import org.valdi.securepasswords.core.auth.Authenticated;
import org.valdi.securepasswords.core.objects.Password;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MainController {
    private final ServerEntry server;
    private final Authenticated auth;

    private final HttpClient client;
    private final Gson gson;

    @FXML
    private ListView<Password> passwordListView;

    public MainController(ServerEntry server, Authenticated auth) {
        this.server = server;
        this.auth = auth;

        this.client = HttpClient.newHttpClient();
        this.gson = new Gson()
                .newBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();
    }

    @FXML
    private void initialize() throws IOException, InterruptedException {
        this.passwordListView.setCellFactory(lv -> {
            TextFieldListCell<Password> cell = new TextFieldListCell<>();
            cell.setConverter(new StringConverter<>() {
                @Override
                public String toString(Password password) {
                    return password.getName();
                }

                @Override
                public Password fromString(String string) {
                    return null;
                }
            });
            return cell;
        });

        this.loadPasswords();
    }

    private void loadPasswords() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(server.createURI(ServerPaths.PASSWORDS))
                .header("Authorization", "Bearer " + this.auth.getToken())
                .GET()
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        int code = response.statusCode();
        String body = response.body();
        if (code == HttpURLConnection.HTTP_OK) {
            Password.List passwords = this.gson.fromJson(body, Password.List.class);
            this.passwordListView.getItems().addAll(passwords);
            this.passwordListView.refresh();
        } else {
            System.out.println("Error on load, code " + code);
        }
    }

    @FXML
    private void onClick(MouseEvent event) throws IOException {
        if (this.passwordListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        if (event.getClickCount() == 3) {
            this.onOpenLink();
        }

        if (event.getClickCount() == 2) {
            this.onView();
        }
    }

    private void onOpenLink() {
        String link = this.passwordListView.getSelectionModel().getSelectedItem().getLink();
        if (link == null || link.isBlank()) {
            return;
        }

        DesktopApplication.INSTANCE.getHostServices().showDocument(link);
    }

    private void onView() throws IOException {
        final Password pass = this.passwordListView.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(DesktopBootstrap.class.getResource("show-view.fxml"));
        loader.setControllerFactory((clazz) -> new ShowController(pass));
        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        Stage primaryStage = (Stage) this.passwordListView.getScene().getWindow();
        stage.getIcons().addAll(primaryStage.getIcons());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);

        //primaryStage.hide();
        stage.setTitle("SecurePasswords - View");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onAdd() throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(DesktopBootstrap.class.getResource("edit-view.fxml"));
        loader.setControllerFactory((clazz) -> new EditController());
        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        Stage primaryStage = (Stage) this.passwordListView.getScene().getWindow();
        stage.getIcons().addAll(primaryStage.getIcons());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);

        //primaryStage.hide();
        stage.setTitle("SecurePasswords - Add");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();

        EditController controller = loader.getController();
        Password pass = controller.getPassword();
        if (pass == null) {
            return;
        }

        String body = this.gson.toJson(pass);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(server.createURI(ServerPaths.PASSWORDS))
                .header("Authorization", "Bearer " + this.auth.getToken())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        int code = response.statusCode();
        body = response.body();
        if (code == HttpURLConnection.HTTP_CREATED) {
            pass = this.gson.fromJson(body, Password.class);
            this.passwordListView.getItems().add(pass);
            this.passwordListView.refresh();
        } else {
            System.out.println("Error on add, code " + code);
        }
    }

    @FXML
    private void onEdit() throws IOException, InterruptedException {
        final Password password = this.passwordListView.getSelectionModel().getSelectedItem();
        if (password == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(DesktopBootstrap.class.getResource("edit-view.fxml"));
        loader.setControllerFactory((clazz) -> new EditController(password));
        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        Stage primaryStage = (Stage) this.passwordListView.getScene().getWindow();
        stage.getIcons().addAll(primaryStage.getIcons());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);

        //primaryStage.hide();
        stage.setTitle("SecurePasswords - Edit");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();

        EditController controller = loader.getController();
        Password pass = controller.getPassword();
        if (pass == null) {
            return;
        }

        String body = this.gson.toJson(pass);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(server.createURI(ServerPaths.PASSWORDS))
                .header("Authorization", "Bearer " + this.auth.getToken())
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        int code = response.statusCode();
        //body = response.body();
        if (code == HttpURLConnection.HTTP_OK) {
            this.passwordListView.getItems().remove(pass);
            this.passwordListView.getItems().add(pass);
            this.passwordListView.refresh();
        } else {
            System.out.println("Error on edit, code " + code);
        }
    }

    @FXML
    private void onDelete() throws IOException, InterruptedException {
        Password pass = this.passwordListView.getSelectionModel().getSelectedItem();
        if (pass == null) {
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(server.createURI(ServerPaths.PASSWORDS, "id=" + pass.getId()))
                .header("Authorization", "Bearer " + this.auth.getToken())
                .DELETE()
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        int code = response.statusCode();
        if (code == HttpURLConnection.HTTP_OK) {
            this.passwordListView.getItems().remove(pass);
            this.passwordListView.refresh();
        } else {
            System.out.println("Error on delete, code " + code);
        }
    }

    @FXML
    private void onExit() {

    }

}
