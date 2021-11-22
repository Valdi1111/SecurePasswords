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

public class EditHandler extends AuthAbstractHandler {

    public EditHandler(final ServerApplication main) {
        super(main);
    }

    // 200 OK
    // 204 NO_CONTENT
    @Override
    public void handle(Authenticated auth, HttpExchange exchange) throws IOException {
        String body = Utils.readString(exchange.getRequestBody());
        Password password = gson.fromJson(body, Password.class);

        // Update element if exists
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement stm = conn.prepareStatement("UPDATE password SET name=?,link=? WHERE id=?");
            stm.setString(1, password.getName());
            stm.setString(2, password.getLink());
            stm.setInt(3, password.getId());
            int inserted = stm.executeUpdate();
            if (inserted == 0) {
                this.sendResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT);
                return;
            }
            stm.close();
        } catch (SQLException e) {
            this.sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
            return;
        }

        // Delete password elements
        try (Connection conn = main.getDatabase().getConnection()) {
            PreparedStatement stm = conn.prepareStatement("DELETE FROM password_element WHERE id=?");
            stm.setInt(1, password.getId());
            stm.executeUpdate();
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

        this.sendResponse(exchange, HttpURLConnection.HTTP_OK);
    }

}
