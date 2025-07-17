package com.diakonovtomer.projektObjektorientierung.game;

import java.util.Map;

/**
 * Represents a saved game state, including the current level,
 * ASCII map representation, and hero's state and position.
 * <p>
 * This class acts as a data container for persisting and restoring
 * the essential components of the game session.
 * </p>
 * 
 * @author Artiem
 */
public class SaveGame {
    
    /** Current level number. */
    private int level;
    
    /** ASCII representation of the map layout. */
    private String asciiMap;
    
    /** Map of hero's attributes and stats (e.g. health, mana). */
    private Map<String, Object> heroState;
    
    /** Map of hero's position data (e.g. tile coordinates, pixel position). */
    private Map<String, Object> heroPosition;

    /**
     * Constructs a new SaveGame instance.
     *
     * @param level the current level number
     * @param asciiMap the ASCII representation of the map
     * @param heroState a map containing hero state properties
     * @param heroPosition a map containing hero position details
     */
    public SaveGame(int level, String asciiMap, Map<String, Object> heroState, Map<String, Object> heroPosition) {
        this.level = level;
        this.heroState = heroState;
        this.heroPosition = heroPosition;
        this.asciiMap = asciiMap;
    }

    /**
     * Returns the saved level number.
     *
     * @return the level
     */
    public int getLevel() { return level; }

    /**
     * Returns the ASCII map string.
     *
     * @return the ASCII map
     */
    public String getAsciiMap() { return asciiMap; }

    /**
     * Returns the hero's saved state.
     *
     * @return the hero's state map
     */
    public Map<String, Object> getHeroState() { return heroState; }

    /**
     * Returns the hero's saved position.
     *
     * @return the hero's position map
     */
    public Map<String, Object> getHeroPosition() { return heroPosition; }
}
