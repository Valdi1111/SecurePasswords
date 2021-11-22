module org.valdi.securepasswords.server {
    requires java.sql;
    requires java.net.http;
    requires org.valdi.securepasswords.core;
    requires com.google.gson;
    requires jdk.httpserver;

    opens org.valdi.securepasswords.server.config to com.google.gson;

    exports org.valdi.securepasswords.server;
    exports org.valdi.securepasswords.server.config;
}