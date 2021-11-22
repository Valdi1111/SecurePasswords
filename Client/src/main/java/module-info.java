module org.valdi.securepasswords.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    requires java.net.http;
    requires com.google.gson;

    requires org.valdi.securepasswords.core;

    opens org.valdi.securepasswords.client to javafx.fxml;
    opens org.valdi.securepasswords.client.config to javafx.fxml;
    opens org.valdi.securepasswords.client.controllers to javafx.fxml;

    exports org.valdi.securepasswords.client;
    exports org.valdi.securepasswords.client.config;
    exports org.valdi.securepasswords.client.controllers;
}