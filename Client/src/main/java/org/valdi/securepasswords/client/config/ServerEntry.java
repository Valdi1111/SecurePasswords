package org.valdi.securepasswords.client.config;

import com.google.gson.annotations.Expose;

import java.net.URI;
import java.net.URISyntaxException;

public class ServerEntry {
    @Expose
    private String name;
    @Expose
    private String scheme;
    @Expose
    private String host;
    @Expose
    private int port;

    public ServerEntry() {
    }

    public String getName() {
        return name;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setName(String name) {
        this.name = name;
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

    public URI createURI(String path) {
        return createURI(path, null, null);
    }

    public URI createURI(String path, String query) {
        return createURI(path, query, null);
    }

    public URI createURI(String path, String query, String fragment) {
        try {
            return new URI(scheme, null, host, port, path, query, fragment);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerEntry)) return false;

        ServerEntry that = (ServerEntry) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
