package org.valdi.securepasswords.server.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerConfig {
    @Expose
    private String host = "127.0.0.1";
    @Expose
    private int port = 4444;

    @Expose
    @SerializedName("db-url")
    private String dbUrl = "jdbc:mysql://127.0.0.1:3306/database";
    @Expose
    @SerializedName("db-username")
    private String dbUsername = "username";
    @Expose
    @SerializedName("db-password")
    private String dbPassword = "password";

    public ServerConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

}
