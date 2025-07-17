package com.diakonovtomer.projektObjektorientierung.model.map;

import com.diakonovtomer.projektObjektorientierung.Constant;
import javafx.scene.paint.Color;

/**
 * Tile represents a single cell on the map grid.
 * Each tile holds data about its type, position, and properties
 * such as passability and visual color.
 * 
 * Tile does not store any logic by default (like traps or interactivity),
 * but can be extended with behavior via `action()` or subclasses in the future.
 * 
 * @author adiakonov
 */
public class Tile {
    
    /** Type of tile (e.g., FLOOR, WALL) */
    private final TileType type;
    
    /** Color for rendering (derived from type) */
    private final Color color;
    
    /** Whether player can walk through this tile */
    private final boolean isPassable;
    
    /** Y position on the tile grid */
    private final int tileY;
    
    /** X position on the tile grid */
    private final int tileX;

    /** Pixel Y position on screen (derived from tileY) */
    private final double posY;
    
    /** Pixel X position on screen (derived from tileX) */
    private final double posX;
    
    /**
     * Constructor for creating a tile from a TileType and grid position.
     *
     * @param type the type of tile (see TileType enum)
     * @param tileY the Y coordinate in tile grid
     * @param tileX the X coordinate in tile grid
     */
    public Tile(TileType type, int tileY, int tileX) {
        this.type = type; 
        this.color = type.getColor(); 
        this.isPassable = type.isPassable();        
        this.tileY = tileY;
        this.tileX = tileX;
        this.posY = tileY * Constant.TILE_H;
        this.posX = tileX * Constant.TILE_W;  
    }
    
    // === GETTERS ===

    /**
     * @return ASCII symbol for this tile (used in export or debug)
     */    
    public char getSymbol(){ return type.getSymbol(); }
    
    
    /**
     * @return Tile type (WALL, FLOOR, etc.)
     */
    public TileType getType(){ return type; }
    
    /**
     * @return Height of the tile in pixels
     */
    public double getHeight(){ return Constant.TILE_H; }
    
    /**
     * @return Width of the tile in pixels
     */
    public double getWidth(){ return Constant.TILE_W; }
              
    /**
     * @return true if the tile can be walked on
     */
    public boolean isPassable(){ return isPassable; }
    
    /**
     * Placeholder for tile-specific action logic.
     * Can be overridden in future to allow traps, triggers, or interactive behavior.
     */
    public void action(){
        // Implement logic for interactive tile actions here if needed
    }
}
