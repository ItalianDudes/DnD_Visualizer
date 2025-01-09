package it.italiandudes.dnd_visualizer.javafx.scene.inventory;

import it.italiandudes.dnd_visualizer.data.item.ItemContainer;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.inventory.ControllerSceneInventoryItem;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SceneInventoryItem {

    // Scene Generator
    public static SceneController getScene() {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Inventory.FXML_INVENTORY_ITEM));
            Parent root = loader.load();
            ControllerSceneInventoryItem controller = loader.getController();
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit(-1);
            return null;
        }
    }
    public static SceneController getScene(@NotNull final ItemContainer container) {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Inventory.FXML_INVENTORY_ITEM));
            Parent root = loader.load();
            ControllerSceneInventoryItem controller = loader.getController();
            controller.setItemContainer(container);
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit(-1);
            return null;
        }
    }
    public static SceneController getScene(@NotNull final ItemContainer container, @NotNull final String itemName) {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Inventory.FXML_INVENTORY_ITEM));
            Parent root = loader.load();
            ControllerSceneInventoryItem controller = loader.getController();
            controller.setItemName(itemName);
            controller.setItemContainer(container);
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit(-1);
            return null;
        }
    }
}
