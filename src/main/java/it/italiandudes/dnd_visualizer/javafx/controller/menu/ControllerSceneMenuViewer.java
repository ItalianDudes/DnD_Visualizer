package it.italiandudes.dnd_visualizer.javafx.controller.menu;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.javafx.controller.ControllerSceneElementEditor;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneElementEditor;
import it.italiandudes.idl.common.SQLiteHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;

public final class ControllerSceneMenuViewer {

    //Attributes
    @FXML
    private BorderPane mainPane;
    @FXML
    private VBox elementPane;
    private boolean startBound = true;
    private static String elementType = null;

    //Initialize
    @FXML
    private void initialize(){
        if(startBound) {
            AnchorPane.setTopAnchor(mainPane, 0.0);
            AnchorPane.setRightAnchor(mainPane, 0.0);
            AnchorPane.setLeftAnchor(mainPane, 0.0);
            AnchorPane.setBottomAnchor(mainPane, 0.0);
            startBound = false;
        }
        Service<Void> dbCaller = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String query = "SELECT * FROM "+ JFXDefs.MenuChoices.getDBTableNameFromChoiceName(elementType);
                        ResultSet set = SQLiteHandler.readDataFromDB(DnD_Visualizer.getDbConnection(), query);
                        while(set.next()) {
                            String elemName = set.getString(2);
                            Label elementLabel = new Label(elemName);
                            elementLabel.setOnMouseClicked(event -> {
                                DnD_Visualizer.getStage().hide();
                                Stage stage = new Stage();
                                ControllerSceneElementEditor.setElement(elementType, elemName, stage);
                                stage.setTitle("D&D Visualizer");
                                stage.getIcons().add(DnD_Visualizer.appImage);
                                stage.setScene(SceneElementEditor.getScene());
                                stage.setOnCloseRequest(closeEvent -> Platform.runLater(() -> {
                                    DnD_Visualizer.getStage().show();
                                    elementPane.getChildren().clear();
                                    initialize();
                                }));
                                stage.show();
                            });
                            Platform.runLater(() -> elementPane.getChildren().add(elementLabel));
                        }
                        return null;
                    }
                };
            }
        };
        dbCaller.start();
    }

    //EDT & Methods
    @FXML
    private void refreshFromButton(){
        elementPane.getChildren().clear();
        initialize();
    }
    public static void setElementType(String elementType){
        ControllerSceneMenuViewer.elementType = elementType;
    }

}
