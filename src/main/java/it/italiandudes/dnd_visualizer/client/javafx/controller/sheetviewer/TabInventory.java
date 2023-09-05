package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.client.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.client.javafx.scene.inventory.SceneInventoryArmor;
import it.italiandudes.dnd_visualizer.client.javafx.scene.inventory.SceneInventoryItem;
import it.italiandudes.dnd_visualizer.client.javafx.scene.inventory.SceneInventorySpell;
import it.italiandudes.dnd_visualizer.client.javafx.scene.inventory.SceneInventoryWeapon;
import it.italiandudes.dnd_visualizer.client.javafx.util.LoadCategory;
import it.italiandudes.dnd_visualizer.client.javafx.util.SheetDataHandler;
import it.italiandudes.dnd_visualizer.data.ElementPreview;
import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.EquipmentType;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import it.italiandudes.dnd_visualizer.data.item.Equipment;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.utils.Defs;
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
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public final class TabInventory {

    // Attributes
    private static String elementName = null;

    // Methods
    public static String getElementName() {
        return elementName;
    }

    // Old Values
    private static int oldValueMR = 0;
    private static int oldValueMA = 0;
    private static int oldValueME = 0;
    private static int oldValueMO = 0;
    private static int oldValueMP = 0;

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.spinnerMR.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerMA.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerME.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerMO.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerMP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.tableColumnInventoryID.setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.tableColumnInventoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
        controller.tableColumnInventoryRarity.setCellValueFactory(new PropertyValueFactory<>("rarity"));
        controller.tableColumnInventoryWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        controller.tableColumnInventoryCostMR.setCellValueFactory(new PropertyValueFactory<>("costCopper"));
        controller.tableColumnInventoryQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        controller.comboBoxCategory.setItems(FXCollections.observableList(Category.categories));
        controller.comboBoxEquipmentType.setItems(FXCollections.observableList(EquipmentType.types));
        readTabData(controller);
        setOnChangeTriggers(controller);
        onLostFocusFireActionEvent(controller);
        search(controller);
    }

    // Data Reader
    private static void readTabData(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String copperCoins = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabInventory.COPPER_COINS);
                        String silverCoins = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabInventory.SILVER_COINS);
                        String electrumCoins = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabInventory.ELECTRUM_COINS);
                        String goldCoins = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabInventory.GOLD_COINS);
                        String platinumCoins = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabInventory.PLATINUM_COINS);
                        Platform.runLater(() -> {
                            if (copperCoins != null) controller.spinnerMR.getValueFactory().setValue(Integer.parseInt(copperCoins));
                            if (silverCoins != null) controller.spinnerMA.getValueFactory().setValue(Integer.parseInt(silverCoins));
                            if (electrumCoins != null) controller.spinnerME.getValueFactory().setValue(Integer.parseInt(electrumCoins));
                            if (goldCoins != null) controller.spinnerMO.getValueFactory().setValue(Integer.parseInt(goldCoins));
                            if (platinumCoins != null) controller.spinnerMP.getValueFactory().setValue(Integer.parseInt(platinumCoins));
                            updateLoad(controller);
                        });
                        return null;
                    }
                };
            }
        }.start();
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
    }

    // Weight Handler
    public static void updateLoad(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        if (!(boolean) Client.getSettings().get("enableLoad")) {
                            return null;
                        }
                        String query;
                        PreparedStatement ps = null;
                        ResultSet result;
                        try {
                            double inventoryWeight = 0;
                            double totalWeight;
                            if ((boolean) Client.getSettings().get("enablePassiveLoad")) {
                                if ((boolean) Client.getSettings().get("coinsIncreaseLoad")) {
                                    inventoryWeight += (controller.spinnerMR.getValueFactory().getValue()/Defs.Load.COIN_LOAD_DIVISOR) + (controller.spinnerMA.getValueFactory().getValue()/Defs.Load.COIN_LOAD_DIVISOR) + (controller.spinnerME.getValueFactory().getValue()/Defs.Load.COIN_LOAD_DIVISOR) + (controller.spinnerMO.getValueFactory().getValue()/Defs.Load.COIN_LOAD_DIVISOR) + (controller.spinnerMP.getValueFactory().getValue()/Defs.Load.COIN_LOAD_DIVISOR);
                                }
                                query = "SELECT weight, quantity FROM items WHERE quantity>0 AND weight>0;";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) throw new SQLException("The database connection doesn't exist");
                                result = ps.executeQuery();
                                while (result.next()) {
                                    inventoryWeight += (result.getDouble("weight")*result.getInt("quantity"));
                                }
                                ps.close();
                                totalWeight = controller.spinnerStrength.getValue()*Defs.Load.TOTAL_PASSIVE_LOAD_MULTIPLIER;
                            } else {
                                query = "SELECT i.weight AS weight FROM items AS i JOIN equipments AS e JOIN armors AS a ON i.id = e.item_id AND e.id = a.equipment_id WHERE a.is_equipped=1;";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) throw new SQLException("The database connection doesn't exist");
                                result = ps.executeQuery();
                                while (result.next()) {
                                    inventoryWeight += result.getDouble("weight");
                                }
                                ps.close();
                                totalWeight = controller.spinnerStrength.getValue()*Defs.Load.TOTAL_ACTIVE_LOAD_MULTIPLIER;
                            }
                            double loadPerc = (inventoryWeight/totalWeight)*100;
                            String status;
                            if (loadPerc >= LoadCategory.LIGHT.getLoadThresholdMinPercentage() && loadPerc < LoadCategory.LIGHT.getLoadThresholdMaxPercentage()) {
                                status = LoadCategory.LIGHT.getLoadIdentifier();
                            } else if (loadPerc >= LoadCategory.NORMAL.getLoadThresholdMinPercentage() && loadPerc < LoadCategory.NORMAL.getLoadThresholdMaxPercentage()) {
                                status = LoadCategory.NORMAL.getLoadIdentifier();
                            } else if (loadPerc >= LoadCategory.HEAVY.getLoadThresholdMinPercentage() && loadPerc < LoadCategory.HEAVY.getLoadThresholdMaxPercentage()) {
                                status = LoadCategory.HEAVY.getLoadIdentifier();
                            } else if (loadPerc > LoadCategory.OVERLOAD.getLoadThresholdMinPercentage()) {
                                status = LoadCategory.OVERLOAD.getLoadIdentifier();
                            } else {
                                status = LoadCategory.ERROR.getLoadIdentifier();
                            }
                            double finalInventoryWeight = inventoryWeight;
                            double finalTotalWeight = totalWeight;
                            Platform.runLater(() -> {
                                DecimalFormat format = new DecimalFormat("#.##");
                                controller.labelLoadCurrent.setText(format.format(finalInventoryWeight));
                                controller.labelLoadTotal.setText(format.format(finalTotalWeight));
                                controller.labelLoadPercentage.setText(format.format(loadPerc) + "%");
                                controller.labelLoadStatus.setText(status);
                            });
                        } catch (SQLException e) {
                            try {
                                if (ps != null) ps.close();
                            } catch (SQLException ignored) {}
                            Logger.log(e);
                            new ErrorAlert("ERRORE", "ERRORE DI DATABASE", "Si e' verificato un errore durante la comunicazione con il database.");
                        }
                        return null;
                    }
                };
            }
        }.start();
    }

    // EDT
    public static void validateMR(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            int qty = Integer.parseInt(controller.spinnerMR.getEditor().getText());
            if (qty < 0) throw new NumberFormatException();
            oldValueMR = qty;
            controller.spinnerMR.getValueFactory().setValue(qty);
            SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabInventory.COPPER_COINS, String.valueOf(qty));
            updateLoad(controller);
        } catch (NumberFormatException e) {
            controller.spinnerMR.getValueFactory().setValue(oldValueMR);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete di rame devono essere un numero intero positivo");
        }
    }
    public static void validateMA(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            int qty = Integer.parseInt(controller.spinnerMA.getEditor().getText());
            if (qty < 0) throw new NumberFormatException();
            oldValueMA = qty;
            controller.spinnerMA.getValueFactory().setValue(qty);
            SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabInventory.SILVER_COINS, String.valueOf(qty));
            updateLoad(controller);
        } catch (NumberFormatException e) {
            controller.spinnerMA.getValueFactory().setValue(oldValueMA);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete d'argento devono essere un numero intero positivo");
        }
    }
    public static void validateME(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            int qty = Integer.parseInt(controller.spinnerME.getEditor().getText());
            if (qty < 0) throw new NumberFormatException();
            oldValueME = qty;
            controller.spinnerME.getValueFactory().setValue(qty);
            SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabInventory.ELECTRUM_COINS, String.valueOf(qty));
            updateLoad(controller);
        } catch (NumberFormatException e) {
            controller.spinnerME.getValueFactory().setValue(oldValueME);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete di electrum devono essere un numero intero positivo");
        }
    }
    public static void validateMO(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            int qty = Integer.parseInt(controller.spinnerMO.getEditor().getText());
            if (qty < 0) throw new NumberFormatException();
            oldValueMO = qty;
            controller.spinnerMO.getValueFactory().setValue(qty);
            SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabInventory.GOLD_COINS, String.valueOf(qty));
            updateLoad(controller);
        } catch (NumberFormatException e) {
            controller.spinnerMO.getValueFactory().setValue(oldValueMO);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete d'oro devono essere un numero intero positivo");
        }
    }
    public static void validateMP(@NotNull final ControllerSceneSheetViewer controller) {
        try {
            int qty = Integer.parseInt(controller.spinnerMP.getEditor().getText());
            if (qty < 0) throw new NumberFormatException();
            oldValueMP = qty;
            controller.spinnerMP.getValueFactory().setValue(qty);
            SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabInventory.PLATINUM_COINS, String.valueOf(qty));
            updateLoad(controller);
        } catch (NumberFormatException e) {
            controller.spinnerMP.getValueFactory().setValue(oldValueMP);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Le monete di platino devono essere un numero intero positivo");
        }
    }
    public static void search(@NotNull final ControllerSceneSheetViewer controller) {
        Category selectedCategory = controller.comboBoxCategory.getSelectionModel().getSelectedItem();
        controller.comboBoxEquipmentType.setDisable(selectedCategory == null || !selectedCategory.equals(Category.EQUIPMENT));
        EquipmentType equipmentType = controller.comboBoxEquipmentType.getSelectionModel().getSelectedItem();
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            String query;
                            PreparedStatement ps;
                            if (selectedCategory != null) {
                                if (selectedCategory.equals(Category.EQUIPMENT) && equipmentType != null) {
                                    query = "SELECT i.id AS id, i.name AS name, i.category AS category, i.rarity AS rarity, i.weight AS weight, i.cost_copper AS cost_copper, i.quantity AS quantity FROM items AS i JOIN equipments AS e ON i.id = e.item_id WHERE i.name LIKE '%" + controller.textFieldSearchBar.getText() + "%' AND i.category=? AND e.type=?;";
                                    ps = DBManager.preparedStatement(query);
                                    if (ps == null) {
                                        Platform.runLater(() -> {
                                            new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                            Client.getStage().setScene(SceneMainMenu.getScene());
                                        });
                                        return null;
                                    }
                                    ps.setInt(1, selectedCategory.getDatabaseValue());
                                    ps.setInt(2, equipmentType.getDatabaseValue());
                                } else {
                                    query = "SELECT id, name, category, rarity, weight, cost_copper, quantity FROM items WHERE name LIKE '%" + controller.textFieldSearchBar.getText() + "%' AND category=?;";
                                    ps = DBManager.preparedStatement(query);
                                    if (ps == null) {
                                        Platform.runLater(() -> {
                                            new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                            Client.getStage().setScene(SceneMainMenu.getScene());
                                        });
                                        return null;
                                    }
                                    ps.setInt(1, selectedCategory.getDatabaseValue());
                                }
                            } else {
                                query = "SELECT id, name, category, rarity, weight, cost_copper, quantity FROM items WHERE name LIKE '%"+controller.textFieldSearchBar.getText()+"%';";
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
                                                result.getInt("cost_copper"),
                                                result.getInt("quantity")
                                        ));
                            }

                            ps.close();
                            Platform.runLater(() -> controller.tableViewInventory.setItems(FXCollections.observableList(resultList)));
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
    public static void resetSearchBarAndCategory(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textFieldSearchBar.setText("");
        controller.comboBoxCategory.getSelectionModel().clearSelection();
        controller.comboBoxEquipmentType.getSelectionModel().clearSelection();
        controller.comboBoxEquipmentType.setDisable(true);
        search(controller);
    }
    public static void deleteElement(@NotNull final ControllerSceneSheetViewer controller) {
        ElementPreview element = controller.tableViewInventory.getSelectionModel().getSelectedItem();
        if (element == null) return;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            PreparedStatement ps = DBManager.preparedStatement("DELETE FROM items WHERE id=?;");
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            ps.setInt(1, element.getId());
                            ps.executeUpdate();
                            ps.close();
                            Platform.runLater(() -> {
                                search(controller);
                                updateLoad(controller);
                                TabSpells.updateListViews(controller);
                                TabEquipment.reloadEquipment(controller);
                            });
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
    public static Scene selectEquipmentScene(@NotNull final ControllerSceneSheetViewer controller, @Nullable final ElementPreview element) {
        EquipmentType equipmentType;
        if (element != null) {
            try {
                equipmentType = new Equipment(element.getName()).getType();
            } catch (SQLException e) {
                new ErrorAlert("ERRORE", "ERRORE COL DATABASE", "Si e' verificato un errore durante l'interrogazione del database.");
                return null;
            }
        } else {
            equipmentType = controller.comboBoxEquipmentType.getSelectionModel().getSelectedItem();
            if (equipmentType == null) {
                new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Per aggiungere dell'equipaggiamento devi prima selezionare il tipo.");
                return null;
            }
        }
        switch (equipmentType) {
            case ARMOR:
                return SceneInventoryArmor.getScene();
            case WEAPON:
                return SceneInventoryWeapon.getScene();
            default: // Invalid
                new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria equipaggiamento valida.");
                return null;
        }
    }
    public static void editElement(@NotNull final ControllerSceneSheetViewer controller) {
        ElementPreview element = controller.tableViewInventory.getSelectionModel().getSelectedItem();
        if (element == null) return;
        elementName = element.getName();
        Scene scene;
        switch (element.getCategory()) {
            case ITEM:
                scene = SceneInventoryItem.getScene();
                break;

            case EQUIPMENT:
                scene = selectEquipmentScene(controller, element);
                if (scene == null) return;
                break;

            case SPELL:
                scene = SceneInventorySpell.getScene();
                break;

            default: // Invalid
                new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria valida o non e' stata ancora implementata nell'applicazione.");
                return;
        }
        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        elementName = null;
        search(controller);
        updateLoad(controller);
        TabSpells.updateListViews(controller);
        TabEquipment.reloadEquipment(controller);
    }
    public static void addElement(@NotNull final ControllerSceneSheetViewer controller) {
        elementName = null;
        Scene scene;
        if (controller.comboBoxCategory.getSelectionModel().isEmpty()) {
            new ErrorAlert("ERRORE", "ERRORE DI PROCEDURA", "Per aggiungere un elemento e' necessario prima selezionare una categoria.");
            return;
        }
        Category category = controller.comboBoxCategory.getSelectionModel().getSelectedItem();
        switch (category) {
            case ITEM:
                scene = SceneInventoryItem.getScene();
                break;

            case EQUIPMENT:
                scene = selectEquipmentScene(controller, null);
                if (scene == null) return;
                break;

            case SPELL:
                scene = SceneInventorySpell.getScene();
                break;

            default: // Invalid
                new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria valida o non e' stata ancora implementata nell'applicazione.");
                return;
        }
        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        search(controller);
        updateLoad(controller);
        TabSpells.updateListViews(controller);
        TabEquipment.reloadEquipment(controller);
    }
}
