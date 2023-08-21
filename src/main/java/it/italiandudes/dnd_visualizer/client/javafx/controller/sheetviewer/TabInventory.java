package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.client.javafx.scene.inventory.SceneInventoryItem;
import it.italiandudes.dnd_visualizer.data.ElementPreview;
import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class TabInventory {

    // Attributes
    private static String elementName = null;

    // Methods
    public static String getElementName() {
        return elementName;
    }

    // Old Values
    private static String oldValueMR = "0";
    private static String oldValueMA = "0";
    private static String oldValueME = "0";
    private static String oldValueMO = "0";
    private static String oldValueMP = "0";

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        onLostFocusFireActionEvent(controller);
        setOnChangeTriggers(controller);
        controller.spinnerMR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerMA.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerME.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerMO.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerMP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        controller.tableColumnRarity.setCellValueFactory(new PropertyValueFactory<>("rarity"));
        controller.tableColumnWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        controller.tableColumnCostMR.setCellValueFactory(new PropertyValueFactory<>("costCopper"));
        controller.comboBoxCategory.setItems(FXCollections.observableList(Category.categories));
        search(controller);
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.spinnerMR.getEditor().textProperty().addListener((observable -> validateMR(controller)));
        controller.spinnerMA.getEditor().textProperty().addListener((observable -> validateMA(controller)));
        controller.spinnerME.getEditor().textProperty().addListener((observable -> validateME(controller)));
        controller.spinnerMO.getEditor().textProperty().addListener((observable -> validateMO(controller)));
        controller.spinnerMP.getEditor().textProperty().addListener((observable -> validateMP(controller)));
    }

    // Lost Focus On Action Fire Event
    private static void onLostFocusFireActionEvent(@NotNull final ControllerSceneSheetViewer controller) {
        controller.spinnerMR.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateMR(controller);
        });
        controller.spinnerMA.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateMA(controller);
        });
        controller.spinnerME.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateME(controller);
        });
        controller.spinnerMO.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateMO(controller);
        });
        controller.spinnerMP.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateMP(controller);
        });
        // TODO: add eventual TextField that need to run their onClick method
    }

    // EDT
    public static void validateMR(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            if (Integer.parseInt(controller.spinnerMR.getEditor().getText()) < 0) throw new NumberFormatException();
            oldValueMR = controller.spinnerMR.getEditor().getText();
        } catch (NumberFormatException e) {
            controller.spinnerMR.getEditor().setText(oldValueMR);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete di rame devono essere un numero intero positivo");
        }
    }
    public static void validateMA(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            if (Integer.parseInt(controller.spinnerMA.getEditor().getText()) < 0) throw new NumberFormatException();
            oldValueMA = controller.spinnerMA.getEditor().getText();
        } catch (NumberFormatException e) {
            controller.spinnerMA.getEditor().setText(oldValueMA);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete d'argento devono essere un numero intero positivo");
        }
    }
    public static void validateME(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            if (Integer.parseInt(controller.spinnerME.getEditor().getText()) < 0) throw new NumberFormatException();
            oldValueME = controller.spinnerME.getEditor().getText();
        } catch (NumberFormatException e) {
            controller.spinnerME.getEditor().setText(oldValueME);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete di electrum devono essere un numero intero positivo");
        }
    }
    public static void validateMO(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            if (Integer.parseInt(controller.spinnerMO.getEditor().getText()) < 0) throw new NumberFormatException();
            oldValueMO = controller.spinnerMO.getEditor().getText();
        } catch (NumberFormatException e) {
            controller.spinnerMO.getEditor().setText(oldValueMO);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete d'oro devono essere un numero intero positivo");
        }
    }
    public static void validateMP(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            if (Integer.parseInt(controller.spinnerMP.getEditor().getText()) < 0) throw new NumberFormatException();
            oldValueMP = controller.spinnerMP.getEditor().getText();
        } catch (NumberFormatException e) {
            controller.spinnerMP.getEditor().setText(oldValueMP);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete di platino devono essere un numero intero positivo");
        }
    }
    public static void search(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            // TODO: search with searchBar content
                            String query;
                            PreparedStatement ps;
                            if (controller.comboBoxCategory.getSelectionModel().getSelectedItem()!=null) {
                                query = "SELECT id, name, category, rarity, weight, cost_copper FROM items WHERE category=?;";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) {
                                    Platform.runLater(() -> {
                                        new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                        Client.getStage().setScene(SceneMainMenu.getScene());
                                    });
                                    return null;
                                }
                                ps.setInt(1, controller.comboBoxCategory.getSelectionModel().getSelectedItem().getDatabaseValue());
                            } else {
                                query = "SELECT id, name, category, rarity, weight, cost_copper FROM items;";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) {
                                    Platform.runLater(() -> {
                                        new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                        Client.getStage().setScene(SceneMainMenu.getScene());
                                    });
                                    return null;
                                }
                            }

                            ResultSet result = ps.executeQuery();

                            ArrayList<ElementPreview> resultList = new ArrayList<>();

                            while (result.next()) {
                                resultList.add(
                                        new ElementPreview(
                                                result.getInt("id"),
                                                result.getString("name"),
                                                Category.values()[result.getInt("category")],
                                                Rarity.values()[result.getInt("rarity")],
                                                result.getDouble("weight"),
                                                result.getInt("cost_copper")
                                        ));
                            }

                            ps.close();
                            Platform.runLater(() -> controller.tableViewInventory.setItems(FXCollections.observableList(resultList)));
                            return null;
                        } catch (Exception e) {
                            Logger.log(e);
                            throw e;
                        }
                    }
                };
            }
        }.start();
    }
    public static void deleteElement(@NotNull final ControllerSceneSheetViewer controller) {
        ElementPreview element = controller.tableViewInventory.getSelectionModel().getSelectedItem();
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            String query = "DELETE FROM items WHERE id=?;";
                            PreparedStatement ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            ps.setInt(1, element.getId());
                            ps.executeUpdate();
                            ps.close();
                            Platform.runLater(() -> search(controller));
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
    public static Scene selectEquipmentScene() {
        if (elementName == null) return null;
        Scene scene;
        // TODO: implement selectEquipmentScene()
        //Equipment equipment = new Equipment(elementName);
        switch (elementName) {}
        return null;
    }
    public static void editElement(@NotNull final ControllerSceneSheetViewer controller) {
        ElementPreview element;
        try {
            element = controller.tableViewInventory.getSelectionModel().getSelectedItem();
        } catch (NullPointerException e) {
            return;
        }
        elementName = element.getName();
        Scene scene;
        switch (element.getCategory().getDatabaseValue()) {
            case 0: // Item
                scene = SceneInventoryItem.getScene();
                break;

            case 1: // Equipment
                scene = selectEquipmentScene();
                break;

            default: // Invalid
                new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria valida.");
                return;
        }
        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        elementName = null;
        search(controller);
    }
    public static void addElement(@NotNull final ControllerSceneSheetViewer controller) {
        elementName = null;
        Scene scene = SceneInventoryItem.getScene();
        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        search(controller);
    }
}
