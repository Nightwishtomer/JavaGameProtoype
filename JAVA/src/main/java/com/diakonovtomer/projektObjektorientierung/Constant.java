package com.diakonovtomer.projektObjektorientierung;

import javafx.scene.paint.Color;

/**
 * The {@code Constant} class defines all the global constants used in the platformer game.
 * <p>
 * This includes UI dimensions, font paths, screen titles, player and tile settings,
 * menu labels, and resources used throughout the application.
 * </p>
 * <p>
 * This class is marked {@code final} and has a private constructor to prevent instantiation.
 * </p>
 * 
 * @author Artiem Diakonov
 */

public final class Constant {

    /**
     * Private constructor to prevent instantiation.
     */
    private Constant() {}

    /** Color used to highlight tiles in the game. */
    public static final Color HIGHLIGHT_TILE_COLOR = Color.YELLOW;
    
    // ─────────────────────────────────────────────────────────────────────────────
    // Application Settings
    // ─────────────────────────────────────────────────────────────────────────────

    /** Title displayed in the application window. */
    public static final String APP_SETTINGS_TITLE = "Projekt Objectorientirung Game by Artiem Diakonov";
    
    /** Default width of the application window. */
    public static final int APP_SETTINGS_WIDTH = 800;
    
    /** Default height of the application window. */
    public static final int APP_SETTINGS_HEIGHT = 600;
    
    // ─────────────────────────────────────────────────────────────────────────────
    // Tile and Player Settings
    // ─────────────────────────────────────────────────────────────────────────────

    /** Width of a single tile in pixels. */
    public static final double TILE_W = 64;
    
    /** Height of a single tile in pixels. */
    public static final double TILE_H = 64;
    
    /** Height of the player character in pixels. */
    public static final double PLAYER_SIZE_H = 64;  
    
    /** Width of the player character in pixels. */
    public static final double PLAYER_SIZE_W = 64;
    
    // ─────────────────────────────────────────────────────────────────────────────
    // Menu Strings and Styles
    // ─────────────────────────────────────────────────────────────────────────────

    /** CSS style for the main menu background. */
    public static final String MENU_BACKGROUND_STYLE = "-fx-background-color: black;";
    
    /** Label for the "Start Game" button. */
    public static final String MENU_BUTTON_ITEMS_TEXT_START = "Start game";
    
    /** Label for the "About" button. */
    public static final String MENU_BUTTON_ITEMS_TEXT_ABOUT = "About the program";
    
    /** Label for the "Settings" button. */
    public static final String MENU_BUTTON_ITEMS_TEXT_SETTINGS = "Settings";
    
    /** Label for the "Exit" button. */
    public static final String MENU_BUTTON_ITEMS_TEXT_EXIT = "Exit";
    
    /** Label for the "Back" button. */
    public static final String MENU_BUTTON_ITEMS_TEXT_BACK = "Back to menu";
    
    /** Label for the "Resume" button. */
    public static final String MENU_BUTTON_ITEMS_TEXT_RESUME = "Resume";
    
    /** Label for the "Save Level" button. */
    public static final String MENU_BUTTON_ITEMS_TEXT_SAVE_LAVEL = "Save level";
    
    /** Label for the username field. */
    public static final String MENU_ITEMS_TEXT_NAME = "Name:";
    
    /** Label for the password field. */
    public static final String MENU_ITEMS_TEXT_PASSWORD = "Password:";
    
    /** Label for the "Login" button. */
    public static final String MENU_ITEMS_TEXT_LOGIN = "Login";
    
    /** Text shown to prompt name and password entry. */
    public static final String MENU_ITEMS_TEXT_ENTER_N_P = " \n Enter Name / Password \n ";
    
    /** Text shown when login credentials are incorrect. */
    public static final String MENU_ITEMS_TEXT_ENTER_A_N_P = " \n enter another Name / Password \n ";  
    
    /** Font size for the menu title. */    
    public static final int MENU_BUTTON_TITLE_FONT_SIZE = 48;
    
    /** Font size for regular menu buttons. */
    public static final int MENU_BUTTON_ITEMS_FONT_SIZE = 40;
    

    // ─────────────────────────────────────────────────────────────────────────────
    // Font Paths
    // ─────────────────────────────────────────────────────────────────────────────

    /** Default font file. */
    public static final String FONTS_DEFAULT_FONT = "arial.ttf";
    
    /** Path to the large Diablo-style font. */
    public static final String FONTS_DIABLO_BIG_FONT_PATH = "/fonts/diablo_h.ttf";
    
    /** Path to the small font (Arial). */
    public static final String FONTS_DIABLO_SMALL_FONT_PATH = "/fonts/arial.ttf";
        
    // ─────────────────────────────────────────────────────────────────────────────
    // Resource Paths
    // ─────────────────────────────────────────────────────────────────────────────

    /** Path to the cursor sprite sheet. */
    public static final String RESOURCES_CURSORS_SPRITESHEET_PATH = "/cursors/cursors.png";
    
    /** Path to the cursor metadata file. */
    public static final String RESOURCES_CURSORS_DATA_PATH = "/cursors/cursors.dat";
    
    // ─────────────────────────────────────────────────────────────────────────────
    // UI Layout Constants
    // ─────────────────────────────────────────────────────────────────────────────

    /** Default padding used for UI boxes. */
    public static final int BOX_PADDING = 20;
    
    // ─────────────────────────────────────────────────────────────────────────────
    // Screen Titles
    // ─────────────────────────────────────────────────────────────────────────────

    /** Title for the "About" screen. */
    public static final String SCREENS_TYTLE_TYTLE = "About";
    
    /** Title for the game screen (Canvas). */
    public static final String SCREENS_GAMESCREEN_TYTLE = "Canvas Game";
    
    /** Title for the login screen. */
    public static final String SCREENS_LOGINSCREEN_TYTLE = "Login Screen";
    
    /** Title for the pause screen. */
    public static final String SCREENS_PAUSED_TYTLE = "Paused";
    
    /** Title for the game selection screen. */
    public static final String SCREENS_SELECTGAME_TYTLE = "Select Game";
    
    /** Title for the settings screen. */
    public static final String SCREENS_SETTINGS_TYTLE = "Settings";
    
    // ─────────────────────────────────────────────────────────────────────────────
    // Map Dimensions
    // ─────────────────────────────────────────────────────────────────────────────

    /** Width of the game map in tiles. */
    public static final int MAP_WIDTH = 160;
    
    /** Height of the game map in tiles. */
    public static final int MAP_HEIGHT = 160;
}