package me.kumo.res;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FontLoader {
    // Map to store fonts by style name (e.g., "Regular", "Bold", "Italic")
    public static final Map<String, Font> fontMap = new HashMap<>();

    public static void loadFonts(String fontDir, String fontName) {
        try {
            fontMap.put("Regular", registerFont(fontDir + fontName + "-Regular.ttf"));
            fontMap.put("Bold", registerFont(fontDir + fontName + "-Bold.ttf"));
            fontMap.put("Italic", registerFont(fontDir + fontName + "-Italic.ttf"));
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private static Font registerFont(String fontPath) throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(FontLoader.class.getResourceAsStream(fontPath)));
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
        return font;
    }
}