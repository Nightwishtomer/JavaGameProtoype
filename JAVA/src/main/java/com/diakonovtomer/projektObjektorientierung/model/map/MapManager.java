package com.diakonovtomer.projektObjektorientierung.model.map;

import com.diakonovtomer.projektObjektorientierung.Constant;

/**
 * Manages the internal map state for the game, including converting raw map data
 * (from ASCII or external generator) into usable {@link Tile} objects, tracking
 * player spawn point, and checking map bounds.
 * <p>
 * Provides abstraction between raw tile data ({@code TileType[][]}) and structured
 * tile objects with coordinates ({@code Tile[][]}).
 * </p>
 *
 * Supports two construction modes:
 * <ul>
 *   <li>From ASCII-encoded map string (character layout)</li>
 *   <li>From pre-parsed {@code TileType[][]} array</li>
 * </ul>
 *
 * @author adiakonov
 */
public class MapManager {
   
    private TileType[][] sourceMap; // Raw map loaded from source
    private Tile[][] newMap; // Converted Tile objects
    private int startPlayerPositionCellY = 0;
    private int startPlayerPositionCellX = 0;
    private final int width;
    private final int height;
    
    /**
     * Constructs map manager from an ASCII string.
     * The string must be of size height * width.
     *
     * @param ascii full ASCII string representing the map
     */
    public MapManager(String ascii, int positionTileY, int positionTileX) {
        this.width  = Constant.MAP_WIDTH;
        this.height = Constant.MAP_HEIGHT;

        this.sourceMap = new TileType[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = ascii.charAt(y * width + x);
                sourceMap[y][x] = TileType.fromChar(c);
            }
        }
        startPlayerPositionCellY = positionTileY;
        startPlayerPositionCellX = positionTileX;
        this.setTiles();  // Convert TileType[][] to Tile[][]
    }

    /**
     * Constructs map manager from a raw 2D TileType array.
     * Also finds and replaces the PLAYER tile with FLOOR,
     * saving the original PLAYER position.
     *
     * @param rawMap 2D array of TileType representing the map
     */
    public MapManager(TileType[][] rawMap) {
        this.width  = rawMap[0].length;
        this.height = rawMap.length;
        this.sourceMap = rawMap;
        this.setStartPlayerPositionCell(); // Find PLAYER tile
        this.setTiles(); // Convert TileType[][] to Tile[][]
    }
    
    // === GETTERS ===
    
    public int getWidth(){ return width; }
    
    public int getHeight(){ return height; }
    
    public TileType[][] getSourceMap() { return sourceMap; }
    
    public TileType getSourceMapCell(int y, int x) { return sourceMap[y][x]; }
    
    public Tile[][] getMap() { return newMap; }
    
    public int getStartPlayerPositionCellY(){ return startPlayerPositionCellY; }
    
    public int getStartPlayerPositionCellX(){ return startPlayerPositionCellX; }
    
    // === SETTERS ===
    public void setStartPlayerPositionCellY(int value){ startPlayerPositionCellY = value; }
    
    public void setStartPlayerPositionCellX(int value){ startPlayerPositionCellX = value; }
    
    // === METHODS ===

    /**
     * Searches for a tile marked as PLAYER in the map.
     * If found, stores its position as the start point and replaces it with FLOOR.
     */
    private void setStartPlayerPositionCell(){
        System.out.println("*setStartPlayerPositionCell  " + TileType.PLAYER.toString() );
        for (int y = 0; y < sourceMap.length; y++) {
            for (int x = 0; x < sourceMap[y].length; x++) {
                if (sourceMap[y][x] == TileType.PLAYER) {
                    startPlayerPositionCellY = y;
                    System.out.println("set" + y + " " + x);
                    startPlayerPositionCellX = x;
                    sourceMap[y][x] = TileType.FLOOR;
                }
            }
        }
    }
    
    /**
     * Converts the raw TileType 2D array into a 2D array of Tile objects.
     */
    private void setTiles(){
        this.newMap = new Tile[height][width];
        for (int y = 0; y < sourceMap.length; y++) {
            for (int x = 0; x < sourceMap[y].length; x++) {
                newMap[y][x] = new Tile(sourceMap[y][x], y, x);
            }
        }    
    }  
    
    /**
     * Checks if a given tile coordinate is within the bounds of the map.
     *
     * @param tileY tile row index
     * @param tileX tile column index
     * @return true if the coordinates are valid
     */
    public boolean checkMapBoundaries(int teilY, int teilX){
        return (teilY >= 0 && teilY < this.getHeight() && teilX >= 0 && teilX < this.getWidth());
    }
    
    /**
     * Converts the current Tile[][] map to an ASCII string.
     * Useful for debugging or exporting the map state.
     *
     * @return ASCII string representing the map
     */
    public String getAsciiMap(){
        String result = "";
        for (int y = 0; y < newMap.length; y++) {
            for (int x = 0; x < newMap[y].length; x++) {
                result += newMap[y][x].getSymbol();
            }
        }
        return result;
    }
}
