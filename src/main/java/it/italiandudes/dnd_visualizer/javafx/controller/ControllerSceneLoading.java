package it.italiandudes.dnd_visualizer.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public final class ControllerSceneLoading {

    //Attributes
    @FXML
    private ImageView loadingView;

    //Initialize
    public void initialize(){
        loadingView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/gif/loading.gif")).toString()));
    }


}
