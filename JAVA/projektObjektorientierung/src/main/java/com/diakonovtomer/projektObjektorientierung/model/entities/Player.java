package com.diakonovtomer.projektObjektorientierung.model.entities;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.game.PathManager;
import com.diakonovtomer.projektObjektorientierung.model.map.MapManager;
import javafx.scene.canvas.GraphicsContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;        

/**
 * Represents the player entity within the game.
 * <p>
 * The {@code Player} maintains its position in both tile and pixel coordinates,
 * handles smooth movement along a path, and keeps track of its current direction.
 * Movement is animated based on delta time and target positions.
 * </p>
 *
 * <h3>Main responsibilities:</h3>
 * <ul>
 *   <li>Track tile and pixel position</li>
 *   <li>Manage movement using pathfinding via {@code PathManager}</li>
 *   <li>Update current direction based on movement vector</li>
 *   <li>Integrate with game camera and rendering system</li>
 * </ul>
 *
 * Example usage:
 * <pre>{@code
 * Player player = new Player(gc, mapManager);
 * player.goTo(10, 5); // pathfinding to target tile
 * player.update(deltaTime); // smooth movement
 * }</pre>
 * 
 * @author adiakonov
 */
public class Player {
    
    /** Graphics context used for rendering */
    private GraphicsContext gc; 
    
    /** Player's current tile position (Y axis) */
    private int positionTileY;
    
    /** Player's current tile position (X axis) */
    private int positionTileX;
    
    /** Player's current pixel position (Y axis) */
    private double positionY;
    
    /** Player's current pixel position (X axis) */
    private double positionX;
    
    /** Player sprite height in pixels */
    private final double sizeY = Constant.PLAYER_SIZE_H;
       
    /** Player sprite width in pixels */
    private final double sizeX = Constant.PLAYER_SIZE_W;
    
    /** Movement speed in pixels per second */
    private double playerSpeed;
    
    /** Path to follow, stored as tile coordinates (Y, X) */
    private Queue<int[]> path = new LinkedList<>();
    
    /** Target pixel Y position for smooth movement */
    private double targetPosY;
    
    /** Target pixel X position for smooth movement */
    private double targetPosX;
    
    /** Indicates whether player is currently moving */
    private boolean moving = false;
    
    /** Reference to the map manager */
    private MapManager mapManager;
    
    /** Pathfinding manager for movement logic */
    private PathManager pathManager;
    
    /** Current movement direction of the player */
    private Direction direction = Direction.S;
    
    /**
     * Constructs a new {@code Player} instance at the map's start position.
     *
     * @param gc          graphics context for drawing (not used here directly)
     * @param mapManager  map context including tile layout and spawn position
     */
    public Player(GraphicsContext gc, MapManager mapManager){
        this.gc = gc;
        this.mapManager = mapManager;
        this.positionTileY = mapManager.getStartPlayerPositionCellY();
        this.positionTileX = mapManager.getStartPlayerPositionCellX();
        this.positionY = this.positionTileY * Constant.TILE_H;
        this.positionX = this.positionTileX * Constant.TILE_W;
        this.playerSpeed = Integer.parseInt(Config.get("PLAYER_SPEED"));
        this.pathManager = new PathManager(mapManager);
    }
    
    // SETTER
    public void setPositionTileY(int value){
        this.positionTileY = value;
        this.positionY = value * Constant.TILE_H;
    }
    
    public void setPositionTileX(int value){
        this.positionTileX = value;
        this.positionX = value * Constant.TILE_W;
    }
    
    //GETTER
    public int getPositionTeilY(){
        return positionTileY;
    }
    
    public int getPositionTeilX(){
        return positionTileX;
    }    
    
    public double getPositionY(){
        return positionY;
    }
    
    public double getPositionX(){
        return positionX;
    }
    
    /* -------------------- Logic -------------------- */

    /**
     * Initiates pathfinding and sets movement toward the target tile.
     *
     * @param tileY target tile Y
     * @param tileX target tile X
     */
    public void goTo(int tileY, int tileX) {
        List<int[]> newPath = pathManager.findPath(getPositionTeilY(), getPositionTeilX(), tileY, tileX);
        if (!newPath.isEmpty()) {
            newPath.remove(0); // Skip current tile
            path.clear();
            if (newPath != null && !newPath.isEmpty()) {
                path.addAll(newPath);
                // Let's set the first goal (first step)
                int[] first = path.poll();
                if (first != null) {
                    targetPosY = first[0] * Constant.TILE_H;
                    targetPosX = first[1] * Constant.TILE_W;
                    moving = true;
                }
            }
        }
    }

    /**
     * Updates player's position and path progress based on elapsed time.
     *
     * @param deltaTime time since last update, in seconds
     */
    public void update(double deltaTime) {
        if (!moving) return;

        double vectorDX = targetPosX - positionX;
        double vectorDY = targetPosY - positionY;

        double dist = Math.sqrt(vectorDX * vectorDX + vectorDY * vectorDY);
        
        if (dist < playerSpeed * deltaTime) {
            // Reached the goal, set the position exactly on the tile
            positionX = targetPosX;
            positionY = targetPosY;

            // Updating logical coordinates
            int nextX = (int) Math.round(positionX / Constant.TILE_W);
            int nextY = (int) Math.round(positionY / Constant.TILE_H);
            
            this.setDirection(nextY, nextX); // setting direction
            
            this.setPositionTileX(nextX);
            this.setPositionTileY(nextY);

            // We move on to the next waypoint, if there is one.
            if (!path.isEmpty()) {
                int[] next = path.poll();
                targetPosY = next[0] * Constant.TILE_H;
                targetPosX = next[1] * Constant.TILE_W;
            } else {
                moving = false; // The path has been passed
            }
        } else {
            // We move towards the goal at a constant speed
            positionX += vectorDX / dist * playerSpeed * deltaTime;
            positionY += vectorDY / dist * playerSpeed * deltaTime;
        }
    }
    
    
    /**
     * Updates the player's direction based on tile movement delta.
     *
     * @param targetY destination tile Y
     * @param targetX destination tile X
     */
    public void setDirection(int targetY, int targetX) {
        Direction newDir = Direction.fromDelta(
                targetX - positionTileX,
                targetY - positionTileY);

        if (newDir != Direction.NONE && newDir != direction) {
            direction = newDir;
        }
    }
   
    
}
