package org.valdi.securepasswords.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class DesktopApplication extends Application {
    public static DesktopApplication INSTANCE;

    public DesktopApplication() {
        INSTANCE = this;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DesktopBootstrap.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        InputStream is = DesktopBootstrap.class.getResourceAsStream("favicon.png");
        stage.getIcons().add(new Image(is));
        stage.setTitle("SecurePasswords - Login");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

}