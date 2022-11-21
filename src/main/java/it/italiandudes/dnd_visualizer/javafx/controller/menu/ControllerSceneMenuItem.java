package it.italiandudes.dnd_visualizer.javafx.controller.menu;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

@SuppressWarnings("unused")
public final class ControllerSceneMenuItem {

    //Attributes
    @FXML
    private BorderPane mainPane;
    @FXML
    private Pane nestedFXMLPane;
    @FXML
    private ComboBox<String> choiceComboBox;

    //Initialize
    public void initialize() {
        AnchorPane.setTopAnchor(mainPane, 0.0);
        AnchorPane.setBottomAnchor(mainPane, 0.0);
        AnchorPane.setRightAnchor(mainPane, 0.0);
        AnchorPane.setLeftAnchor(mainPane, 0.0);
        if(nestedFXMLPane == null)
            nestedFXMLPane = new Pane();
        if(choiceComboBox == null)
            choiceComboBox = new ComboBox<>();
         choiceComboBox.setItems(FXCollections.observableArrayList(
                 ""
         ));
    }

    //Handler
    @FXML
    private void changeShowedPane(ActionEvent event){
        //TODO: Estensione dell'oggetto generico (esempio: Ammunition, Melee Weapon)
    }

}
