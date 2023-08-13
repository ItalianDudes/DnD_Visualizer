package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import javafx.fxml.FXML;

@SuppressWarnings("unused")
public final class ControllerSceneMainMenu {

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
    }

    // EDT
    @FXML
    private void newSheet() {}
    @FXML
    private void openSheet() {}
}
