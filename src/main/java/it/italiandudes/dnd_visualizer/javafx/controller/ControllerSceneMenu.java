package it.italiandudes.dnd_visualizer.javafx.controller;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.db.classes.Item;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.controller.menu.ControllerSceneMenuItem;
import it.italiandudes.dnd_visualizer.javafx.controller.menu.ControllerSceneMenuLanguage;
import it.italiandudes.dnd_visualizer.javafx.controller.menu.ControllerSceneMenuViewer;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public final class ControllerSceneMenu {

    //Attributes
    private static HashMap<String, String> choiceDictionary = null;
    @FXML
    private ComboBox<String> choiceComboBox;
    @FXML
    private CheckBox viewOpt;
    private boolean viewMode;
    @FXML
    private Button clearButton;
    @FXML
    private Button saveButton;
    @FXML
    private AnchorPane nestedFXMLPanel;
    private FXMLLoader nestedFXML;

    //Initialize
    @FXML
    private void initialize() {
        viewOpt.setSelected(false);
        viewOpt.setDisable(true);
        if(nestedFXMLPanel == null){
            nestedFXMLPanel = new AnchorPane();
        }
        if(choiceDictionary == null){
            choiceDictionary = new HashMap<>();
            for(int i = 0; i< JFXDefs.MenuChoices.getChoiceNames().length; i++){
                choiceDictionary.put(JFXDefs.MenuChoices.getChoiceNames()[i], JFXDefs.MenuChoices.getFxmlPrefix()[i]);
            }
        }
        if(choiceComboBox == null) choiceComboBox = new ComboBox<>();
        choiceComboBox.setItems(FXCollections.observableArrayList(JFXDefs.MenuChoices.getChoiceNames()));
    }

    //Handler
    @FXML
    private void changeViewMode(){
        viewMode = viewOpt.isSelected();
        changeShowedPane();
    }
    @FXML
    private void changeShowedPane() {
        viewOpt.setDisable(false);
        String item = choiceComboBox.getValue();
        try {
            nestedFXMLPanel.getChildren().clear();
            if(!viewMode) {
                saveButton.setDisable(false);
                clearButton.setDisable(false);
                nestedFXML = new FXMLLoader(Objects.requireNonNull(getClass().getResource(choiceDictionary.get(item))));
            }else{
                saveButton.setDisable(true);
                clearButton.setDisable(true);
                ControllerSceneMenuViewer.setElementType(item);
                nestedFXML = new FXMLLoader(Objects.requireNonNull(getClass().getResource(JFXDefs.MenuChoices.FXML_VIEW)));
            }
            nestedFXMLPanel.getChildren().add(nestedFXML.load());
        } catch (IOException e) {
            if (Logger.isInitialized()) {
                Logger.log(e);
            } else {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleSaveButton(){
        String choice = choiceComboBox.getValue();
        System.out.println(choice);
        if(choice==null || choice.equals("")) return;
        if(nestedFXML.getController() instanceof ControllerSceneMenuItem){
            ControllerSceneMenuItem itemMenu = nestedFXML.getController();
            Item describedItem = itemMenu.getDescribedItem();
            if(describedItem==null) return;
            Service<Void> dbWriterService = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() {
                            if(!describedItem.writeIntoDB(DnD_Visualizer.getDbConnection())){
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore DB", "Si e' verificato un errore durante la scrittura nel DB"));
                            }
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Scrittura DB", "Oggetto salvato con successo"));
                            return null;
                        }
                    };
                }
            };
            dbWriterService.start();
        }else if(nestedFXML.getController() instanceof ControllerSceneMenuLanguage){
            ControllerSceneMenuLanguage langMenu = nestedFXML.getController();
            //TODO: Continue Language
        }else{
            throw new RuntimeException("Controller not recognized!");
        }
    }
    @FXML
    private void handleClearButton(){
        String choice = choiceComboBox.getValue();
        if(choice==null || choice.equals("")) return;
        if(nestedFXML.getController() instanceof ControllerSceneMenuItem){
            ControllerSceneMenuItem itemMenu = nestedFXML.getController();
            itemMenu.clearAllFields();
        }else if(nestedFXML.getController() instanceof ControllerSceneMenuLanguage){
            ControllerSceneMenuLanguage langMenu = nestedFXML.getController();
            //TODO: Continue Lang Cancel Button
        }else{
            throw new RuntimeException("Controller not recognized!");
        }

    }

}
