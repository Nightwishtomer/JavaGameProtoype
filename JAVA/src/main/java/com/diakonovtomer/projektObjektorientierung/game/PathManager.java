package com.diakonovtomer.projektObjektorientierung.game;

import com.diakonovtomer.projektObjektorientierung.model.map.MapManager;

import java.util.List;
        
/**
 * Handles pathfinding logic using A* algorithm and stores the latest computed path.
 * <p>
 * Encapsulates interaction between the {@link MapManager} and the {@link AStar}
 * pathfinding algorithm. Supports both computing and printing paths.
 * </p>
 * 
 * <p>
 * The class assumes that A* operates on a tile-based grid and returns
 * a list of coordinate pairs representing the path.
 * </p>
 * 
 * @author Artiem
 */
public class PathManager {
    
    /** The currently stored path (as list of {y, x} coordinates). */
    private List<int[]> path;
    
    /** Reference to the map manager used to retrieve the tile grid. */
    private MapManager mapManager;
    
    
    private AStar aStar;
    
    /**
     * Constructs a {@code PathManager} with a specific map manager.
     *
     * @param mapManager the map manager containing the grid
     */
    public PathManager(MapManager mapManager){ this.mapManager = mapManager; }
    
    /**
     * Returns the currently stored path.
     *
     * @return the current path as a list of coordinate pairs
     */
    public List<int[]> getPath(){ return this.path; }
    
    /**
     * Manually sets a new path.
     *
     * @param newPath a list of coordinate pairs representing the path
     */
    public void setPath(List<int[]> newPath){ this.path = newPath; }
    
    /**
     * Computes a path from the given source to destination using A* algorithm.
     *
     * @param fromY starting tile Y coordinate
     * @param fromX starting tile X coordinate
     * @param toY target tile Y coordinate
     * @param toX target tile X coordinate
     * @return the computed path as a list of {y, x} pairs
     */
    public List<int[]> findPath(int fromY, int fromX, int toY, int toX){
        this.path = aStar.findPath(mapManager.getMap(), fromY, fromX, toY, toX, DirMode.OCTILE);
        return this.path;
    }
    
    /**
     * Prints the current path to the console in human-readable form.
     */
    public void printPath(){ aStar.printPath(this.path); }
    
    /**
     * Prints the current path as an overlay on the tile grid.
     * Walls are shown as '#', passable tiles as '.', and path tiles as '*'.
     */
    public void printPathOnGrid(){ aStar.printPathOnGrid(mapManager.getMap(), this.path); }
}
