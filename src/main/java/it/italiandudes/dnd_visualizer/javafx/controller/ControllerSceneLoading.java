package it.italiandudes.dnd_visualizer.javafx.controller;

import it.italiandudes.dnd_visualizer.javafx.Client;
import javafx.fxml.FXML;

@SuppressWarnings("unused")
public final class ControllerSceneLoading {

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
    }
}