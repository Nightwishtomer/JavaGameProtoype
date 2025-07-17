package com.diakonovtomer.projektObjektorientierung.graphics;

import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.model.map.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Implementation of {@link Renderer} for drawing a tile-based map (cartesian 2D view).
 * <p>
 * Responsible for rendering the tile map grid, highlighting tiles,
 * and rendering the player at the center of the viewport.
 * </p>
 * <p>
 * Uses the {@link Camera} instance to get the current offset for scrolling and centering.
 * </p>
 * <p>
 * Tiles are rendered as colored rectangles based on their {@code TileType}.
 * The player is rendered as a simple red rectangle.
 * </p>
 * 
 * @author Artiem
 */
public class CartRenderer implements Renderer {

    /** Tile width in pixels. */
    private static final double TILE_W = Constant.TILE_W;
    
    /** Tile height in pixels. */
    private static final double TILE_H = Constant.TILE_H;

    /** Graphics context for drawing on the canvas. */
    private final GraphicsContext gc;
    
    /** 2D array of tiles representing the map. */
    private final Tile[][] tiles;

    /** Canvas on which rendering is performed. */
    private Canvas canvas;
    
    /** Camera used for calculating view offsets. */
    private Camera camera;
    
    /** Current horizontal offset for rendering, based on camera. */
    private double offsetX;
    
    /** Current vertical offset for rendering, based on camera. */
    private double offsetY;

    /**
     * Constructs a CartRenderer.
     * 
     * @param gc the GraphicsContext to draw on
     * @param canvas the Canvas associated with rendering
     * @param camera the Camera controlling the viewport offset
     * @param tiles the 2D array of tiles to render
     */
    public CartRenderer(GraphicsContext gc, Canvas canvas, Camera camera, Tile[][] tiles) {
        this.gc = gc;
        this.canvas = canvas;
        this.camera = camera;
        this.tiles = tiles;
        
        this.offsetX = camera.getOffsetX();
        this.offsetY = camera.getOffsetY();
    }
        
    /**
     * Renders the entire tile map using colored rectangles.
     * The fill color corresponds to the tile's {@code TileType}.
     */
    @Override
    public void renderMap() {
        this.setOffsets();
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile t = tiles[y][x];
                gc.setFill(t.getType().getColor());
                gc.fillRect((x * TILE_H) + offsetX, (y * TILE_W) + offsetY, TILE_H, TILE_W);
            }
        }
    }
        
    /**
     * Updates the local offset fields from the current camera offsets.
     * Should be called before rendering to synchronize offsets.
     */
    @Override
    public void setOffsets(){
        this.offsetY = camera.getOffsetY();
        this.offsetX = camera.getOffsetX();
    }
   
    /**
     * Renders a highlighted rectangle around a specific tile.
     * The highlight color and line width are defined in {@link Constant}.
     * 
     * @param tileY the tile's Y coordinate to highlight
     * @param tileX the tile's X coordinate to highlight
     */
    @Override
    public void renderHighlightTile(int tileY, int tileX) {
        if (((tileY >= 0 && tileX >= 0) ) && (tileY < (tiles.length - 1) && tileX < (tiles[0].length - 1))) {
            gc.setStroke(Constant.HIGHLIGHT_TILE_COLOR);
            gc.setLineWidth(2);    
            gc.setLineWidth(2);
            gc.strokeRect(
                tileX * Constant.TILE_W + offsetX,
                tileY * Constant.TILE_H + offsetY,
                Constant.TILE_W,
                Constant.TILE_H
            );
        }
    }
    
    /**
     * Renders the player as a red rectangle at the center of the viewport.
     * The size of the rectangle is determined by player constants.
     */
    @Override
    public void renderPlayer(){
        double centerY = camera.getCenterY();
        double centerX = camera.getCenterX();
        gc.setFill(Color.RED);
        gc.fillRect(centerX - Constant.PLAYER_SIZE_W / 2, centerY - Constant.PLAYER_SIZE_H / 2, Constant.PLAYER_SIZE_W, Constant.PLAYER_SIZE_H);
    }
}