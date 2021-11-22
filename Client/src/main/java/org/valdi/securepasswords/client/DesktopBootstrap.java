package org.valdi.securepasswords.client;

import javafx.application.Application;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class DesktopBootstrap {
    private static File jar;

    public static void main(String[] args) {
        try {
            URL url = DesktopBootstrap.class.getProtectionDomain().getCodeSource().getLocation();
            jar = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Application.launch(DesktopApplication.class, args);
    }

    public static File getJarFile() {
        return jar;
    }

    public static File getParentFolder() {
        return getJarFile().getParentFile();
    }

}
