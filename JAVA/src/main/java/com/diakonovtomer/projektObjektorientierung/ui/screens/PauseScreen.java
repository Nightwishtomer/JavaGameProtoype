package com.diakonovtomer.projektObjektorientierung.ui.screens;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.util.CursorSpritesheetManager;
import com.diakonovtomer.projektObjektorientierung.util.FontManager;
import com.diakonovtomer.projektObjektorientierung.util.NavigationManager;
import com.diakonovtomer.projektObjektorientierung.util.MenuItemMaker;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

/**
 * Pause screen UI presenting options to the player during game pause.
 * 
 * Contains buttons to:
 * - Resume the game
 * - Save current level
 * - Return to previous menu (likely the game selection or main menu)
 * - Exit the application entirely
 *
 * All buttons trigger passed-in Runnable callbacks for flexibility.
 */
public class PauseScreen {

    private final Scene scene;
    private final CursorSpritesheetManager cursor = new CursorSpritesheetManager();

    /**
     * Creates a pause screen with interactive buttons.
     * 
     * @param nav            navigation manager (unused here, but could be for future navigation calls)
     * @param onResume       callback to resume gameplay
     * @param saveLavel      callback to save the current level/game state (typo: should be saveLevel)
     * @param onExitToMenu   callback to exit pause and go to menu screen
     * @param onExit         callback to exit application entirely
     */
    public PauseScreen(NavigationManager nav, Runnable onResume, Runnable saveLavel, Runnable onExitToMenu, Runnable onExit) {

        Font font = FontManager.getDiabloBigFont(Constant.MENU_BUTTON_ITEMS_FONT_SIZE);
        
        // Title text with custom font and color
        Text title     = new Text(Constant.SCREENS_PAUSED_TYTLE);
        title.setFont(FontManager.getDiabloBigFont(Constant.MENU_BUTTON_TITLE_FONT_SIZE));
        title.setFill(Color.LIGHTGRAY);

        // Create menu items with assigned callbacks
        Text btnResume   = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_RESUME,     font, onResume);
        Text btnSave     = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_SAVE_LAVEL, font, saveLavel);
        Text btnMenu     = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_BACK,       font, onExitToMenu);
        Text btnMainMenu = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_EXIT,       font, onExit);
    
        // Vertical box layout with spacing and center alignment
        VBox root = new VBox(40, title, btnResume, btnSave, btnMenu, btnMainMenu);
        root.setAlignment(Pos.CENTER);
        
        // Apply background style from constants for consistency
        root.setStyle(Constant.MENU_BACKGROUND_STYLE);

        // Initialize scene with fixed app size and custom cursor
        scene = new Scene(root, Constant.APP_SETTINGS_WIDTH, Constant.APP_SETTINGS_HEIGHT);
        scene.setCursor(cursor.getCursor(Integer.parseInt(Config.get("TYPE_CURSOR"))));
    }

    /** 
     * Accessor for the Scene object to be displayed.
     * 
     * @return JavaFX Scene instance for this pause menu
     */
    public Scene getScene() { return scene; }
}