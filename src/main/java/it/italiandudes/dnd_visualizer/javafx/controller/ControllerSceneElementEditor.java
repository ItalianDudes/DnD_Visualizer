package it.italiandudes.dnd_visualizer.javafx.controller;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.db.DBElement;
import it.italiandudes.dnd_visualizer.db.classes.Item;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.alert.ConfirmationAlert;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class ControllerSceneElementEditor {

    //Attributes
    @FXML private AnchorPane mainPane;
    @FXML private Pane nestedPane;
    private FXMLLoader nestedFXML;
    private String tableName;
    private static Stage thisStage;
    private static String elementType;
    private static String elementName;
    private static DBElement element;

    //Initialize
    @FXML
    private void initialize(){
        try {
            AnchorPane.setBottomAnchor(mainPane, 0.0);
            AnchorPane.setLeftAnchor(mainPane, 0.0);
            AnchorPane.setRightAnchor(mainPane, 0.0);
            AnchorPane.setTopAnchor(mainPane, 0.0);
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

    //EDT & Methods
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
        if(new ConfirmationAlert("CONFERMA RIMOZIONE", "Rimozione Elemento", "Sei sicuro di rimuovere questo elemento dal DB?").result) {
            //TODO: chiedi conferma per l'eliminazione, se va a buon fine spara un alert di conferma e alla chiusura dell'alert chiudi lo stage dell'editor e rivisualizza il viewer
            String query = "DELETE FROM " + tableName + " WHERE name LIKE '" + element.getName() + "'";
            PreparedStatement deleteElement = SQLiteHandler.prepareDataWriteIntoDB(DnD_Visualizer.getDbConnection(), query);
            try {
                deleteElement.execute();
            } catch (SQLException e) {
                Logger.log(e);
                new ErrorAlert("ERRORE", "Errore di scrittura nel DB", "C'è stato un errore durante la rimozione dell'elemento");
                return;
            }
            new InformationAlert("CONFERMA", "Conferma Rimozione", "Rimozione dell'elemento dal database completata con successo!", true);
            thisStage.fireEvent(new WindowEvent(thisStage, WindowEvent.WINDOW_CLOSE_REQUEST));
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
    public static void setElement(String elementType, String elementName, Stage stage) {
        ControllerSceneElementEditor.elementType = elementType;
        ControllerSceneElementEditor.elementName = elementName;
        thisStage = stage;
    }

}
