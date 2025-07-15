package com.diakonovtomer.projektObjektorientierung.ui.screens;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.game.GameEngine;
import com.diakonovtomer.projektObjektorientierung.game.GameLoop;
import com.diakonovtomer.projektObjektorientierung.game.SaveGame;
import com.diakonovtomer.projektObjektorientierung.network.ApiService;
import com.diakonovtomer.projektObjektorientierung.util.CursorSpritesheetManager;
import com.diakonovtomer.projektObjektorientierung.util.FontManager;
import com.diakonovtomer.projektObjektorientierung.util.NavigationManager;

import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.canvas.Canvas;
import javafx.geometry.Pos;

import static javafx.scene.input.KeyCode.ESCAPE;

/**
 * GameScreen represents the main in-game screen where
 * the player controls the character and interacts with the map.
 * 
 * It supports:
 * - Loading from saved state (map, hero position)
 * - Handling resize events
 * - Handling pause via ESC key
 * - Saving game progress
 */
public class GameScreen {
    private final NavigationManager navigationManager;
    private final Scene scene;
    private final CursorSpritesheetManager cursorManager = new CursorSpritesheetManager();
    private final GameLoop gameLoop; 
    private final GameEngine engine;
    
    /**
     * Creates a new GameScreen with default map and hero position.
     * 
     * @param navigationManager Global navigation manager
     */
    public GameScreen(NavigationManager navigationManager){
        this(navigationManager, null, -1, -1);
    }
    
    /**
     * Creates a new GameScreen with a specific ASCII map and hero start position.
     * 
     * @param navigationManager Global navigation manager
     * @param asciiMap ASCII map string
     * @param positionTileY Y coordinate (in tiles) of the hero
     * @param positionTileX X coordinate (in tiles) of the hero
     */
    public GameScreen(NavigationManager navigationManager, String asciiMap, int positionTileY, int positionTileX) {
        this.navigationManager = navigationManager;
        BorderPane root = new BorderPane();
        root.setStyle(Constant.MENU_BACKGROUND_STYLE);
        
        /* ---- Title ---- */
        Text title = new Text(Constant.SCREENS_GAMESCREEN_TYTLE);
        title.setFont(FontManager.getDiabloBigFont(Constant.MENU_BUTTON_TITLE_FONT_SIZE));
        title.setFill(Color.LIGHTGRAY);
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new javafx.geometry.Insets(Constant.BOX_PADDING)); // отступ от края
        
        /* ---- Canvas and Engine ---- */
        Canvas canvas = new Canvas(Constant.APP_SETTINGS_WIDTH, Constant.APP_SETTINGS_HEIGHT); // Задай нужный размер
        this.engine = new GameEngine(canvas, asciiMap, positionTileY, positionTileX);
        this.gameLoop = new GameLoop(engine);
        this.gameLoop.startLoop();
        root.setCenter(canvas);
        
        /* ---- Scene ---- */
        scene = new Scene(root, Constant.APP_SETTINGS_WIDTH, Constant.APP_SETTINGS_HEIGHT);
        
        // Resize listeners width
        scene.widthProperty().addListener((obs, oldW, newW) -> {
            this.engine.onResize(canvas.getHeight(), Math.max(newW.doubleValue(), Constant.APP_SETTINGS_WIDTH));
        });

        // Resize listeners height
        scene.heightProperty().addListener((obs, oldH, newH) -> {
            this.engine.onResize(Math.max(newH.doubleValue(), Constant.APP_SETTINGS_HEIGHT), canvas.getWidth());
        });
        
        // ESC key handling to open pause menu
        scene.setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case ESCAPE -> openPause();
            }
        });
        
        // Custom cursor
        scene.setCursor(cursorManager.getCursor(Integer.parseInt(Config.get("TYPE_CURSOR"))));
    }
    
    /**
     * Forces the scene to gain focus for input handling.
     * 
     * @param scene The JavaFX Scene to give focus to
     */
    public static void giveFocus(Scene scene) {
        Platform.runLater(() -> 
            Optional.ofNullable(scene.getRoot())
                    .ifPresent(n -> { n.setFocusTraversable(true);
                                      n.requestFocus(); })
        );
    }

    /**
     * Opens the pause screen with options to resume, save, or return to menu.
     */
    private void openPause() {
        gameLoop.stop(); // pause the game
        PauseScreen pause = new PauseScreen(
            navigationManager,
            /* onResume */
            () -> {
                navigationManager.pop(); // remove PauseScreen from the stack
                gameLoop.startLoop();    // continue the game
            },
            /* saveLavel */
            () -> {
                gameLoop.stop();
                SaveGame saveGame = createSaveGame();
                String json = new com.google.gson.Gson().toJson(saveGame);
                ApiService.save(saveGame)
                    .exceptionally(ex -> { ex.printStackTrace(); return null; });
                navigationManager.pop(); // remove PauseScreen from the stack
                gameLoop.startLoop();    // continue the game
            },
            /* onExitToMenu */
            () -> {
                gameLoop.stop();
                navigationManager.push(new SelectGameScreen(navigationManager).getScene());
            },
            /* onExitToMenu */
            () -> {
                gameLoop.stop();
                navigationManager.clearAndPush( new MenuScreen(navigationManager).getScene() );
            }
        );
        navigationManager.push(pause.getScene());
    }

    /**
     * Returns the scene associated with this screen.
     * 
     * @return JavaFX Scene object
     */
    public Scene getScene() { return scene; }
    
    /**
     * Creates a save game object containing current game state.
     * 
     * @return SaveGame object
     */
    private SaveGame createSaveGame() {
        return new SaveGame(
            engine.getCurrentLevel(),
            engine.getAsciiMapRepresentation(),
            engine.getHeroStats(),
            engine.getHeroPosition()
        );
    }
}
