package it.italiandudes.dnd_visualizer.javafx.controller;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneLoading;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneMenu;
import it.italiandudes.idl.common.FileHandler;
import it.italiandudes.idl.common.SQLiteHandler;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ControllerSceneStartup {

    //Attributes
    @FXML
    private TextField pathDBTextField;
    @FXML
    private Button dbChooserButton;

    //Initialize
    public void initialize() {
        ImageView fileChooserView = new ImageView(Objects.requireNonNull(getClass().getResource("/icon/file-explorer.png")).toString());
        fileChooserView.setFitWidth(dbChooserButton.getPrefWidth());
        fileChooserView.setFitHeight(dbChooserButton.getHeight());
        fileChooserView.setPreserveRatio(true);
        dbChooserButton.setGraphic(fileChooserView);
    }

    //Listeners
    @FXML
    private void openDBFileChooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il DB");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite3 Database", "*.db3"));
        fileChooser.setInitialDirectory(DnD_Visualizer.Defs.jarExecutablePath);
        File fileDB = fileChooser.showOpenDialog(dbChooserButton.getScene().getWindow());
        if(fileDB!=null)
            pathDBTextField.setText(fileDB.getAbsolutePath());
    }
    @FXML
    private void attemptConnectionToDB(ActionEvent event){

        Scene thisScene = pathDBTextField.getScene();
        DnD_Visualizer.getStage().setScene(SceneLoading.getScene());
        Service<Void> attemptDBConnectionThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String dbPath = pathDBTextField.getText();
                        if(dbPath==null || dbPath.equals("")){
                            Platform.runLater(() -> {
                                DnD_Visualizer.getStage().setScene(thisScene);
                                new ErrorAlert("ERRORE", "Errore inserimento DB", "Non e' stato selezionato alcun percorso! Inserire un percorso valido a un file valido");
                            });
                            return null;
                        }
                        File fileChecker = new File(dbPath);
                        if(!fileChecker.exists() || !fileChecker.isFile()){
                            Platform.runLater(() -> {
                                DnD_Visualizer.getStage().setScene(thisScene);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("ERRORE");
                                alert.setHeaderText("Errore inserimento DB");
                                alert.setContentText("Il percorso inserito non esiste oppure non e' un file! Inserire un percorso a un file valido");
                                alert.show();
                            });
                            return null;
                        }
                        if(!FileHandler.getFileExtension(dbPath).equals("db3")){
                            Platform.runLater(() -> {
                                DnD_Visualizer.getStage().setScene(thisScene);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("ERRORE");
                                alert.setHeaderText("Errore inserimento DB");
                                alert.setContentText("Il file inserito non e' di formato .db3! Inserire un percorso a un file valido");
                                alert.show();
                            });
                            return null;
                        }
                        Connection dbConnection = SQLiteHandler.openConnection(pathDBTextField.getText());

                        if(dbConnection==null){
                            Platform.runLater(() -> {
                                DnD_Visualizer.getStage().setScene(thisScene);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("ERRORE");
                                alert.setHeaderText("Errore connessione DB");
                                alert.setContentText("Si e' verificato un errore durante la connessione al database");
                                alert.show();
                            });
                            return null;
                        }

                        if(!DnD_Visualizer.setDbConnection(dbConnection)){
                            throw new RuntimeException("There is already an open connection with a database");
                        }
                        Platform.runLater(() -> DnD_Visualizer.getStage().setScene(SceneMenu.getScene()));
                        return null;
                    }
                };
            }
        };
        attemptDBConnectionThread.start();
    }

}
