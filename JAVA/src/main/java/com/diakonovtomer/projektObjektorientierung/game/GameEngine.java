package com.diakonovtomer.projektObjektorientierung.game;

import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.model.map.MapGenerator;
import com.diakonovtomer.projektObjektorientierung.model.map.MapManager;
import com.diakonovtomer.projektObjektorientierung.model.entities.Player;
import com.diakonovtomer.projektObjektorientierung.graphics.Camera;
import com.diakonovtomer.projektObjektorientierung.graphics.RendererManager;
import com.diakonovtomer.projektObjektorientierung.input.MouseHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import java.util.Map;

/**
 * Core game engine that handles rendering, player updates, and input events.
 * <p>
 * Responsible for controlling the main game loop, maintaining camera position,
 * and rendering the tile map, player, and UI highlights.
 * </p>
 *
 * <p>The engine supports both starting from a saved ASCII map or generating
 * a new random map.</p>
 *
 * @author Artiem
 */
public class GameEngine {
    
    /** The main canvas for rendering. */
    private final Canvas canvas;
    
    /** 2D graphics context used to draw on the canvas. */
    private final GraphicsContext gc;
    
    /** The player entity. */
    private final Player player;
    
    /** Camera that follows the player and handles screen offset. */
    private final Camera camera;
    
    /** Manager for the tile map and player start position. */
    private final MapManager mapManager;
    
    /** Handles rendering of map, player, and UI elements. */
    private RendererManager rendererManager;
    
    /** Handles mouse interactions such as hover and clicks. */
    private final MouseHandler mouseHandler;
    
    /** Highlighted tile coordinates. */
    private int highlightTileY = -1;
    private int highlightTileX = -1;

    /** Last time the update was called (nanoseconds). */
    private long lastUpdateTime = 0; // поле для отслеживания времени:
    
    /**
     * Initializes the game engine with either a new or loaded map.
     *
     * @param canvas the JavaFX canvas to draw on
     * @param asciiMap ASCII map representation (optional, can be null to generate new)
     * @param positionTileY the Y tile position for the player (if map is loaded)
     * @param positionTileX the X tile position for the player (if map is loaded)
     */
    public GameEngine(Canvas сanvas, String asciiMap, int positionTileY, int positionTileX) {
        this.canvas = сanvas; // Canvas
        this.gc = canvas.getGraphicsContext2D(); // Graphics Context 2D
        
        System.out.println(positionTileY + " - " + positionTileX);
        
        // if the card arrived, it means the game is loaded
        if (asciiMap != null && !asciiMap.isEmpty()) {
            this.mapManager = new MapManager(asciiMap, positionTileY, positionTileX);
            //this.mapManager.setStartPlayerPositionCellY(positionTileY);
           // this.mapManager.setStartPlayerPositionCellX(positionTileX);
        } else { // generate a new one
             System.out.println("generate a new one");
            MapGenerator gen = new MapGenerator(Constant.MAP_WIDTH, Constant.MAP_HEIGHT);
            this.mapManager = new MapManager(gen.generate());
        }
System.out.println(mapManager.getStartPlayerPositionCellX() + " - " + mapManager.getStartPlayerPositionCellY());
        
        this.player = new Player(gc, mapManager);
        this.camera = new Camera(canvas, mapManager.getWidth(), mapManager.getHeight(), player);
        recalcRenderers();
        this.mouseHandler = new MouseHandler(canvas, camera, this::onTileHover, this::onTileClick);
    }
    
    /**
     * Calls {@link #update(long)} using the current system time.
     */
    public void update() {
        update(System.nanoTime());
    }

    /**
     * Updates game logic, player state, and renders the scene.
     *
     * @param now the current time in nanoseconds
     */
    public void update(long now) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = now;
            return;
        }
        double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
        lastUpdateTime = now;
        player.update(deltaTime); // update the player
        camera.update();
        this.rendererManager.cleaningScreen(); // Clearing the screen
        this.rendererManager.renderMap();
        this.rendererManager.renderPlayer();
        this.rendererManager.renderHighlightTile(highlightTileY, highlightTileX); //--- Default Highlight
    }               
    
    /**
     * Recalculates renderers to center the camera on the player.
     * Useful after resizing or map changes.
     */
    private void recalcRenderers() {
        camera.update();
        this.rendererManager = new RendererManager(gc, canvas, camera, mapManager.getMap());
    }
    
    /**
     * Updates the camera and renderer after window resize.
     *
     * @param valueHeight the new height
     * @param valueWidth the new width
     */
    public void onResize(double valueHeight, double valueWidth){
        camera.setResize(valueHeight, valueWidth);       
        recalcRenderers();                 // recalculation of offsets
    }
    
    /**
     * Called when the mouse hovers over a tile.
     *
     * @param ty tile Y position
     * @param tx tile X position
     */
    private void onTileHover(int ty, int tx) {
        highlightTileY = ty;
        highlightTileX = tx;
    }

    /**
     * Called when a tile is clicked.
     * Instructs the player to move to the clicked tile.
     *
     * @param ty tile Y position
     * @param tx tile X position
     */
    private void onTileClick(int ty, int tx) {
        player.goTo(ty, tx); // Player movement with pathfinding
    }
 
    /**
     * Returns the current hero (player) position as a key-value map.
     *
     * @return map containing player X/Y positions in both pixel and tile coordinates
     */
    public Map<String, Object> getHeroPosition() {
        return Map.of(
                "positionX", this.player.getPositionX(),
                "positionY", this.player.getPositionY(),
                "positionTileX", this.player.getPositionTeilX(),
                "positionTileY", this.player.getPositionTeilY()
        );
    }

    /**
     * Returns the hero's current stats such as health and mana.
     * Currently hardcoded to 0.
     *
     * @return map containing hero stats
     */
    public Map<String, Object> getHeroStats() {
        return Map.of(
                "health", 0,
                "mana", 0
        );
    }

    /**
     * Returns the current level number.
     * Currently hardcoded to 1.
     *
     * @return current level number
     */
    public int getCurrentLevel() { return 1; }

    /**
     * Returns the ASCII representation of the current map.
     *
     * @return ASCII map string
     */
    public String getAsciiMapRepresentation() {
        return mapManager.getAsciiMap();
    }
}