package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneLoading;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneSheetViewer;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class ControllerSceneMainMenu {

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
    }

    // EDT
    @FXML
    private void newSheet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create the Sheet");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Sheet", "*." + Defs.Resources.SHEET_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File fileSheet;
        try {
            fileSheet = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileSheet = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        }
        if (fileSheet == null) return;

        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        Service<Void> sheetCreatorService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            DBManager.createSheet(finalSheetDB.getAbsolutePath());
                        } catch (SQLException e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "I/O Error", "An error has occurred during sheet opening");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        Platform.runLater(() -> Client.getStage().setScene(SceneSheetViewer.getScene()));
                        return null;
                    }
                };
            }
        };

        sheetCreatorService.start();
    }
    @FXML
    private void openSheet() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the Sheet");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Sheet", "*." + Defs.Resources.SHEET_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File fileSheet;
        try {
            fileSheet = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileSheet = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        }
        if (fileSheet == null) return;

        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());

        File finalSheetDB = fileSheet;
        Service<Void> sheetOpenerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            DBManager.connectToDB(finalSheetDB);
                        } catch (IOException | SQLException e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERROR", "I/O Error", "An error has occurred during sheet opening");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        Platform.runLater(() -> Client.getStage().setScene(SceneSheetViewer.getScene()));
                        return null;
                    }
                };
            }
        };

        sheetOpenerService.start();
    }
}
