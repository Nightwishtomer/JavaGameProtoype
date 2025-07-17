package com.diakonovtomer.projektObjektorientierung.game;

import javafx.animation.AnimationTimer;

/**
 * Controls the game loop using JavaFX's {@link AnimationTimer}.
 * <p>
 * Calls the {@code update()} method of {@link GameEngine} once per frame,
 * allowing smooth rendering and logic updates tied to the screen refresh rate.
 * </p>
 *
 * @author Artiem
 */
public class GameLoop extends AnimationTimer { 
    
    /** The game engine instance to be updated every frame. */
    private final GameEngine engine;

    /**
     * Constructs a new {@code GameLoop} for the given {@link GameEngine}.
     *
     * @param engine the game engine to update each frame
     */
    public GameLoop(GameEngine engine) { this.engine = engine; }

    /**
     * Called by JavaFX on each frame.
     *
     * @param now the timestamp of the current frame in nanoseconds
     */
    @Override
    public void handle(long now) { engine.update(System.nanoTime()); }

    /**
     * Starts the game loop.
     * This is just a convenience method for {@link #start()}.
     */
    public void startLoop() { this.start(); }
}
