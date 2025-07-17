package com.diakonovtomer.projektObjektorientierung.util;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Utility class for creating styled interactive menu items (text-based buttons) used in the UI.
 * <p>
 * This class simplifies the creation of clickable text elements with a hover effect and attached action.
 * </p>
 *
 * <h3>Features:</h3>
 * <ul>
 *   <li>Custom font and color styling</li>
 *   <li>Hover effect: changes color on mouse over</li>
 *   <li>Click action: triggers a {@link Runnable} when clicked</li>
 * </ul>
 *
 * <h3>Usage example:</h3>
 * <pre>{@code
 * Text startButton = MenuItemMaker.create("Start", myFont, () -> startGame());
 * }</pre>
 *
 * @author adiakonov
 */
public class MenuItemMaker {
    
    /**
     * Creates a styled and interactive menu item as a {@link Text} node.
     * <p>
     * The item responds to mouse hover by changing its color,
     * and executes the provided action on mouse click.
     * </p>
     *
     * @param text   the visible label of the menu item
     * @param font   the font to be used for the item
     * @param action the action to perform when the item is clicked
     * @return a fully configured {@link Text} node ready to be added to the scene
     */
    public static Text create(String text, Font font, Runnable action) {
        Text item = new Text(text);
        item.setFont(font);
        item.setFill(Color.LIGHTGRAY);
        item.setOnMouseEntered(e -> { item.setFill(Color.GOLDENROD); });
        item.setOnMouseExited(e -> { item.setFill(Color.LIGHTGRAY); });
        item.setOnMouseClicked(e -> action.run());
        return item;
    }
}
