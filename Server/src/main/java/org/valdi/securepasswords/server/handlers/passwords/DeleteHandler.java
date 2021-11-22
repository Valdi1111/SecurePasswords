package org.valdi.securepasswords.server.handlers.passwords;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.securepasswords.core.Utils;
import org.valdi.securepasswords.core.auth.Authenticated;
import org.valdi.securepasswords.server.ServerApplication;
import org.valdi.securepasswords.server.handlers.base.AuthAbstractHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class DeleteHandler extends AuthAbstractHandler {

    public DeleteHandler(final ServerApplication main) {
        super(main);
    }

    // 200 OK
    // 204 NO_CONTENT
    @Override
    public void handle(Authenticated auth, HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = Utils.getParametersMap(query);
        if (!params.containsKey("id")) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }

        int id = 0;
        try {
            id = Integer.parseInt(params.get("id"));
        } catch (NumberFormatException e) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }

        // Delete password elements
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement stm = conn.prepareStatement("DELETE FROM password_element WHERE id=?");
            stm.setInt(1, id);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
            return;
        }

        // Delete password
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement stm = conn.prepareStatement("DELETE FROM password WHERE id=?");
            stm.setInt(1, id);
            int deleted = stm.executeUpdate();
            if (deleted == 0) {
                this.sendResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT);
            } else {
                this.sendResponse(exchange, HttpURLConnection.HTTP_OK);
            }
            stm.close();
        } catch (SQLException e) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
        }
    }

}
