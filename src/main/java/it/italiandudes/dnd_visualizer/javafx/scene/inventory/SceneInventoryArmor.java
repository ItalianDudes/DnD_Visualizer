package it.italiandudes.dnd_visualizer.javafx.scene.inventory;

import it.italiandudes.dnd_visualizer.data.item.ItemContainer;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.inventory.ControllerSceneInventoryArmor;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public final class SceneInventoryArmor {

    // Scene Generator
    @NotNull
    public static SceneController getScene() {
        return Objects.requireNonNull(genScene());
    }
    @Nullable
    private static SceneController genScene() {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Inventory.FXML_INVENTORY_ARMOR));
            Parent root = loader.load();
            ControllerSceneInventoryArmor controller = loader.getController();
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit(-1);
            return null;
        }
    }
    @NotNull
    public static SceneController getScene(@NotNull final ItemContainer container) {
        return Objects.requireNonNull(genScene(container));
    }
    @Nullable
    private static SceneController genScene(@NotNull final ItemContainer container) {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Inventory.FXML_INVENTORY_ARMOR));
            Parent root = loader.load();
            ControllerSceneInventoryArmor controller = loader.getController();
            controller.setItemContainer(container);
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit(-1);
            return null;
        }
    }
    @NotNull
    public static SceneController getScene(@NotNull final ItemContainer container, @NotNull final String armorName) {
        return Objects.requireNonNull(genScene(container, armorName));
    }
    @Nullable
    private static SceneController genScene(@NotNull final ItemContainer container, @NotNull final String armorName) {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Inventory.FXML_INVENTORY_ARMOR));
            Parent root = loader.load();
            ControllerSceneInventoryArmor controller = loader.getController();
            controller.setArmorName(armorName);
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
