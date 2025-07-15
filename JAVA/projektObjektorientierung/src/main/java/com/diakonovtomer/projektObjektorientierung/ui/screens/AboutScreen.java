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
 * AboutScreen class displays a simple "About" screen
 * with a title and a "Back" button.
 * 
 * Uses:
 * - Custom Diablo font for styling
 * - NavigationManager to go back to the previous screen
 * - CursorSpritesheetManager for custom cursor
 */
public class AboutScreen {

    private final NavigationManager navigationManager;
    private final Scene scene;
    private final CursorSpritesheetManager cursorManager = new CursorSpritesheetManager();

    /**
     * Constructs the About screen.
     * 
     * @param navigationManager Reference to the global navigation manager
     */
    public AboutScreen(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
        
        Font menuFont = FontManager.getDiabloBigFont(Constant.MENU_BUTTON_ITEMS_FONT_SIZE); // Подключаем кастомный шрифт из ресурсов

        BorderPane root = new BorderPane();
        root.setStyle(Constant.MENU_BACKGROUND_STYLE);
        
         /* -------- Title -------- */
        Text title = new Text(Constant.SCREENS_TYTLE_TYTLE);
        title.setFont(FontManager.getDiabloBigFont(Constant.MENU_BUTTON_TITLE_FONT_SIZE));
        title.setFill(Color.LIGHTGRAY);
        
        // Центрируем заголовок
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new javafx.geometry.Insets(Constant.BOX_PADDING)); // отступ от края
        root.setTop(topBox);
        
        /* -------- Back Button -------- */
        Text txtBack = MenuItemMaker.create(Constant.MENU_BUTTON_ITEMS_TEXT_BACK, menuFont, () -> onBackToMenu()); // Кнопка "Back"
        
        VBox bottomBox = new VBox(txtBack);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new javafx.geometry.Insets(Constant.BOX_PADDING)); // отступ от края
        root.setBottom(bottomBox);
        
        /* -------- Scene Setup -------- */
        scene = new Scene(root, Constant.APP_SETTINGS_WIDTH, Constant.APP_SETTINGS_HEIGHT);
        scene.setCursor(cursorManager.getCursor(Integer.parseInt(Config.get("TYPE_CURSOR"))));
    }
       
    /**
     * Handles navigation when the "Back" button is clicked.
     */
    private void onBackToMenu(){ navigationManager.pop(); }
 
    /**
     * Returns the constructed JavaFX Scene.
     * 
     * @return JavaFX Scene instance
     */
    public Scene getScene() { return scene; }
}