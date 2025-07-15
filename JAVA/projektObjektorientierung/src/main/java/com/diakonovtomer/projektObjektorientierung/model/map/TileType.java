package com.diakonovtomer.projektObjektorientierung.model.map;

import javafx.scene.paint.Color;

/**
 * Enum representing all possible types of map tiles.
 * Each type is defined by:
 *  - a symbol (used in ASCII maps and JSON),
 *  - passability (whether the player can walk through it),
 *  - and a color (used for simple rendering).
 * 
 * Extend this if more tile types are needed.
 * 
 * @author adiakonov
 */
public enum TileType {
    IMPASS    ('0', false, Color.BLACK        ), // Unwalkable (default fallback)
    FLOOR     ('.', true,  Color.DARKSLATEGRAY), // Walkable floor tile
    WALL      ('#', false, Color.SADDLEBROWN  ), // Solid wall
    GRATE     ('"', false, Color.TRANSPARENT  ), // Metal grate (impassable)
    DOOR      ('*', true,  Color.ORANGE       ), // Door (walkable)
    DOORGRATE ('~', true,  Color.TRANSPARENT  ), // Door with grate
    ARCH      ('-', true,  Color.TRANSPARENT  ), // Arch (walkable, visual only)
    ARCHGRATE ('+', false, Color.TRANSPARENT  ), // Arch with grate (impassable)
    CORNERITL ('A', false, Color.BLUE         ), // Inner corner top-left
    CORNERITR ('B', false, Color.RED          ), // Inner corner top-right
    CORNERIBL ('C', false, Color.YELLOW       ), // Inner corner bottom-left
    CORNERIBR ('D', false, Color.GREEN        ), // Inner corner bottom-right
    CORNEROTL ('E', false, Color.BLUE         ), // Outer corner top-left
    CORNEROTR ('F', false, Color.RED          ), // Outer corner top-right
    CORNEROBL ('G', false, Color.YELLOW       ), // Outer corner bottom-left
    CORNEROBR ('H', false, Color.GREEN        ), // Outer corner bottom-right
    ENTRANCE  ('S', true,  Color.BISQUE       ), // Entry point for player
    PLAYER    ('@', true,  Color.RED          ), // Initial player tile (converted to FLOOR)
    EXIT      ('X', true,  Color.BLACK        ); // Exit tile
    
    /** Character used in ASCII representation */
    private final char symbol;
    
    /** Whether tile is walkable */
    private final boolean passable;
    
    /** Color used for rendering */
    private final Color color;
    
    TileType(char symbol, boolean passable, Color color) {
        this.symbol = symbol;
        this.passable = passable;
        this.color = color;
    }

    public char getSymbol()       { return symbol; }
    public boolean isPassable()   { return passable; }
    public Color getColor()       { return color; }
    
    /**
     * Converts a character from ASCII map into a TileType enum.
     * If character is unknown, returns IMPASS as fallback.
     */
    public static TileType fromChar(char c) {
        for (TileType t : values()) {
            if (t.getSymbol() == c) return t;
        }
        return IMPASS;
    }
}