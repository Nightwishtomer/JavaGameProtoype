package com.diakonovtomer.projektObjektorientierung.graphics;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.model.map.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Central class for managing the active {@link Renderer} implementation (2D or Isometric).
 * <p>
 * This class delegates rendering tasks to either {@link CartRenderer} or {@link IsoRenderer},
 * depending on the value of the {@code RENDER_MODE} property in {@code config.properties}.
 * </p>
 * 
 * Responsibilities include:
 * <ul>
 *     <li>Clearing the screen each frame</li>
 *     <li>Rendering the map tiles</li>
 *     <li>Highlighting a specific tile (e.g. hover)</li>
 *     <li>Rendering the player</li>
 * </ul>
 * 
 * @author adiakonov
 */
public class RendererManager {

    /** The selected renderer based on RENDER_MODE ("2D" or "3D") */
    private final Renderer renderer;
    
    /** JavaFX graphics context to draw on */
    private final GraphicsContext gc;
    
    /** JavaFX canvas that holds the graphics context */
    private Canvas canvas;

    /**
     * Constructs a new RendererManager, selecting the rendering mode (2D or 3D)
     * at runtime based on configuration.
     *
     * @param gc      the graphics context used for drawing
     * @param canvas  the canvas component
     * @param camera  the camera providing screen offsets
     * @param tiles   the tile map to be rendered
     */
    public RendererManager(GraphicsContext gc, Canvas canvas, Camera camera, Tile[][] tiles) {
        this.gc = gc;
        this.canvas = canvas;
        this.renderer =  (Config.get("RENDER_MODE").equals("3D")) ? new IsoRenderer(gc, canvas, camera, tiles) : new CartRenderer(gc, canvas, camera, tiles);
    }

    /**
     * Renders the full tile map.
     */
    public void renderMap() { renderer.renderMap(); }

    /**
     * Highlights a specific tile (hover effect).
     *
     * @param tileY the tile's Y coordinate (row)
     * @param tileX the tile's X coordinate (column)
     */
    public void renderHighlightTile(int tileY, int tileX) { renderer.renderHighlightTile(tileY, tileX); }

    /**
     * Updates the renderer's internal offsets from the camera.
     * Useful after window resize or camera movement.
     */
    public void setOffsets() { renderer.setOffsets(); }

    /**
     * Renders the player at the current position.
     */
    public void renderPlayer() { renderer.renderPlayer(); }
    
    /**
     * Clears the canvas and fills it with a black background.
     * Called at the beginning of each frame.
     */
    public void cleaningScreen() { // Очистика экрана
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
