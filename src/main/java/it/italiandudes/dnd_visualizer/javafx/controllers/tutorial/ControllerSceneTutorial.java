package it.italiandudes.dnd_visualizer.javafx.controllers.tutorial;

import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.scene.tutorial.SceneTutorialPage1;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

public final class ControllerSceneTutorial {

    // Attributes
    private ControllerSceneSheetViewer sheetController = null;
    private volatile boolean configurationComplete = false;

    // Methods
    public void setSheetController(@NotNull final ControllerSceneSheetViewer sheetController) {
        this.sheetController = sheetController;
    }
    public void configurationComplete() {
        configurationComplete = true;
    }
    public static void setTutorialPage(@NotNull AnchorPane anchorPaneTutorialPage, @NotNull final SceneController scene) {
        setTutorialPage(anchorPaneTutorialPage, scene.getParent());
    }
    public static void setTutorialPage(@NotNull AnchorPane anchorPaneTutorialPage, @NotNull final Parent tutorialPage) {
        anchorPaneTutorialPage.getChildren().clear();
        anchorPaneTutorialPage.getChildren().add(tutorialPage);
        AnchorPane.setBottomAnchor(tutorialPage, 0.0);
        AnchorPane.setLeftAnchor(tutorialPage, 0.0);
        AnchorPane.setRightAnchor(tutorialPage, 0.0);
        AnchorPane.setTopAnchor(tutorialPage, 0.0);
    }

    // Graphic Elements
    @FXML private AnchorPane anchorPaneTutorialPage;

    // Initialize
    @FXML
    private void initialize() {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        //noinspection StatementWithEmptyBody
                        while (!configurationComplete);
                        Platform.runLater(() -> setTutorialPage(anchorPaneTutorialPage, SceneTutorialPage1.getPage(sheetController, anchorPaneTutorialPage)));
                        return null;
                    }
                };
            }
        }.start();
    }
}
