package org.valdi.securepasswords.server.handlers.base;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.valdi.securepasswords.server.ServerApplication;

import java.io.IOException;

public abstract class AbstractHandler {
    protected final ServerApplication main;
    protected final Gson gson;

    public AbstractHandler(final ServerApplication main) {
        this.main = main;
        this.gson = new Gson()
                .newBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();
    }

    public void sendResponse(HttpExchange exchange, int code) throws IOException {
        exchange.sendResponseHeaders(code, 0);
        exchange.getResponseBody().close();
        exchange.close();
    }

}
