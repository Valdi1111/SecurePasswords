package org.valdi.securepasswords.server.handlers.passwords;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.securepasswords.core.Utils;
import org.valdi.securepasswords.core.auth.Authenticated;
import org.valdi.securepasswords.core.objects.Password;
import org.valdi.securepasswords.server.ServerApplication;
import org.valdi.securepasswords.server.handlers.base.AuthAbstractHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetHandler extends AuthAbstractHandler {

    public GetHandler(final ServerApplication main) {
        super(main);
    }

    // 200 OK,
    // 404 NOT_FOUND,
    // 400 BAD_REQUEST
    @Override
    public void handle(Authenticated auth, HttpExchange exchange) throws IOException {
        Password.List list = new Password.List();

        // Load passwords
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM password");
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                Password password = new Password();
                password.setId(rs.getInt("id"));
                password.setName(rs.getString("name"));
                password.setLink(rs.getString("link"));
                list.add(password);
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
            return;
        }

        // Load password elements
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM password_element");
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                Password.Element element = new Password.Element();
                element.setName(rs.getString("name"));
                element.setValue(rs.getString("value"));
                element.setHide(rs.getBoolean("hide"));

                int id = rs.getInt("id");
                list.stream().filter(p -> p.getId() == id).findFirst().ifPresent(p -> p.addElement(element));
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
            return;
        }

        // Send loaded elements to client
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        String body = gson.toJson(list); // serialize result to json
        Utils.writeString(exchange.getResponseBody(), body);
        exchange.getResponseBody().close();
        exchange.close();
    }

}
