package it.italiandudes.dnd_visualizer.javafx.controllers.tutorial;

import it.italiandudes.dnd_visualizer.javafx.controllers.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.scene.tutorial.SceneTutorialPage2;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

public final class ControllerSceneTutorialPage1 {

    // Attributes
    private ControllerSceneSheetViewer sheetController = null;
    private AnchorPane anchorPaneTutorialPage = null;
    private volatile boolean configurationComplete = false;

    // Methods
    public void setSheetController(@NotNull final ControllerSceneSheetViewer sheetController) {
        this.sheetController = sheetController;
    }
    public void setAnchorPaneTutorialPage(@NotNull final AnchorPane anchorPaneTutorialPage) {
        this.anchorPaneTutorialPage = anchorPaneTutorialPage;
    }
    public void configurationComplete() {
        configurationComplete = true;
    }

    // Graphic Elements
    @FXML private Button buttonNextPage;

    // Initialize
    @FXML
    private void initialize() {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        while (!configurationComplete) Thread.onSpinWait();
                        buttonNextPage.setDisable(false);
                        return null;
                    }
                };
            }
        }.start();
    }

    // EDT
    @FXML
    private void nextPage() {
        ControllerSceneTutorial.setTutorialPage(anchorPaneTutorialPage, SceneTutorialPage2.getPage(sheetController, anchorPaneTutorialPage));
    }
}
