package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import javafx.fxml.FXML;

public final class ControllerSceneLoading {

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
    }
}