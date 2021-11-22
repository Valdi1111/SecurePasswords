package org.valdi.securepasswords.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.securepasswords.core.auth.*;
import org.valdi.securepasswords.core.Utils;
import org.valdi.securepasswords.server.ServerApplication;
import org.valdi.securepasswords.server.handlers.base.SimpleAbstractHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationHandler extends SimpleAbstractHandler {

    public AuthenticationHandler(final ServerApplication main) {
        super(main);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            return;
        }

        String body = Utils.readString(exchange.getRequestBody());
        Authorization authorization = gson.fromJson(body, Authorization.class);
        Result result = Result.INVALID_USERNAME;

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM login WHERE username=?");
            statement.setString(1, authorization.getUsername());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                result = Result.WRONG_PASSWORD;
                int id = rs.getInt("id");
                String password = rs.getString("password");
                String token = rs.getString("token");
                if (password.equals(authorization.getPassword())) {
                    result = Result.SUCCESS;

                    Authenticated authenticated = new Authenticated();
                    authenticated.setId(id);
                    authenticated.setToken(token);
                    authenticated.setDate(System.currentTimeMillis());

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    body = gson.toJson(authenticated); // serialize result to json
                }
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (result != Result.SUCCESS) {
            Unauthenticated unauthenticated = new Unauthenticated();
            unauthenticated.setError(result.getError());
            unauthenticated.setMessage(result.getMessage());

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
            body = gson.toJson(unauthenticated); // serialize result to json
        }

        Utils.writeString(exchange.getResponseBody(), body);
        exchange.getResponseBody().close();
        exchange.close();
    }

}
