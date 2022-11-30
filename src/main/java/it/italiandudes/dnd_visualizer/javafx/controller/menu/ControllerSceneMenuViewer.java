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
    private String elementName;

    //Initialize
    @FXML
    private void initialize(){
        elementName = (String) elementPane.getScene().getWindow().getUserData(); //FIXME
        System.out.println(elementName);
    }

    //EDT

}
