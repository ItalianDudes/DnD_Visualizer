package it.italiandudes.dnd_visualizer.javafx.controllers;

import it.italiandudes.dnd_visualizer.features.ProTipsManager;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class ControllerSceneLoading {

    // Attributes
    public static final Image LOADING_GIF = new Image(JFXDefs.Resources.GIF.GIF_LOADING);

    // Graphic Elements
    @FXML private ImageView imageViewLoading;
    @FXML private ImageView imageViewLogo;
    @FXML private Label labelProTip;

    //Initialize
    @FXML
    private void initialize() {
        imageViewLoading.setImage(LOADING_GIF);
        imageViewLogo.setImage(JFXDefs.AppInfo.LOGO);
        labelProTip.setText(ProTipsManager.getRandomProTip());
    }
}