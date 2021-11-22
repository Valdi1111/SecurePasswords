package org.valdi.securepasswords.server;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class ServerBootstrap {
    private static File jar;

    public static void main(String[] args) throws IOException {
        try {
            URL url = ServerBootstrap.class.getProtectionDomain().getCodeSource().getLocation();
            jar = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        new ServerApplication();
    }

    public static File getJarFile() {
        return jar;
    }

    public static File getParentFolder() {
        return getJarFile().getParentFile();
    }

}
