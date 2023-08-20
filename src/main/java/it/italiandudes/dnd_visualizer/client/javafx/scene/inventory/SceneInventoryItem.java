package it.italiandudes.dnd_visualizer.client.javafx.scene.inventory;

import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.client.javafx.controller.inventory.ControllerSceneInventoryItem;
import it.italiandudes.dnd_visualizer.client.javafx.util.ThemeHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SceneInventoryItem {

    //Scene Generator
    public static Scene getScene(@NotNull final ControllerSceneSheetViewer sheetController){
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Inventory.FXML_INVENTORY_ITEM));
            Scene scene = new Scene(loader.load());
            ((ControllerSceneInventoryItem) loader.getController()).setSheetController(sheetController);
            ThemeHandler.loadConfigTheme(scene);
            return scene;
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }
}
