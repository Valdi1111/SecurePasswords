package org.valdi.securepasswords.server.handlers.base;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.securepasswords.core.auth.Authenticated;
import org.valdi.securepasswords.server.ServerApplication;

import java.io.IOException;

public abstract class AuthAbstractHandler extends AbstractHandler {

    public AuthAbstractHandler(ServerApplication main) {
        super(main);
    }

    public abstract void handle(Authenticated auth, HttpExchange exchange) throws IOException;

}
