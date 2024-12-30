package it.italiandudes.dnd_visualizer.javafx.scene.tutorial;

import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.controllers.tutorial.ControllerSceneTutorialPage1;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SceneTutorialPage1 {

    // Page Generator
    @NotNull
    public static SceneController getPage(@NotNull final ControllerSceneSheetViewer sheetController, @NotNull final AnchorPane anchorPaneTutorialPage) {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Tutorial.FXML_TUTORIAL_PAGE1));
            Parent root = loader.load();
            ControllerSceneTutorialPage1 controller = loader.getController();
            controller.setSheetController(sheetController);
            controller.setAnchorPaneTutorialPage(anchorPaneTutorialPage);
            controller.configurationComplete();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            System.exit(-1);
            return null;
        }
    }
}