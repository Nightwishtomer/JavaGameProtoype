package com.diakonovtomer.projektObjektorientierung.ui.screens;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.Constant;
import com.diakonovtomer.projektObjektorientierung.network.ApiService;
import com.diakonovtomer.projektObjektorientierung.util.CursorSpritesheetManager;
import com.diakonovtomer.projektObjektorientierung.util.FontManager;
import com.diakonovtomer.projektObjektorientierung.util.NavigationManager;
import com.diakonovtomer.projektObjektorientierung.util.AuthContext;
import com.diakonovtomer.projektObjektorientierung.util.MenuItemMaker;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * LoginScreen UI class for user authentication.
 * 
 * Features:
 * - Username and password input fields with styling
 * - Login button triggers async API call
 * - Shows error messages inline and as alert dialogs
 * - Back button to return to previous menu
 */
public class LoginScreen {
    private final NavigationManager navigationManager;
    private final Scene scene;
    private final CursorSpritesheetManager cursorManager = new CursorSpritesheetManager();
    private final TextField nameField  = new TextField();
    private final PasswordField passField = new PasswordField();
    private Text txtLoginHelp;
    
    /**
     * Constructs the login screen.
     * Sets up UI components, styles, and event handlers.
     */
    public LoginScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        Font menuFont = FontManager.getDiabloBigFont(Constant.MENU_BUTTON_ITEMS_FONT_SIZE); // Подключаем кастомный шрифт из ресурсов
        BorderPane root = new BorderPane();
        root.setStyle(Constant.MENU_BACKGROUND_STYLE);
        // Title text at the top
        Text title = new Text(Constant.SCREENS_LOGINSCREEN_TYTLE);
        title.setFont(FontManager.getDiabloBigFont(Constant.MENU_BUTTON_TITLE_FONT_SIZE));
        title.setFill(Color.LIGHTGRAY);
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new javafx.geometry.Insets(20)); // отступ от края
        root.setTop(topBox);
        
        // Username input row
        Text nameLabel = makeLabel(Constant.MENU_ITEMS_TEXT_NAME);
        stylizeField(nameField);
        HBox nameRow = new HBox(10, nameLabel, nameField);
        nameRow.setAlignment(Pos.CENTER);
        
        // Password input row
        Text passLabel = makeLabel(Constant.MENU_ITEMS_TEXT_PASSWORD);
        stylizeField(passField);
        HBox passRow = new HBox(10, passLabel, passField);
        passRow.setAlignment(Pos.CENTER);

        // Login button
        Text txtLogin = MenuItemMaker.create(Constant.MENU_ITEMS_TEXT_LOGIN, FontManager.getDiabloSmallFont(30), () -> onLogin(nameField.getText(), passField.getText()));
        
        // Help / error message text
        txtLoginHelp = new Text(Constant.MENU_ITEMS_TEXT_ENTER_N_P);
        txtLoginHelp.setFont(FontManager.getDiabloSmallFont(30));
        txtLoginHelp.setFill(Color.GRAY);
        VBox centerBox = new VBox(15, nameRow, passRow, txtLoginHelp, txtLogin);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(Constant.BOX_PADDING));
        root.setCenter(centerBox);
        
        Text txtBack = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_BACK, menuFont, () -> onBackToMenu());
      
        VBox bottomBox = new VBox(txtBack);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new javafx.geometry.Insets(Constant.BOX_PADDING)); // отступ от края
        root.setBottom(bottomBox);
        
        scene = new Scene(root, Constant.APP_SETTINGS_WIDTH, Constant.APP_SETTINGS_HEIGHT);
        scene.setCursor(cursorManager.getCursor(Integer.parseInt(Config.get("TYPE_CURSOR"))));
    }
    
    /** Handles navigation back to previous screen */
    private void onBackToMenu(){ navigationManager.pop(); }
    
    /**
     * Attempts login via API.
     * Updates UI accordingly on success or failure.
     *
     * @param user Username input
     * @param pass Password input
     */
    private void onLogin(String user, String pass) {
        ApiService.auth(user, pass)
            .thenAccept(token -> Platform.runLater(() -> {
                AuthContext.set(user, pass, ApiService.getJwt());
               navigationManager.push(new SelectGameScreen(navigationManager).getScene());
            }))
            .exceptionally(ex -> {
                // Display error message if login fails
                txtLoginHelp.setFill(Color.RED);
                txtLoginHelp.setText(Constant.MENU_ITEMS_TEXT_ENTER_A_N_P);
                return null;
            });
    }
    
    /**
     * Shows a modal error dialog.
     *
     * @param msg The error message to display
     */
    private void showError(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Authorization failed");
            alert.setContentText(msg);
            alert.initOwner(scene.getWindow());   // сцена LoginScreen
            alert.showAndWait();
        });
    }

    /** Helper method to create styled label */
    private Text makeLabel(String text) {
        Text label = new Text(text);
        label.setFont(FontManager.getDiabloBigFont(Constant.MENU_BUTTON_ITEMS_FONT_SIZE));
        label.setFill(Color.LIGHTGRAY);
        return label;
    }
       
    /** Applies consistent styling to text fields */
    private void stylizeField(TextField field){
        field.setPrefWidth(260);
        field.setPrefHeight(40);
        field.setFont(FontManager.getDiabloBigFont(Constant.MENU_BUTTON_ITEMS_FONT_SIZE));
        field.setStyle("""
            -fx-padding: 4 6 4 6;       
            -fx-background-radius: 4;
            -fx-background-color: black;
            -fx-text-fill: white;
        """);
    }

    /** Returns the JavaFX scene of this screen */
    public Scene getScene() {
        return scene;
    }
}