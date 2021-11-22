package org.valdi.securepasswords.client.config;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class DesktopConfig {
    @Expose
    private List<ServerEntry> servers = new ArrayList<>();

    public DesktopConfig() {
    }

    public List<ServerEntry> getServers() {
        return servers;
    }

    public void setServers(List<ServerEntry> servers) {
        this.servers = servers;
    }

}
