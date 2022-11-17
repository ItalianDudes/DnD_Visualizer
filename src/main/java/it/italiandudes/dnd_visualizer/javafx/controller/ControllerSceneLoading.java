package it.italiandudes.dnd_visualizer.javafx.controller;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public final class ControllerSceneLoading {

    //Attributes
    @FXML
    private ImageView loadingView;
    private static final Image loadingImage = new Image(Objects.requireNonNull(DnD_Visualizer.class.getResource("/gif/loading.gif")).toString());

    //Initialize
    public void initialize() {
        loadingView.setImage(loadingImage);
    }

}
