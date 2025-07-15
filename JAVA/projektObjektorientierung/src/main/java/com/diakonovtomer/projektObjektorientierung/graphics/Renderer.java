package com.diakonovtomer.projektObjektorientierung.graphics;

/**
 * A unified interface for different map renderers (e.g., 2D top-down, isometric).
 * <p>
 * Implementations must handle rendering of the game map, highlighting tiles, rendering the player,
 * and syncing camera-based offsets.
 * </p>
 * 
 * @author adiakonov
 */
public interface Renderer {
    
    /**
     * Renders the entire tile map using the current rendering style.
     * Called once per frame by the game engine.
     */
    void renderMap();
    
    /**
     * Draws a visual highlight (e.g. outline or effect) on a specific tile.
     *
     * @param tileY the Y (row) index of the tile to highlight
     * @param tileX the X (column) index of the tile to highlight
     */
    void renderHighlightTile(int tileY, int tileX);
    
    /**
     * Updates internal render offsets (e.g. after a camera move or resize).
     * Typically pulled from the {@code Camera} object.
     */
    void setOffsets();
    
    /**
     * Renders the player character using the current coordinate system.
     * Should draw the player at the center of the screen based on camera.
     */
    void renderPlayer();
}
