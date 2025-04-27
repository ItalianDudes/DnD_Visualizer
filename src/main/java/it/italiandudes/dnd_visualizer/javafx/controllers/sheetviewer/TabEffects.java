package it.italiandudes.dnd_visualizer.javafx.controllers.sheetviewer;

import it.italiandudes.dnd_visualizer.data.effect.EffectPreview;
import it.italiandudes.dnd_visualizer.data.enums.EffectKnowledge;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.alerts.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.controllers.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneEffect;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class TabEffects {

    // Attributes
    private static String effectName = null;

    // Methods
    public static String getEffectName() {
        return effectName;
    }

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.tableColumnEffectID.setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.tableColumnEffectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        controller.tableColumnEffectDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        controller.tableColumnEffectIntensity.setCellValueFactory(new PropertyValueFactory<>("intensity"));
        controller.tableColumnEffectIsTreatable.setCellValueFactory(new PropertyValueFactory<>("isTreatable"));
        controller.tableColumnEffectIsCurable.setCellValueFactory(new PropertyValueFactory<>("isCurable"));
        controller.tableColumnEffectIsLethal.setCellValueFactory(new PropertyValueFactory<>("isLethal"));
        controller.tableColumnEffectIsActive.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        controller.tableViewEffects.setPlaceholder(new Label("Non ci sono effetti salvati."));
        searchEffect(controller);
    }

    // EDT
    public static void searchEffect(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        try {
                            String query;
                            PreparedStatement ps;

                            query = "SELECT id, name, duration, intensity, is_treatable, is_curable, is_lethal, is_active FROM effects WHERE name LIKE '%" + controller.textFieldEffectSearchBar.getText() + "%'" + (controller.checkBoxShowActiveEffects.isSelected() ? " AND is_active=1;" : ";");
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) {
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                    Client.setScene(SceneMainMenu.getScene());
                                });
                                return null;
                            }

                            ResultSet result = ps.executeQuery();
                            ArrayList<EffectPreview> resultList = new ArrayList<>();

                            while (result.next()) {
                                resultList.add(
                                        new EffectPreview(
                                                result.getInt("id"),
                                                result.getString("name"),
                                                result.getString("duration"),
                                                result.getInt("intensity"),
                                                EffectKnowledge.values()[result.getInt("is_treatable") + 1],
                                                EffectKnowledge.values()[result.getInt("is_curable") + 1],
                                                EffectKnowledge.values()[result.getInt("is_lethal") + 1],
                                                result.getInt("is_active") != 0
                                        ));
                            }

                            ps.close();
                            Platform.runLater(() -> controller.tableViewEffects.setItems(FXCollections.observableList(resultList)));
                        } catch (Exception e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            new ErrorAlert("ERRORE", "ERRORE DI CONNESSIONE", "Si e' verificato un errore durante la comunicazione con il database.");
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void deleteEffect(@NotNull final ControllerSceneSheetViewer controller) {
        EffectPreview effectPreview = controller.tableViewEffects.getSelectionModel().getSelectedItem();
        if (effectPreview == null) return;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        try {
                            PreparedStatement ps = DBManager.preparedStatement("DELETE FROM effects WHERE id=?;");
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            ps.setInt(1, effectPreview.getId());
                            ps.executeUpdate();
                            ps.close();
                            Platform.runLater(() -> {
                                searchEffect(controller);
                                TabEquipment.updateEquipmentProperties(controller);
                            });
                        } catch (SQLException e) {
                            Logger.log(e, Defs.LOGGER_CONTEXT);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Rimozione", "Si e' verificato un errore durante la rimozione dell'elemento."));
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void editEffect(@NotNull final ControllerSceneSheetViewer controller) {
        EffectPreview effectPreview = controller.tableViewEffects.getSelectionModel().getSelectedItem();
        if (effectPreview == null) return;
        effectName = effectPreview.getName();
        Stage popupStage = Client.initPopupStage(SceneEffect.getScene());
        popupStage.showAndWait();
        effectName = null;
        searchEffect(controller);
        TabEquipment.updateEquipmentProperties(controller);
    }
    public static void addEffect(@NotNull final ControllerSceneSheetViewer controller) {
        effectName = null;
        Stage popupStage = Client.initPopupStage(SceneEffect.getScene());
        popupStage.showAndWait();
        searchEffect(controller);
        TabEquipment.updateEquipmentProperties(controller);
    }
}
