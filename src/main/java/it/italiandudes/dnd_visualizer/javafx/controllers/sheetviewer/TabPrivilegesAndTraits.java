package it.italiandudes.dnd_visualizer.javafx.controllers.sheetviewer;

import it.italiandudes.dnd_visualizer.data.PrivilegeOrTrait;
import it.italiandudes.dnd_visualizer.data.enums.PrivilegeType;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.components.SceneController;
import it.italiandudes.dnd_visualizer.javafx.controllers.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.javafx.scene.ScenePrivilegeOrTrait;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class TabPrivilegesAndTraits {

    // Attributes
    private static String elementName = null;

    // Methods
    public static String getPrivilegeName() {
        return elementName;
    }

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.comboBoxPrivilegesAndTraitsType.setItems(FXCollections.observableList(PrivilegeType.privilege_types));
        searchPrivilegesAndTraits(controller);
    }

    // Data Reader
    public static void searchPrivilegesAndTraits(@NotNull final ControllerSceneSheetViewer controller) {
        PrivilegeType selectedType = controller.comboBoxPrivilegesAndTraitsType.getSelectionModel().getSelectedItem();
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            String query;
                            PreparedStatement ps;
                            query = "SELECT name FROM privileges_and_traits";
                            if (selectedType != null) {
                                query += " WHERE type=?;";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) {
                                    Platform.runLater(() -> {
                                        new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                        Client.setScene(SceneMainMenu.getScene());
                                    });
                                    return null;
                                }
                                ps.setInt(1, selectedType.getDatabaseValue());
                            } else {
                                query += ";";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) {
                                    Platform.runLater(() -> {
                                        new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                        Client.setScene(SceneMainMenu.getScene());
                                    });
                                    return null;
                                }
                            }
                            ResultSet result = ps.executeQuery();
                            ArrayList<PrivilegeOrTrait> resultList = new ArrayList<>();
                            while (result.next()) {
                                resultList.add(new PrivilegeOrTrait(result.getString("name")));
                            }
                            ps.close();
                            Platform.runLater(() -> controller.listViewPrivilegesAndTraits.setItems(FXCollections.observableList(resultList)));
                        } catch (Exception e) {
                            Logger.log(e);
                            new ErrorAlert("ERRORE", "ERRORE DI CONNESSIONE", "Si e' verificato un errore durante la comunicazione con il database.");
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void resetPrivilegesAndTraitsSearchBar(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textFieldPrivilegesAndTraitsSearchBar.setText("");
        controller.comboBoxPrivilegesAndTraitsType.getSelectionModel().clearSelection();
        searchPrivilegesAndTraits(controller);
    }
    public static void deletePrivilegesAndTraits(@NotNull final ControllerSceneSheetViewer controller) {
        PrivilegeOrTrait element = controller.listViewPrivilegesAndTraits.getSelectionModel().getSelectedItem();
        if (element == null) return;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            PreparedStatement ps = DBManager.preparedStatement("DELETE FROM privileges_and_traits WHERE id=?;");
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            ps.setInt(1, element.getId());
                            ps.executeUpdate();
                            ps.close();
                            Platform.runLater(() -> searchPrivilegesAndTraits(controller));
                        } catch (SQLException e) {
                            Logger.log(e);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Rimozione", "Si e' verificato un errore durante la rimozione dell'elemento."));
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void editPrivilegesAndTraits(@NotNull final ControllerSceneSheetViewer controller) {
        PrivilegeOrTrait element = controller.listViewPrivilegesAndTraits.getSelectionModel().getSelectedItem();
        if (element == null) return;
        elementName = element.getName();
        SceneController scene = ScenePrivilegeOrTrait.getScene();
        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        elementName = null;
        searchPrivilegesAndTraits(controller);
    }
    public static void addPrivilegesAndTraits(@NotNull final ControllerSceneSheetViewer controller) {
        elementName = null;
        SceneController scene = ScenePrivilegeOrTrait.getScene();
        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        searchPrivilegesAndTraits(controller);
    }
}
