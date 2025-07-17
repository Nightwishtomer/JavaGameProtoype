package com.diakonovtomer.projektObjektorientierung.game;

import com.diakonovtomer.projektObjektorientierung.network.ApiService;
import javafx.application.Platform;

/**
 * Manages loading game state from the backend based on a save ID.
 * <p>
 * The {@code LoadManager} stores temporary state used to initialize a game session,
 * such as the map layout, player position, and last updated timestamp.
 * </p>
 *
 * <p>
 * Loading is performed asynchronously using {@link ApiService#loadDataById(int)},
 * and results are applied on the JavaFX thread using {@link Platform#runLater(Runnable)}.
 * </p>
 *
 * <p>This class is static and should not be instantiated.</p>
 *
 * @author Artiem
 */
public class LoadManager {
    
    /** Unique ID for the loaded game save (non-zero if loading). */
    private static int loadID = 0;
    
    /** The map level number. */
    private static int map_level = 0;
    
    /** ASCII representation of the map layout. */
    private static String ascii_map = "";
    
    /** Last updated timestamp string. */
    private static String updated = "";
    
    /** Starting X tile position of the player. */
    private static int positionTileX = 0;
    
    /** Starting Y tile position of the player. */
    private static int positionTileY = 0;

    /**
     * Sets the ID of the save to load.
     *
     * @param id the save ID
     */
    public static void setLoadId(int id){ loadID = id; }

    /**
     * Gets the current save ID.
     *
     * @return the load ID
     */
    public static int getLoadId(){ return loadID; }

    /**
     * Resets the load ID to 0, indicating no active load.
     */
    public static void resetLoadId(){ loadID = 0; }

    /**
     * Checks whether loading is active.
     *
     * @return {@code true} if a save is currently loaded (ID != 0)
     */
    public static boolean is_load(){ return (loadID != 0); }
    
    /**
     * Sets the level number of the loaded map.
     *
     * @param value the map level
     */
    public static void setMapLevel(int value){ map_level = value; }
    
    /**
     * Sets the ASCII map representation.
     *
     * @param value the ASCII map string
     */
    public static void setAsciiMap(String value){ ascii_map = value; }
    
    /**
     * Sets the last updated timestamp string.
     *
     * @param value the timestamp
     */
    public static void setUpdated(String value){ updated = value; }
    
    /**
     * Sets the starting tile X coordinate.
     *
     * @param value the X coordinate
     */
    public static void setPositionTileX(int value){ positionTileX = value; }
    
    /**
     * Sets the starting tile Y coordinate.
     *
     * @param value the Y coordinate
     */
    public static void setPositionTileY(int value){ positionTileY = value; }
        
    /**
     * Returns the current map level.
     *
     * @return the map level
     */
    public static int getMapLevel(){ return map_level; }
    
    /**
     * Returns the ASCII map string.
     *
     * @return the ASCII map
     */
    public static String getAsciiMap(){ return ascii_map; }
    
    /**
     * Returns the last updated timestamp.
     *
     * @return update time string
     */
    public static String getUpdated(){ return updated; }
    
    /**
     * Returns the X tile coordinate where the player should spawn.
     *
     * @return player X tile
     */
    public static int getPositionTileX(){ return positionTileX; }
    
    /**
     * Returns the Y tile coordinate where the player should spawn.
     *
     * @return player Y tile
     */
    public static int getPositionTileY(){ return positionTileY; }
    
    /**
     * Initiates asynchronous loading of save data from the backend
     * using the current {@link #getLoadId()} value.
     * <p>
     * On success, updates internal fields with the loaded data
     * and applies them on the JavaFX application thread.
     * </p>
     */
    public static void loadDataLevel(){
         ApiService.loadDataById(loadID)
            .thenAccept(save -> Platform.runLater(() -> {
                setMapLevel(save.map_level);
                setAsciiMap(save.ascii_map);
                setUpdated(save.updated);
                setPositionTileX(save.positionTileX);
                setPositionTileY(save.positionTileY);
          }))
            .exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
    }
}   
