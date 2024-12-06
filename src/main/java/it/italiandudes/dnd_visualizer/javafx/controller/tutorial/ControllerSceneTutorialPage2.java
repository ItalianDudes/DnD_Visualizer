package it.italiandudes.dnd_visualizer.javafx.controller.tutorial;

import it.italiandudes.dnd_visualizer.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.scene.tutorial.SceneTutorialPage1;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

public final class ControllerSceneTutorialPage2 {

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
    @FXML private TextField textFieldCharacterName;
    @FXML private TextField textFieldPlayerName;
    @FXML private Button buttonPreviousPage;
    @FXML private Button buttonNextPage;

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
                        buttonPreviousPage.setDisable(false);
                        buttonNextPage.setDisable(false);
                        Platform.runLater(ControllerSceneTutorialPage2.this::postInitialize);
                        return null;
                    }
                };
            }
        }.start();
    }
    private void postInitialize() {
        textFieldCharacterName.setText(sheetController.textFieldCharacterName.getText());
        textFieldPlayerName.setText(sheetController.textFieldPlayerName.getText());
        setOnChangeTriggers();
    }

    // OnChange Triggers Setter
    private void setOnChangeTriggers() {
        textFieldCharacterName.textProperty().addListener((observable, oldValue, newValue) -> sheetController.textFieldCharacterName.setText(newValue));
        textFieldPlayerName.textProperty().addListener((observable, oldValue, newValue) -> sheetController.textFieldPlayerName.setText(newValue));
    }

    // EDT
    @FXML
    private void previousPage() {
        ControllerSceneTutorial.setTutorialPage(anchorPaneTutorialPage, SceneTutorialPage1.getPage(sheetController, anchorPaneTutorialPage));
    }
    @FXML
    private void nextPage() {}
}
