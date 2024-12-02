package it.italiandudes.dnd_visualizer.client.javafx.scene.tutorial;

import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.client.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.client.javafx.controller.tutorial.ControllerSceneTutorial;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SceneTutorial {

    // Scene Generator
    @NotNull
    public static SceneController getScene(@NotNull final ControllerSceneSheetViewer sheetController) {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Tutorial.FXML_TUTORIAL));
            Parent root = loader.load();
            ControllerSceneTutorial controller = loader.getController();
            controller.setSheetController(sheetController);
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e);
            System.exit(-1);
            return null;
        }
    }
}