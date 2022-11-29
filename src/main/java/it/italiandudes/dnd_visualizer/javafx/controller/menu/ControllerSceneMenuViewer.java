package it.italiandudes.dnd_visualizer.javafx.controller.menu;

import it.italiandudes.dnd_visualizer.javafx.alert.InformationAlert;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public final class ControllerSceneMenuViewer {

    //Attributes
    @FXML
    private VBox elementPane;

    //Initialize
    @FXML
    private void initialize(){
        Label label = new Label("MAN");
        label.setOnMouseClicked(event -> {
            new InformationAlert("CIAO", "CIAO", "CIAO");
        });
        elementPane.getChildren().add(label);
        elementPane.getChildren().add(new Label("PANE"));
        elementPane.getChildren().add(new Label("MARCONI"));
    }

    //EDT

}
