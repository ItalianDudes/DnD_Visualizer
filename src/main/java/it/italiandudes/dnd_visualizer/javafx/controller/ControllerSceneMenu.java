package it.italiandudes.dnd_visualizer.javafx.controller;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.idl.common.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ControllerSceneMenu {

    //Attributes
    private static HashMap<String, String> choiceDictionary = null;
    @FXML
    private ComboBox<String> choiceComboBox;
    @FXML
    private AnchorPane nestedFXMLPanel;

    //Initialize
    public void initialize() {
        if(nestedFXMLPanel == null){
            nestedFXMLPanel = new AnchorPane();
        }
        if(choiceDictionary == null){
            choiceDictionary = new HashMap<>();
            for(int i=0;i<DnD_Visualizer.Defs.MenuChoices.getChoiceNames().length;i++){
                choiceDictionary.put(DnD_Visualizer.Defs.MenuChoices.getChoiceNames()[i], DnD_Visualizer.Defs.MenuChoices.getFxmlPrefix()[i]);
            }
        }
        if(choiceComboBox == null)
            choiceComboBox = new ComboBox<>();
        choiceComboBox.setItems(FXCollections.observableArrayList(DnD_Visualizer.Defs.MenuChoices.getChoiceNames()));
    }

    //Handler
    @FXML
    private void changeShowedPane(ActionEvent event) {
        String item = choiceComboBox.getValue();
        try {
            nestedFXMLPanel.getChildren().clear();
            nestedFXMLPanel.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(choiceDictionary.get(item)))));
        } catch (IOException e) {
            if (Logger.isInitialized()) {
                Logger.log(e);
            } else {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleSaveButton(ActionEvent event){
        //TODO: handleSaveButton
    }
    @FXML
    private void handleCancelButton(ActionEvent event){
        //TODO: handleCancelButton
    }

}
