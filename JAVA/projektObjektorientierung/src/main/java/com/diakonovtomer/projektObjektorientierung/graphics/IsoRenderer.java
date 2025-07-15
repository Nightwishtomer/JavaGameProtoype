package com.diakonovtomer.projektObjektorientierung.graphics;

import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.model.map.Tile;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Renderer for isometric tile maps.
 * <p>
 * Draws the tile map in a diamond-shaped layout using isometric projection.
 * Supports rendering of the player at the center and highlighting tiles under the cursor.
 * </p>
 * 
 * <h2>Tile Geometry:</h2>
 * Each tile is rendered as a rhombus (diamond), using 4 corner points.
 * Width and height are derived from the constants:
 * <ul>
 *     <li>{@code TILE_W} = full width of tile</li>
 *     <li>{@code TILE_H} = half height of tile (iso height = TILE_H / 2)</li>
 * </ul>
 * 
 * <h2>Player:</h2>
 * The player is rendered as a red diamond centered on the canvas.
 * 
 * @author adiakonov
 */
public class IsoRenderer implements Renderer {

    /** Width of a tile in pixels. */
    private static final double TILE_W = Constant.TILE_W;
    
    /** Height of a tile in isometric view (half of real height). */
    private static final double TILE_H = Constant.TILE_H / 2;

    /** 2D grid of tiles representing the map. */
    private final Tile[][] tiles;
    
    /** Graphics context to draw on. */
    private final GraphicsContext gc;
    
    /** Canvas reference for dimensions and context. */
    private Canvas canvas;
    
    /** Camera used for scrolling and centering. */
    private Camera camera;
    
    /** Calculated horizontal offset for rendering. */
    private  double offsetX;
    
    /** Calculated vertical offset for rendering. */
    private  double offsetY;

    /**
     * Constructs the isometric renderer.
     *
     * @param gc      GraphicsContext from the canvas
     * @param canvas  The Canvas to render on
     * @param camera  The Camera to control view offset
     * @param tiles   The 2D map of tiles to render
     */
    public IsoRenderer(GraphicsContext gc, Canvas canvas, Camera camera, Tile[][] tiles) {
        this.gc = gc;
        this.canvas = canvas;
        this.camera = camera;
        this.tiles = tiles;
        this.offsetX = camera.getOffsetX();
        this.offsetY = camera.getOffsetY();
    }

    /**
     * Updates offsets from camera.
     */
    @Override
    public void setOffsets(){
        this.offsetY = camera.getOffsetY();
        this.offsetX = camera.getOffsetX();
    }

    /**
     * Renders the entire map in isometric style.
     */
    @Override
    public void renderMap() {
        this.setOffsets();
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile t = tiles[y][x];

                Point2D iso = toIso(y, x);
                gc.setFill(t.getType().getColor());
                gc.fillPolygon(
                        new double[] { iso.getX(), iso.getX() + TILE_W / 2, iso.getX(), iso.getX() - TILE_W / 2 },
                        new double[] { iso.getY(), iso.getY() + TILE_H / 2, iso.getY() + TILE_H, iso.getY() + TILE_H / 2 },
                        4);
            }
        }
    }
    
    /**
     * Renders a highlight box around a given tile in isometric coordinates.
     *
     * @param tileY Tile row (Y index)
     * @param tileX Tile column (X index)
     */
    @Override
    public void renderHighlightTile(int tileY, int tileX) {
        if (((tileY >= 0 && tileX >= 0) ) && (tileY < (tiles.length - 1) && tileX < (tiles[0].length - 1))) {
            gc.setStroke(Constant.HIGHLIGHT_TILE_COLOR);
            gc.setLineWidth(2);  
            double tileWidth = Constant.TILE_W;
            double tileHeight = Constant.TILE_H ;
            double screenX = (tileX - tileY) * tileWidth / 2 + offsetX;
            double screenY = (tileX + tileY) * tileHeight / 4 + offsetY;
            double[] xPoints = {
                screenX,                 //u
                screenX + tileWidth / 2, //r
                screenX,                 //d
                screenX - tileWidth / 2  //l
            };
            double[] yPoints = {
                screenY,                  //u 
                screenY + tileHeight / 4, //r
                screenY + tileHeight / 2, //d
                screenY + tileHeight / 4  //l
            };
            gc.setLineWidth(2);
            gc.strokePolygon(xPoints, yPoints, 4);
        }
    }

    
    /**
     * Converts grid coordinates (row/col) to screen coordinates for isometric projection.
     *
     * @param y Tile Y index
     * @param x Tile X index
     * @return Screen position as {@code Point2D}
     */
    private Point2D toIso(int y, int x) {
        double screenX = (x - y) * TILE_W / 2 + offsetX;
        double screenY = (x + y) * TILE_H / 2 + offsetY;
        return new Point2D(screenX, screenY);
    }
    
    /**
     * Converts screen (pixel) coordinates to isometric tile grid coordinates.
     * Mostly useful for picking tiles with mouse clicks.
     *
     * @param screenX X position on screen
     * @param screenY Y position on screen
     * @return Tile coordinates as {@code Point2D} (fractional values)
     */
    private Point2D toGrid(double screenX, double screenY) {
        double dx = screenX - offsetX;
        double dy = screenY - offsetY;
        double x = dx / (TILE_W / 2) + dy / (TILE_H / 2);
        x /= 2;
        double y = dy / (TILE_H / 2) - dx / (TILE_W / 2);
        y /= 2;
        return new Point2D(x, y);
    }
    
    /**
     * Renders the player at the center of the canvas.
     * Player is drawn as a red diamond matching tile style.
     */
    @Override
    public void renderPlayer(){
        // ── isometric rhombus ──
        double centerY = camera.getCenterY();
        double centerX = camera.getCenterX();
        
        double halfW = Constant.PLAYER_SIZE_W * 0.5;
        double halfH = Constant.PLAYER_SIZE_H * 0.5;

        gc.beginPath();
        gc.setFill(Color.RED);
        gc.moveTo(centerX,         centerY  );           // u
        gc.lineTo(centerX + halfW, centerY + halfH/2.0); // r
        gc.lineTo(centerX,         centerY + halfH);     // d
        gc.lineTo(centerX - halfW, centerY + halfH/2.0); // l
        gc.closePath();
        gc.fill();
    }
}