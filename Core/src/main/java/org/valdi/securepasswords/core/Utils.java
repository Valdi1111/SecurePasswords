package org.valdi.securepasswords.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static String readString(InputStream in) throws UncheckedIOException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            for (int n; 0 < (n = in.read(buf)); ) {
                out.write(buf, 0, n);
            }
            out.close();
            return out.toString(StandardCharsets.UTF_8); // Or whatever encoding
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeString(OutputStream out, String body) throws UncheckedIOException {
        try {
            OutputStreamWriter sw = new OutputStreamWriter(out);
            sw.write(body); // Write result to stream.
            sw.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Map<String, String> getParametersMap(String query) {
        Map<String, String> map = new HashMap<>();
        if(query == null || query.isBlank()) {
            return map;
        }

        String[] params = query.split("&");
        for (String param : params) {
            String[] split = param.split("=");
            String name = split[0];
            String value = split[1];
            map.put(name, value);
        }
        return map;
    }

}
