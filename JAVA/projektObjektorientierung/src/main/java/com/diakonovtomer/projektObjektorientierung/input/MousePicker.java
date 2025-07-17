package com.diakonovtomer.projektObjektorientierung.input;


/**
 * Utility class for converting screen (mouse) coordinates into tile coordinates.
 * <p>
 * Supports both 2D cartesian and isometric projection modes.
 * </p>
 * 
 * <p>
 * Example usage: used by {@code MouseHandler} to detect which tile the mouse is pointing at.
 * </p>
 *
 * <pre>{@code
 * int[] tile = MousePicker.pickTile(
 *     mouseX, mouseY,
 *     camera.getOffsetX(), camera.getOffsetY(),
 *     Constant.TILE_W, Constant.TILE_H,
 *     true  // isoMode
 * );
 * }</pre>
 *
 * @author adiakonov
 */
public final class MousePicker {

    /** Prevent instantiation of static utility class */
    private MousePicker() {}

    /**
     * Converts screen (mouse) coordinates into tile coordinates.
     *
     * @param screenX   Mouse X coordinate in screen space (e.g., from Canvas)
     * @param screenY   Mouse Y coordinate in screen space
     * @param offsetX   Horizontal camera offset (from {@code camera.getOffsetX()})
     * @param offsetY   Vertical camera offset (from {@code camera.getOffsetY()})
     * @param tileW     Width of one tile (in pixels)
     * @param tileH     Height of one tile (in pixels)
     * @param isoMode   {@code true} if isometric mode is enabled, {@code false} for top-down 2D
     * @return          An array {@code [tileY, tileX]} indicating the tile at that position
     */
    public static int[] pickTile(
            double screenX, double screenY,
            double offsetX, double offsetY,
            double tileW,   double tileH,
            boolean isoMode)
    {
        // Adjust for camera scroll offset
        double localX = screenX - offsetX;
        double localY = screenY - offsetY;

        if (isoMode) {
            /* 
             * For isometric mode, invert the isometric projection formulas:
             *
             * Original projection:
             *     screenX = (tileX - tileY) * tileW / 2
             *     screenY = (tileX + tileY) * tileH / 4
             *
             * Inverted:
             *     tileX = ((localX / (tileW/2)) + (localY / (tileH/4))) / 2
             *     tileY = ((localY / (tileH/4)) - (localX / (tileW/2))) / 2
             */
            double a = localX / (tileW / 2.0);
            double b = localY / (tileH / 4.0);
            int tx = (int)Math.floor(( a + b ) / 2.0);
            int ty = (int)Math.floor(( b - a ) / 2.0);
            return new int[]{ty, tx};
        } else {
            // Standard 2D cartesian grid
            int tx = (int)Math.floor(localX / tileW);
            int ty = (int)Math.floor(localY / tileH);
            return new int[]{ty, tx};
        }
    }
}
