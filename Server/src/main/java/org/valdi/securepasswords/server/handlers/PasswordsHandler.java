package org.valdi.securepasswords.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.securepasswords.core.auth.Authenticated;
import org.valdi.securepasswords.server.ServerApplication;
import org.valdi.securepasswords.server.handlers.base.SimpleAbstractHandler;
import org.valdi.securepasswords.server.handlers.passwords.AddHandler;
import org.valdi.securepasswords.server.handlers.passwords.DeleteHandler;
import org.valdi.securepasswords.server.handlers.passwords.EditHandler;
import org.valdi.securepasswords.server.handlers.passwords.GetHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.*;

public class PasswordsHandler extends SimpleAbstractHandler {
    private final GetHandler get;
    private final AddHandler add;
    private final EditHandler edit;
    private final DeleteHandler delete;

    public PasswordsHandler(final ServerApplication main) {
        super(main);

        this.get = new GetHandler(main);
        this.add = new AddHandler(main);
        this.edit = new EditHandler(main);
        this.delete = new DeleteHandler(main);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String t = exchange.getRequestHeaders().getFirst("Authorization");
        if(t == null) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_UNAUTHORIZED);
            return;
        }

        String[] token = t.split(" ", 2);
        if(!token[0].equals("Bearer")) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_UNAUTHORIZED);
            return;
        }

        Authenticated auth = new Authenticated();
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement stm = conn.prepareStatement("SELECT id FROM login WHERE token=?");
            stm.setString(1, token[1]);
            ResultSet rs = stm.executeQuery();
            if(!rs.next()) {
                this.sendResponse(exchange, HttpURLConnection.HTTP_UNAUTHORIZED);
                return;
            }

            auth.setId(rs.getInt("id"));
            auth.setToken(token[1]);
            auth.setDate(System.currentTimeMillis());
            rs.close();
            stm.close();
        } catch (SQLException e) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
            return;
        }

        if (exchange.getRequestMethod().equals("GET")) {
            this.get.handle(auth, exchange);
        }
        else if (exchange.getRequestMethod().equals("POST")) {
            this.add.handle(auth, exchange);
        }
        else if (exchange.getRequestMethod().equals("PUT")) {
            this.edit.handle(auth, exchange);
        }
        else if (exchange.getRequestMethod().equals("DELETE")) {
            this.delete.handle(auth, exchange);
        }
        else {
            // Send response for bad method
            this.sendResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD);
        }
    }

}
