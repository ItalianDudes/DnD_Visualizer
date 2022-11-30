package it.italiandudes.dnd_visualizer.javafx.controller.menu;

import it.italiandudes.dnd_visualizer.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.controller.ControllerSceneMenu;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public final class ControllerSceneMenuViewer {

    //Attributes
    @FXML
    private VBox elementPane;
    private static String elementType = null;

    //Initialize
    @FXML
    private void initialize(){
        System.out.println(elementType);
    }

    //EDT

    //Methods
    public static void setElementType(String elementType){
        ControllerSceneMenuViewer.elementType = elementType;
    }
    public static String getElementType(){
        return elementType;
    }

}
