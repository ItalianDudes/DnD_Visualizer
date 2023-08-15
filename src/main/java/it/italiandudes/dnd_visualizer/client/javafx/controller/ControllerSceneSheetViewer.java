package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public final class ControllerSceneSheetViewer {

    // Attributes
    private Rectangle hpRemoverRectangle;

    //Graphic Elements
    @FXML private ImageView imageViewCurrentHP;
    @FXML private StackPane stackPaneCurrentHP;
    @FXML private Label labelHPLeftPercentage;
    @FXML private TextField textFieldMaxHP;
    @FXML private TextField textFieldCurrentHP;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        hpRemoverRectangle = new Rectangle(imageViewCurrentHP.getFitWidth(), 0, Client.getBackgroundThemeColor());
    }

    // EDT
    @FXML
    private void recalculateHealthPercentage() {
        try {
            int hpPercentage = (int) ((Double.parseDouble(textFieldCurrentHP.getText()) / Double.parseDouble(textFieldMaxHP.getText())) * 100.0);
            double newHealthPercentage = (imageViewCurrentHP.getFitHeight()-25) - (imageViewCurrentHP.getFitHeight() * Double.parseDouble(textFieldCurrentHP.getText())) / Double.parseDouble(textFieldMaxHP.getText());
            if (newHealthPercentage<0) newHealthPercentage = 0;
            hpRemoverRectangle.setHeight(newHealthPercentage);
            stackPaneCurrentHP.getChildren().remove(hpRemoverRectangle);
            stackPaneCurrentHP.getChildren().add(hpRemoverRectangle);
            labelHPLeftPercentage.setText(hpPercentage > 500?">500%":hpPercentage+"%");
        } catch (NumberFormatException ignored) {}
    }
}
