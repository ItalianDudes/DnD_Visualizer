package it.italiandudes.dnd_visualizer.javafx.controller;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.db.DBElement;
import it.italiandudes.dnd_visualizer.db.classes.Item;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.controller.menu.ControllerSceneMenuItem;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.SQLiteHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class ControllerSceneElementEditor {

    //Attributes
    @FXML private Pane nestedPane;
    private FXMLLoader nestedFXML;
    private String tableName;
    private static String elementType;
    private static String elementName;
    private static DBElement element;

    //Initialize
    @FXML
    private void initialize(){
        try {
            String fxmlFile = JFXDefs.MenuChoices.getFXMLbyChoiceName(elementType);
            tableName = JFXDefs.MenuChoices.getDBTableNameFromChoiceName(elementType);
            if(fxmlFile==null) throw new RuntimeException("Element Type not found!");
            nestedFXML = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            nestedPane.getChildren().add(nestedFXML.load());
            fillPane();
        }catch (IOException e){
            Logger.log(e);
        }
    }

    //EDT
    private void fillPane(){
        Service<Void> dbCaller = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String query = "SELECT * FROM "+tableName+" WHERE name = '"+elementName+"'";
                        ResultSet set = SQLiteHandler.readDataFromDB(DnD_Visualizer.getDbConnection(), query);
                        if(nestedFXML.getController() instanceof ControllerSceneMenuItem){
                            element = new Item(set);
                            Platform.runLater(() -> ((ControllerSceneMenuItem) nestedFXML.getController()).setDescripedItem((Item) element));
                        }else{
                            throw new RuntimeException("Controller not recognized!");
                        }
                        return null;
                    }
                };
            }
        };
        dbCaller.start();
    }
    @FXML
    private void deleteElement() {
        String query = "DELETE FROM "+tableName+" WHERE name LIKE '"+element.getName()+"'";
        PreparedStatement deleteElement = SQLiteHandler.prepareDataWriteIntoDB(DnD_Visualizer.getDbConnection(), query);
        try {
            deleteElement.execute();
        }catch (SQLException e){
            Logger.log(e);
            new ErrorAlert("ERRORE", "Errore di scrittura nel DB", "C'è stato un errore durante la rimozione dell'elemento");
        }
    }
    @FXML
    private void saveElement(){
        Service<Void> dbWriter = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        if(nestedFXML.getController() instanceof ControllerSceneMenuItem){
                            Item item = ((ControllerSceneMenuItem) nestedFXML.getController()).getDescribedItem();
                            assert item != null;
                            if(!item.updateIntoDB(DnD_Visualizer.getDbConnection())){
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore DB", "Si e' verificato un errore durante l'aggiornamento del DB"));
                            }
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Scrittura DB", "Oggetto aggiornato con successo"));
                        }else{
                            throw new RuntimeException("Controller not recognized!");
                        }
                        return null;
                    }
                };
            }
        };
        dbWriter.start();
    }
    @FXML
    private void clearFields(){
        if(nestedFXML.getController() instanceof ControllerSceneMenuItem){
            ((ControllerSceneMenuItem) nestedFXML.getController()).clearAllFields();
        }else{
            throw new RuntimeException("Controller not recognized!");
        }
    }

    //Methods
    public static void setElement(String elementType, String elementName) {
        ControllerSceneElementEditor.elementType = elementType;
        ControllerSceneElementEditor.elementName = elementName;
    }

}
