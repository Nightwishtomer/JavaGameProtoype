package com.diakonovtomer.projektObjektorientierung.input;

import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.graphics.Camera;
import javafx.scene.canvas.Canvas;
import java.util.function.BiConsumer;

/**
 * Handles mouse interactions on the game canvas and translates them into tile coordinates.
 * <p>
 * Supports both 2D and isometric rendering modes. Converts screen pixel coordinates
 * into tile (grid) positions and passes them to the provided callback functions.
 * </p>
 *
 * <ul>
 *     <li>{@code onHover}: called on mouse move over a tile</li>
 *     <li>{@code onClick}: called on mouse click on a tile</li>
 * </ul>
 *
 * @author adiakonov
 */
public class MouseHandler {
    
    /** Canvas the user interacts with */
    private final Canvas canvas;
    
    /** Camera providing current screen offsets */
    private final Camera camera;
    
    /** Width of a single tile in pixels */
    private final double tileW;
    
    /** Height of a single tile in pixels */
    private final double tileH;
    
    /** Whether the game is in isometric ("3D") mode */
    private final boolean isoMode;

    /** Callback for hover (mouse move) events */
    private final BiConsumer<Integer, Integer> hoverCallback;
        
    /** Callback for click events */
    private final BiConsumer<Integer, Integer> clickCallback;

    /**
     * Constructs a new MouseHandler for a canvas.
     *
     * @param canvas         the JavaFX canvas receiving mouse input
     * @param camera         the active camera controlling screen offsets
     * @param onHover        callback to run when the mouse moves over a tile (tileY, tileX)
     * @param onClick        callback to run when a tile is clicked (tileY, tileX)
     */
    public MouseHandler(Canvas canvas,
                        Camera camera,
                        BiConsumer<Integer, Integer> onHover,
                        BiConsumer<Integer, Integer> onClick)
    {
        this.canvas        = canvas;
        this.camera        = camera;
        this.tileW         = Constant.TILE_W;
        this.tileH         = Constant.TILE_H;
        this.isoMode       = (Config.get("RENDER_MODE").equals("3D")) ? true : false;
        this.hoverCallback = onHover;
        this.clickCallback = onClick;
        hook(); // Attach listeners
    }

    /**
     * Attaches mouse event listeners to the canvas.
     * <ul>
     *     <li>{@code setOnMouseMoved}: for hover detection</li>
     *     <li>{@code setOnMouseClicked}: for click actions</li>
     * </ul>
     */
    private void hook() {
        canvas.setOnMouseMoved(e -> handle(e.getX(), e.getY(), hoverCallback));
        canvas.setOnMouseClicked(e -> handle(e.getX(), e.getY(), clickCallback));
    }

    /**
     * Converts screen coordinates to tile grid coordinates
     * and calls the corresponding callback.
     *
     * @param mx  mouse X position (pixels)
     * @param my  mouse Y position (pixels)
     * @param cb  callback to invoke with (tileY, tileX)
     */
    private void handle(double mx, double my, BiConsumer<Integer, Integer> cb) {
        int[] tile = MousePicker.pickTile(
            mx, my,
            camera.getOffsetX(),
            camera.getOffsetY(),
            tileW, tileH,
            isoMode);
        cb.accept(tile[0], tile[1]);
    }
    
    /**
     * Detaches mouse event handlers from the canvas.
     * <p>
     * Should be called when the scene is destroyed or changed,
     * to prevent memory leaks from lingering listeners.
     * </p>
     */
    public void dispose() {
        canvas.setOnMouseMoved(null);
        canvas.setOnMouseClicked(null);
    }
}
