package com.diakonovtomer.projektObjektorientierung.util;

import com.diakonovtomer.projektObjektorientierung.Constant;
import javafx.scene.text.Font;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for loading and caching fonts used throughout the application.
 * <p>
 * Fonts are loaded from the application's resources (typically from the `/fonts/` directory)
 * and cached in memory to avoid redundant loading and performance overhead.
 * </p>
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * Font titleFont = FontManager.getDiabloBigFont(48);
 * Font bodyFont = FontManager.getFont("/fonts/myfont.ttf", 24);
 * }</pre>
 *
 * <h3>Fallback:</h3>
 * If a font fails to load from resources, a default system font
 * (defined in {@link Constant#FONTS_DEFAULT_FONT}) is returned instead.
 *
 * <h3>Thread Safety:</h3>
 * This class is not thread-safe. If used in a multi-threaded context, synchronization must be added externally.
 * 
 * @author adiakonov
 */
public class FontManager {
    private static final Map<String, Font> fonts = new HashMap<>();

    /**
     * Loads a font from the given resource path with the specified size.
     * <p>
     * If the font has already been loaded previously with the same size,
     * the cached version is returned instead.
     * </p>
     *
     * @param fontFileName the path to the font file relative to the resource root
     *                     (e.g., {@code "/fonts/diablo_h.ttf"})
     * @param size         the desired font size
     * @return the loaded {@link Font} object, or a fallback system font if the resource is missing
     */
    public static Font getFont(String fontFileName, double size) {
        String key = fontFileName + "_" + size;
        if (fonts.containsKey(key)) {
            return fonts.get(key);
        }

        Font font = Font.loadFont(FontManager.class.getResourceAsStream(fontFileName), size);
        if (font == null) {
            System.err.println("Failed to load font: " + fontFileName);
            font = Font.font(Constant.FONTS_DEFAULT_FONT, size); // запасной шрифт
        }
        fonts.put(key, font);
        return font;
    }

    /**
     * Loads the custom "Diablo Big Font" from resources using the configured path.
     *
     * @param size the desired font size
     * @return the loaded font, or a fallback system font if unavailable
     */
    public static Font getDiabloBigFont(double size) {
        return getFont(Constant.FONTS_DIABLO_BIG_FONT_PATH, size);
    }

    /**
     * Loads the custom "Diablo Small Font" from resources using the configured path.
     *
     * @param size the desired font size
     * @return the loaded font, or a fallback system font if unavailable
     */
    public static Font getDiabloSmallFont(double size) {
        return getFont(Constant.FONTS_DIABLO_SMALL_FONT_PATH, size);
    }
}
