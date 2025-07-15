package com.diakonovtomer.projektObjektorientierung.model.entities;

/**
 * Enumeration representing 8-directional movement plus "NONE" (no direction).
 * <p>
 * Each direction has:
 * <ul>
 *   <li>{@code code} — numeric identifier (can be used in switch or maps)</li>
 *   <li>{@code text} — human-readable abbreviation (e.g., "N", "SW")</li>
 *   <li>{@code dx} — delta X offset (e.g., -1 for west)</li>
 *   <li>{@code dy} — delta Y offset (e.g., 1 for south)</li>
 * </ul>
 * This enum is commonly used in tile-based movement systems (e.g., RPG or tactical games).
 * </p>
 *
 * <pre>{@code
 * Direction dir = Direction.fromDelta(1, -1); // NE
 * int dx = dir.dx;  // +1
 * int dy = dir.dy;  // -1
 * }</pre>
 * 
 * @author adiakonov
 */
public enum Direction {
    
    /** No direction (used as a default or idle state) */
    NONE(0, "NONE",  0,  0),
    
    /** North */
    N   (1, "N",     0, -1),
    
    /** North-East */
    NE  (2, "NE",    1, -1),
    
    /** East */
    E   (3, "E",     1,  0),
    
    /** South-East */
    SE  (4, "SE",    1,  1),
    
    /** South */
    S   (5, "S",     0,  1),
    
    /** South-West */
    SW  (6, "SW",   -1,  1),
    
    /** West */
    W   (7, "W",    -1,  0),
    
    /** North-West */
    NW  (8, "NW",   -1, -1);

    /** Short label (e.g., "N", "SE") */
    public final String text;

    /** Direction code, usually from 0 to 8 */
    public final int code;

    /** Delta X movement: -1, 0, or 1 */
    public final int dx;

    /** Delta Y movement: -1, 0, or 1 */
    public final int dy;
    
    
    Direction(int code, String text, int dx, int dy) {
        this.code = code;
        this.text = text;
        this.dx   = dx;
        this.dy   = dy;
    }

    /**
     * Returns a {@code Direction} enum based on given delta offsets.
     * Automatically normalizes the values using {@code Integer.signum()}.
     *
     * @param dx horizontal delta (can be any integer)
     * @param dy vertical delta (can be any integer)
     * @return   matching {@code Direction} or {@code NONE} if not found
     */
    static Direction fromDelta(int dx, int dy) {
        int sx = Integer.signum(dx);
        int sy = Integer.signum(dy);
        for (Direction d : values())
            if (d.dx == sx && d.dy == sy)
                return d;
        return NONE;
    }
}
