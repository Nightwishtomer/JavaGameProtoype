package com.diakonovtomer.projektObjektorientierung.ui.screens;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.util.CursorSpritesheetManager;
import com.diakonovtomer.projektObjektorientierung.util.FontManager;
import com.diakonovtomer.projektObjektorientierung.util.NavigationManager;
import com.diakonovtomer.projektObjektorientierung.util.MenuItemMaker;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

/**
 * The {@code SettingsScreen} class represents the settings screen of the application.
 * <p>
 * This screen currently only includes a title and a "Back" button, serving
 * as a placeholder for future settings options (like volume, language, graphics, etc).
 * </p>
 * 
 * <p>Features:</p>
 * <ul>
 *   <li>Styled title using custom font</li>
 *   <li>"Back" button to return to the previous menu</li>
 *   <li>Custom cursor from a sprite sheet</li>
 * </ul>
 * 
 * @author adiakonov
 */
public class SettingsScreen {

    /** Manages scene transitions within the application. */
    private final NavigationManager navigationManager;
    
    /** The JavaFX scene representing this settings screen. */
    private final Scene scene;
    
    /** Loads and manages custom cursor sprites. */
    private final CursorSpritesheetManager cursorManager = new CursorSpritesheetManager();

    /**
     * Constructs a new {@code SettingsScreen}.
     *
     * @param navigationManager the manager used for screen navigation
     */
    public SettingsScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        
        // Load custom font from resources
        Font menuFont = FontManager.getDiabloBigFont(Constant.MENU_BUTTON_ITEMS_FONT_SIZE);
        
        // Main container for the layout
        BorderPane root = new BorderPane();
        root.setStyle(Constant.MENU_BACKGROUND_STYLE);
        
        // Title section
        Text title = new Text(Constant.SCREENS_SETTINGS_TYTLE);
        title.setFont(FontManager.getDiabloBigFont(Constant.MENU_BUTTON_TITLE_FONT_SIZE));
        title.setFill(Color.LIGHTGRAY);
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new javafx.geometry.Insets(Constant.BOX_PADDING)); // отступ от края
        root.setTop(topBox);
        
        // "Back" button
        Text txtBack = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_BACK, menuFont, () -> onBackToMenu());
        VBox bottomBox = new VBox(txtBack);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new javafx.geometry.Insets(Constant.BOX_PADDING)); // отступ от края
        root.setBottom(bottomBox);
        
        // Final scene setup
        scene = new Scene(root, Constant.APP_SETTINGS_WIDTH, Constant.APP_SETTINGS_HEIGHT);
        scene.setCursor(cursorManager.getCursor(Integer.parseInt(Config.get("TYPE_CURSOR"))));
    }
    
    /**
     * Handles the back button action, returning to the previous screen.
     */
    private void onBackToMenu(){ navigationManager.pop(); }

    
    /**
     * Returns the JavaFX scene object for this screen.
     *
     * @return the scene for display
     */
    public Scene getScene() { return scene; }
}