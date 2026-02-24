package com.example.kafkaswing.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TemplateLoader {
    public static String loadResourceAsString(String resourcePath) {
        try (InputStream in = TemplateLoader.class.getResourceAsStream(resourcePath)) {
            if (in == null) return null;
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
