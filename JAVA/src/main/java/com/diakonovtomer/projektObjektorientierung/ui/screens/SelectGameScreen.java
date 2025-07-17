package com.diakonovtomer.projektObjektorientierung.ui.screens;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.network.ApiService;
import com.diakonovtomer.projektObjektorientierung.util.AuthContext;
import com.diakonovtomer.projektObjektorientierung.util.CursorSpritesheetManager;
import com.diakonovtomer.projektObjektorientierung.util.FontManager;
import com.diakonovtomer.projektObjektorientierung.util.NavigationManager;
import com.diakonovtomer.projektObjektorientierung.util.MenuItemMaker;
import javafx.application.Platform;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.geometry.Pos;

/**
 * The {@code SelectGameScreen} class represents a user interface screen where
 * players can choose to start a new game or load an existing saved game.
 * <p>
 * This screen is typically shown after the user logs in. It retrieves the list
 * of saved games from the server and displays them as clickable menu items.
 * Players can start a new game (if authorized) or return to the previous screen.
 * </p>
 * <p>Features include:</p>
 * <ul>
 * <li>Button to start a new game (only if user is authenticated)</li>
 * <li>Dynamic list of saved games loaded from the backend</li>
 * <li>Back button to return to the previous screen</li>
 * <li>Custom font, cursor, and stylized UI consistent with the Diablo-like theme</li>
 * </ul>
 @author adiakonov
*/
public class SelectGameScreen {

    /**
     * The navigation manager to handle scene transitions.
     */
    private final NavigationManager navigationManager;
    
    /**
     * Scene representing the entire screen layout.
     */
    private final Scene scene;
    
    /**
     * Manager for loading and setting custom cursor from sprite sheet.
     */
    private final CursorSpritesheetManager cursorManager = new CursorSpritesheetManager();

    /**
     * Constructs a new {@code SelectGameScreen}.
     * <p>
     * Initializes UI components including a title, a "New Game" button,
     * a list of saved games fetched from the server, and a "Back" button.
     * </p>
     * @param navigationManager the navigation manager used to switch screens
     */
    public SelectGameScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        Font menuFont = FontManager.getDiabloBigFont(Constant.MENU_BUTTON_ITEMS_FONT_SIZE); // Подключаем кастомный шрифт из ресурсов
        BorderPane root = new BorderPane();
        root.setStyle(Constant.MENU_BACKGROUND_STYLE);
        
        Text title = new Text(Constant.SCREENS_SELECTGAME_TYTLE);
        title.setFont(FontManager.getDiabloBigFont(Constant.MENU_BUTTON_TITLE_FONT_SIZE));
        title.setFill(Color.LIGHTGRAY);
        
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new javafx.geometry.Insets(Constant.BOX_PADDING)); // отступ от края
        root.setTop(topBox);
        
        // "New Game" button
        Text txtNewGame = MenuItemMaker.create(
            Constant.MENU_BUTTON_ITEMS_TEXT_START,
            FontManager.getDiabloBigFont(Constant.MENU_BUTTON_TITLE_FONT_SIZE),
            () -> onNewGame()
        );

        if (!AuthContext.hasToken()) {
            txtNewGame.setFill(Color.DARKGRAY);
            txtNewGame.setOnMouseClicked(e -> showError("First, log in to your account"));
            txtNewGame.setOnMouseEntered(e -> {});   // убираем hover‑эффект
        }
        VBox savesBox = new VBox(10);
        savesBox.setAlignment(Pos.CENTER);
        ApiService.loadList()
            .thenAccept(respArray -> {
                Platform.runLater(() -> {
                    for (ApiService.LoadResp save : respArray) {
                        Text line = MenuItemMaker.create(
                            "L" + save.map_level + " - " + save.updated,
                            FontManager.getDiabloBigFont(Constant.MENU_BUTTON_TITLE_FONT_SIZE),
                            () -> loadGame(save));
                        savesBox.getChildren().add(line);                   
                    }
                });
            })
            .exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
        txtNewGame = MenuItemMaker.create(
        Constant.MENU_BUTTON_ITEMS_TEXT_START,
        FontManager.getDiabloBigFont(Constant.MENU_BUTTON_TITLE_FONT_SIZE),
        () -> navigationManager.push(
            new GameScreen(navigationManager, null, -1, -1).getScene()
        ));   // ← null
                 
        VBox menu = new VBox(30, txtNewGame, savesBox);
        menu.setAlignment(Pos.CENTER);
        menu.setStyle(Constant.MENU_BACKGROUND_STYLE); // например, фон
        root.setCenter(menu);        
        
        // "Back" button
        Text txtBack = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_BACK, menuFont, () -> onBackToMenu());
        VBox bottomBox = new VBox(txtBack);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new javafx.geometry.Insets(Constant.BOX_PADDING)); // отступ от края
        root.setBottom(bottomBox);
        
        scene = new Scene(root, Constant.APP_SETTINGS_WIDTH, Constant.APP_SETTINGS_HEIGHT);
        scene.setCursor(cursorManager.getCursor(Integer.parseInt(Config.get("TYPE_CURSOR"))));
    }
    
    /**
     * Navigates back to the previous screen.
     */
    private void onBackToMenu(){ navigationManager.pop(); }
    
    /**
     * Starts a new game with a fresh level and default player position.
     */
    private void onNewGame() { 
        GameScreen gameScreen = new GameScreen(navigationManager);
        navigationManager.push(gameScreen.getScene());
    }

    /**
     * Returns the JavaFX {@link Scene} for this screen.
     *
     * @return the JavaFX Scene
     */
    public Scene getScene() { return scene; }
    
    /**
     * Displays an error popup dialog with the given message.
     *
     * @param msg the error message to show
     */
    private void showError(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Authorization failed");
            alert.setContentText(msg);
            alert.initOwner(scene.getWindow());   // LoginScreen
            alert.showAndWait();
        });
    }    
    
    /**
     * Loads a saved game from the server using its ID, then launches it.
     *
     * @param shortSave metadata of the save to load
     */
    private void loadGame(ApiService.LoadResp shortSave) {
        ApiService.loadDataById(shortSave.id)
            .thenAccept(full ->
                Platform.runLater(() -> {
                    navigationManager.push(
                        new GameScreen(
                            navigationManager,
                            full.ascii_map,
                            full.positionTileY,
                            full.positionTileX 
                        ).getScene()
                    );
                })                                
            ) .exceptionally(ex -> { ex.printStackTrace(); showError(ex.getMessage()); return null; });
    }
}