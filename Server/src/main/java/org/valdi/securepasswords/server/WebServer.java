package org.valdi.securepasswords.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;

public class WebServer {
    private final HttpServer server;

    public WebServer() throws UncheckedIOException {
        try {
            this.server = HttpServer.create();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void start() {
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
    }

    public void bind(String ip, int port) throws UncheckedIOException {
        InetSocketAddress address = new InetSocketAddress(ip, port);
        try {
            this.server.bind(address, 0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public InetSocketAddress getAddress() {
        return this.server.getAddress();
    }

    public void createContext(String path, HttpHandler handler) {
        this.server.createContext(path, handler);
    }

    public void removeContext(String path) {
        this.server.removeContext(path);
    }

}
