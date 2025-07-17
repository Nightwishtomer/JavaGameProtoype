package com.diakonovtomer.projektObjektorientierung.ui.screens;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.util.NavigationManager;
import com.diakonovtomer.projektObjektorientierung.util.FontManager;
import com.diakonovtomer.projektObjektorientierung.util.CursorSpritesheetManager;
import com.diakonovtomer.projektObjektorientierung.util.MenuItemMaker;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Main menu screen that provides navigation to:
 * - Start game (leads to login)
 * - About screen
 * - Settings screen
 * - Exit application
 *
 * This class handles simple UI creation and interaction wiring.
 */
public class MenuScreen {

    private final NavigationManager navigationManager;
    private final Scene scene;
    private final CursorSpritesheetManager cursorManager = new CursorSpritesheetManager();
    
    /**
     * Constructs the menu screen UI, setting up buttons and styling.
     * 
     * @param navigationManager Reference for managing scene navigation stack
     */
    public MenuScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        
        // Load custom font for menu items with predefined size
        Font menuFont = FontManager.getDiabloBigFont(Constant.MENU_BUTTON_ITEMS_FONT_SIZE); // Подключаем кастомный шрифт из ресурсов

        // Create clickable menu items with their action handlersS
        Text txtStart    = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_START,    menuFont, () -> onStartGame());
        Text txtAbout    = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_ABOUT,    menuFont, () -> onAboutScreen());
        Text txtSettings = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_SETTINGS, menuFont, () -> onShowSettings());
        Text txtExit     = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_EXIT,     menuFont, () -> onExit());
        
        // Layout container with vertical spacing between items, centered alignment
        VBox menu = new VBox(30, txtStart, txtAbout, txtSettings, txtExit);
        menu.setAlignment(Pos.CENTER);
        
        // Apply background style defined in constants (could be color, image, etc.)
        menu.setStyle(Constant.MENU_BACKGROUND_STYLE);

        // Create the scene with fixed width/height constants and set custom cursor
        scene = new Scene(menu, Constant.APP_SETTINGS_WIDTH, Constant.APP_SETTINGS_HEIGHT);
        scene.setCursor(cursorManager.getCursor(Integer.parseInt(Config.get("TYPE_CURSOR"))));
    }

    /** 
     * Called when "Start" is clicked - pushes LoginScreen on navigation stack.
     */
    private void onStartGame(){
        LoginScreen loginScreen = new LoginScreen(navigationManager);
        navigationManager.push(loginScreen.getScene());
    }
    
    /**
     * Called when "About" is clicked - pushes AboutScreen.
     */
    private void onAboutScreen(){
        AboutScreen aboutScreen = new AboutScreen(navigationManager);
        navigationManager.push(aboutScreen.getScene());
    }
    
    /**
     * Called when "Settings" is clicked - pushes SettingsScreen.
     */
    private void onShowSettings(){
        SettingsScreen settingsScreen = new SettingsScreen(navigationManager);
        navigationManager.push(settingsScreen.getScene());
    }
    
    /**
     * Called when "Exit" is clicked - cleanly exits the application.
     */
    private void onExit(){ System.exit(0); }

    /** 
     * Exposes the JavaFX scene to be shown.
     * 
     * @return the Scene instance managed by this screen
     */
    public Scene getScene() { return scene; }
}
