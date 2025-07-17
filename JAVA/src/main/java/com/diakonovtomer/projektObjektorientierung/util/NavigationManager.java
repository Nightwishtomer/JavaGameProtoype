package com.diakonovtomer.projektObjektorientierung.util;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Utility class to manage navigation between JavaFX scenes using a stack-based approach.
 * <p>
 * It allows for simple push/pop scene transitions, mimicking navigation like in mobile apps or games.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li>Push a new scene and store the current one</li>
 *   <li>Pop to return to the previous scene</li>
 *   <li>Clear the navigation stack and start fresh</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * NavigationManager nav = new NavigationManager(primaryStage);
 * nav.push(mainMenuScene);           // Push new scene
 * nav.pop();                         // Go back to previous scene
 * nav.clearAndPush(startScene);      // Clear stack and set new root
 * }</pre>
 *
 * @author adiakonov
 */
public class NavigationManager {
    private final Stage stage;
    private final Deque<Scene> history = new ArrayDeque<>();

    /**
     * Creates a new NavigationManager for the given stage.
     *
     * @param stage the JavaFX stage to control
     */
    public NavigationManager(Stage stage) {
        this.stage = stage;
    }

    /**
     * Pushes a new scene onto the stack and displays it.
     * The current scene is saved and can be returned to using {@link #pop()}.
     *
     * @param scene the new scene to display
     */
    public void push(Scene scene) {
        if (stage.getScene() != null) history.push(stage.getScene());
        stage.setScene(scene);
    }

    /**
     * Pops the previous scene from the stack and displays it.
     * If the stack is empty, nothing happens.
     */
    public void pop() {
        if (!history.isEmpty()) {
            stage.setScene(history.pop());
        }
    }

    /**
     * Clears the entire scene stack and sets the given scene as the current one.
     *
     * @param scene the scene to set after clearing the history
     */
    public void clearAndPush(Scene scene) {
        history.clear();
        stage.setScene(scene);
    }
}