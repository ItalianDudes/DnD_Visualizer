package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer.TabCharacter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

@SuppressWarnings("unused")
public final class ControllerSceneSheetViewer {

    // Attributes
    public Rectangle hpRemoverRectangle;
    public boolean isCharacterImageSet = false;
    public String characterImageExtension = null;

    //Graphic Elements
    @FXML public ImageView imageViewCharacterImage;
    @FXML public ImageView imageViewCurrentHP;
    @FXML public StackPane stackPaneCurrentHP;
    @FXML public Label labelHPLeftPercentage;
    @FXML public TextField textFieldMaxHP;
    @FXML public TextField textFieldCurrentHP;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        imageViewCharacterImage.setImage(Client.getDefaultImage());
        hpRemoverRectangle = new Rectangle(imageViewCurrentHP.getFitWidth(), 0, Client.getBackgroundThemeColor());
    }

    // EDT
    @FXML private void recalculateHealthPercentage() {
        TabCharacter.recalculateHealthPercentage(this);
    }
    @FXML private void openCharacterImageFileChooser() {
        TabCharacter.openCharacterImageFileChooser(this);
    }
}
