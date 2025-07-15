package com.diakonovtomer.projektObjektorientierung;

import com.diakonovtomer.projektObjektorientierung.ui.screens.*;
import com.diakonovtomer.projektObjektorientierung.util.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main entry point of the Platformer Game application.
 * <p>
 * This class launches the JavaFX application and sets up the primary stage,
 * initializes the navigation manager, and displays the main menu.
 * </p>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 *     java -jar platformer.jar
 * }</pre>
 *
 * @author Artiem
 */
public class Main extends Application {
    
    /**
     * Handles scene transitions and screen management throughout the app.
     */
    private NavigationManager navigationManager;

    /**
     * JavaFX application entry point.
     * Initializes the {@link NavigationManager}, sets up the primary window,
     * and displays the main menu screen.
     *
     * @param primaryStage the main stage provided by JavaFX runtime
     */
    @Override
    public void start(Stage primaryStage) {      

        // Initialize scene navigation
        navigationManager = new NavigationManager(primaryStage);
        
        // Create the main menu screen
        MenuScreen menuScreen = new MenuScreen(navigationManager);
        
        // Configure the main window
        primaryStage.setTitle(Constant.APP_SETTINGS_TITLE);
        primaryStage.setMinWidth(Constant.APP_SETTINGS_WIDTH);
        primaryStage.setMinHeight(Constant.APP_SETTINGS_HEIGHT);
        primaryStage.setResizable(true);
        primaryStage.show();
        
        // Display the main menu screen
        navigationManager.clearAndPush(menuScreen.getScene()); // Показываем главный меню
    }

    /**
     * The main method. Launches the JavaFX application.
     *
     * @param args command-line arguments passed to the app
     */
    public static void main(String[] args) {
        launch(args);
    }
}