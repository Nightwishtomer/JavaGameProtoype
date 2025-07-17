package com.diakonovtomer.projektObjektorientierung.graphics;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.model.entities.Player;
import javafx.scene.canvas.Canvas;

/**
 * Camera manages the viewport offset based on the player's position and canvas size.
 * <p>
 * Supports both classic 2D rendering and pseudo-3D isometric view depending on configuration.
 * It calculates offsetX and offsetY so that the player is centered on the screen.
 * </p>
 * <p>
 * The camera tracks the player and updates offsets accordingly,
 * also handles resizing of the canvas and recalculating centers.
 * </p>
 * 
 * <p>This class is immutable except for offsets and center coordinates which are updated dynamically.</p>
 * 
 * @author Artiem
 */
public final class Camera {
    
    /** Current vertical offset of the viewport in pixels. */
    private double offsetY;

    /** Current horizontal offset of the viewport in pixels. */
    private double offsetX;

    /** Width of the level in tiles. */
    private final int levelTileWidth;

    /** Height of the level in tiles. */
    private final int levelTileHeight;

    /** Width of the level in pixels. */
    private final double levelPxWidth;

    /** Height of the level in pixels. */
    private final double levelPxHeight;

    /** Center position X coordinate of the viewport (usually half canvas width). */
    private double centerX;

    /** Center position Y coordinate of the viewport (usually half canvas height). */
    private double centerY;

    /** Reference to the player object to track its position. */
    private final Player player;

    /** Canvas associated with this camera for size and rendering context. */
    private final Canvas canvas;
            
    /**
     * Constructs a camera instance that follows the player within the level bounds.
     *
     * @param canvas the canvas representing the drawing surface
     * @param levelTileWidth the width of the level in tiles
     * @param levelTileHeight the height of the level in tiles
     * @param player the player object to track
     */
    public Camera(Canvas canvas, int levelTileWidth, int levelTileHeight, Player player){
        this.canvas = canvas;
        this.levelTileWidth = levelTileWidth;
        this.levelTileHeight = levelTileHeight;
        this.player = player;
        this.levelPxWidth = levelTileWidth * Constant.TILE_W;
        this.levelPxHeight = levelTileHeight * Constant.TILE_H;  
        this.setCenterX();
        this.setCenterY(); 
    }
    
    /* ───────── SETTERS ───────────────────────────────────────────── */

    /**
     * Recalculates and sets the vertical center coordinate of the viewport,
     * typically half the canvas height.
     */
    public void setCenterY() { centerY = canvas.getHeight() / 2; }
        
    /**
     * Recalculates and sets the horizontal center coordinate of the viewport,
     * typically half the canvas width.
     */
    public void setCenterX() { centerX = canvas.getWidth() / 2; }
    
    /**
     * Resizes the canvas and updates the center coordinates accordingly.
     *
     * @param valueHeight new height of the canvas
     * @param valueWidth new width of the canvas
     */
    public void setResize(double valueHeight, double valueWidth) {
        canvas.setWidth(valueWidth); 
        canvas.setHeight(valueHeight);
        setCenterY();
        setCenterX();
    }
    
    /* ───────── GET ─────────────────────────────────────────────── */
    
    /**
     * Returns the vertical center coordinate of the viewport.
     *
     * @return center Y coordinate in pixels
     */
    public double getCenterY() { return centerY; }
        
    /**
     * Returns the horizontal center coordinate of the viewport.
     *
     * @return center X coordinate in pixels
     */
    public double getCenterX() { return centerX; }
    
    /**
     * Returns the current vertical offset applied to rendering.
     *
     * @return vertical offset in pixels
     */
    public double getOffsetY(){ return offsetY; }
    
    /**
     * Returns the current horizontal offset applied to rendering.
     *
     * @return horizontal offset in pixels
     */
    public double getOffsetX(){ return offsetX; }
    
    
    /* ───────── METHOD ─────────────────────────────────────────────── */
   
    /**
     * Updates the camera offsets based on the player's position
     * and the current rendering mode (2D or pseudo-3D).
     * <p>
     * In 2D mode, centers the player by subtracting player's pixel position.
     * In pseudo-3D mode, applies isometric transformation for offsets.
     * </p>
     */
    public void update(){       
        if (Config.get("RENDER_MODE").equals("3D")) {
            // Pseudo-3D mode
            double heroTileX = player.getPositionX() / Constant.TILE_W;
            double heroTileY = player.getPositionY() / Constant.TILE_H;
            offsetX = centerX - ((heroTileX - heroTileY) * Constant.TILE_W / 2.0) ;
            offsetY = centerY - ((heroTileX + heroTileY) * Constant.TILE_H / 4.0);
        } else {
            // Classic 2D mode
            offsetX = centerX - (player.getPositionX() + Constant.TILE_W / 2.0);
            offsetY = centerY - (player.getPositionY() + Constant.TILE_H / 2.0);
        }
    }
}
