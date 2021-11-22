package org.valdi.securepasswords.server.handlers.passwords;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.securepasswords.core.Utils;
import org.valdi.securepasswords.core.auth.Authenticated;
import org.valdi.securepasswords.core.objects.Password;
import org.valdi.securepasswords.server.ServerApplication;
import org.valdi.securepasswords.server.handlers.base.AuthAbstractHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.*;

public class AddHandler extends AuthAbstractHandler {

    public AddHandler(final ServerApplication main) {
        super(main);
    }

    // 201 CREATED
    // 400 BAD_REQUEST
    @Override
    public void handle(Authenticated auth, HttpExchange exchange) throws IOException {
        String body = Utils.readString(exchange.getRequestBody());
        Password password = gson.fromJson(body, Password.class);

        // Create password
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO password (user, name, link) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, auth.getId());
            stm.setString(2, password.getName());
            stm.setString(3, password.getLink());
            int inserted = stm.executeUpdate();
            if (inserted == 0) {
                throw new SQLException("Creating password failed, no rows affected.");
            }

            ResultSet rs = stm.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Creating password failed, no key generated.");
            }

            int id = rs.getInt(1);
            password.setId(id);
            rs.close();
            stm.close();
        } catch (SQLException e) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
            return;
        }

        // Create password elements
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO password_element (id, name, value, hide) VALUES (?, ?, ?, ?)");
            for(Password.Element element : password) {
                stm.setInt(1, password.getId());
                stm.setString(2, element.getName());
                stm.setString(3, element.getValue());
                stm.setBoolean(4, element.isHide());
                stm.addBatch();
            }
            int[] inserted = stm.executeBatch();
            for(int ins : inserted) {
                if (ins == 0) {
                    throw new SQLException("Creating password element failed, no rows affected.");
                }
            }
            stm.close();
        } catch (SQLException e) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
            return;
        }

        // Send created element to client
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_CREATED, 0);

        body = gson.toJson(password); // serialize result to json
        Utils.writeString(exchange.getResponseBody(), body);
        exchange.getResponseBody().close();
        exchange.close();
    }

}
