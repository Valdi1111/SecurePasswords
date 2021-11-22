package org.valdi.securepasswords.server.handlers.base;

import com.sun.net.httpserver.HttpHandler;
import org.valdi.securepasswords.server.ServerApplication;

public abstract class SimpleAbstractHandler extends AbstractHandler implements HttpHandler {

    public SimpleAbstractHandler(ServerApplication main) {
        super(main);
    }

}
