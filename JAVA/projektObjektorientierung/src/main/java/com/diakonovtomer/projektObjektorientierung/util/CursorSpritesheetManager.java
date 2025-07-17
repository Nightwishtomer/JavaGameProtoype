package com.diakonovtomer.projektObjektorientierung.util;

import com.diakonovtomer.projektObjektorientierung.Constant;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code CursorSpritesheetManager} class is responsible for loading custom mouse cursors
 * from a single spritesheet image and a metadata file. It allows dynamic cursor switching
 * at runtime by using IDs.
 * <p>
 * The manager reads a `.png` spritesheet and a corresponding `.txt` data file
 * (see {@link Constant#RESOURCES_CURSORS_SPRITESHEET_PATH} and {@link Constant#RESOURCES_CURSORS_DATA_PATH})
 * where each line describes the position, size, and hotspot of a cursor in the spritesheet.
 * </p>
 *
 * <h3>Data Format:</h3>
 * The cursor data file must follow this format:
 * <pre>
 * # Cursor ID: x, y, width, height, hotspotX, hotspotY
 * 0: 0,0,32,32,0,0
 * 1: 32,0,32,32,16,16
 * </pre>
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * CursorSpritesheetManager manager = new CursorSpritesheetManager();
 * ImageCursor customCursor = manager.getCursor(1);
 * scene.setCursor(customCursor);
 * }</pre>
 *
 * <h3>Typical Use Cases:</h3>
 * <ul>
 *   <li>Change the cursor based on game mode or UI context</li>
 *   <li>Highlight hoverable elements with a special cursor</li>
 *   <li>Implement pixel-perfect legacy game UX</li>
 * </ul>
 *
 * @author adiakonov
 */
public class CursorSpritesheetManager {  
    
    private final Map<Integer, ImageCursor> cachedCursors = new HashMap<>();

    /**
     * Constructs a {@code CursorSpritesheetManager} and loads all defined cursors from the spritesheet and metadata.
     * 
     * @throws RuntimeException if the spritesheet or data file is missing, malformed, or unreadable
     */
    public CursorSpritesheetManager() {
        Image spritesheet = new Image(getClass().getResourceAsStream(Constant.RESOURCES_CURSORS_SPRITESHEET_PATH));
        PixelReader reader = spritesheet.getPixelReader();
        if (reader == null) {
            throw new RuntimeException("Failed to get PixelReader from " + Constant.RESOURCES_CURSORS_SPRITESHEET_PATH);
        }

        Map<Integer, int[]> cursorData = loadCursorData();
        for (Map.Entry<Integer, int[]> entry : cursorData.entrySet()) {
            int id = entry.getKey();
            int[] coords = entry.getValue();

            int x = coords[0];
            int y = coords[1];
            int width = coords[2];
            int height = coords[3];
            int hotspotX = coords[4];
            int hotspotY = coords[5];

            WritableImage cursorImage = new WritableImage(reader, x, y, width, height);
            cachedCursors.put(id, new ImageCursor(cursorImage, hotspotX, hotspotY));
        }
    }

    
    /**
     * Parses the cursor metadata file and extracts coordinates and hotspot definitions for each cursor.
     *
     * @return a map of cursor ID to an array of six integers: [x, y, width, height, hotspotX, hotspotY]
     * @throws RuntimeException if the file format is invalid or cannot be read
     */
    private Map<Integer, int[]> loadCursorData() {
        Map<Integer, int[]> data = new HashMap<>();

        try (InputStream stream = getClass().getResourceAsStream(Constant.RESOURCES_CURSORS_DATA_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(":");
                if (parts.length != 2) continue;

                int id = Integer.parseInt(parts[0].trim());

                String[] values = parts[1].split(",");
                if (values.length != 6) continue;

                int[] coords = new int[6];
                for (int i = 0; i < 6; i++) {
                    coords[i] = Integer.parseInt(values[i].trim());
                }

                data.put(id, coords);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while reading" + Constant.RESOURCES_CURSORS_DATA_PATH + ": " + e.getMessage(), e);
        }

        return data;
    }

    /**
     * Returns the {@link ImageCursor} associated with the given ID.
     *
     * @param id the ID of the cursor as defined in the metadata file
     * @return the {@link ImageCursor} instance
     * @throws IllegalArgumentException if the ID is not defined
     */
    public ImageCursor getCursor(int id) {
        ImageCursor cursor = cachedCursors.get(id);
        if (cursor == null) {
            throw new IllegalArgumentException("Cursor with id \" + id + \" not found");
        }
        return cursor;
    }

    /**
     * Applies the custom cursor with the given ID to the specified {@link Node}.
     *
     * @param node the UI element to apply the cursor to
     * @param id   the cursor ID
     */
    public void setCursor(Node node, int id) {
        node.setCursor(getCursor(id));
    }

    /**
     * Restores the default system cursor for the given {@link Node}.
     *
     * @param node the UI element to reset the cursor on
     */
    public static void setDefaultCursor(Node node) {
        node.setCursor(Cursor.DEFAULT);
    }
}
