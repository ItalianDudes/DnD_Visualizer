package it.italiandudes.dnd_visualizer.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.javafx.Client;
import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneLoading;
import it.italiandudes.dnd_visualizer.javafx.scene.SceneMainMenu;
import it.italiandudes.dnd_visualizer.javafx.scene.inventory.*;
import it.italiandudes.dnd_visualizer.javafx.util.LoadCategory;
import it.italiandudes.dnd_visualizer.javafx.util.SheetDataHandler;
import it.italiandudes.dnd_visualizer.javafx.util.UIElementConfigurator;
import it.italiandudes.dnd_visualizer.data.ElementPreview;
import it.italiandudes.dnd_visualizer.data.bags.DnDBag;
import it.italiandudes.dnd_visualizer.data.bags.DnDBags;
import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.EquipmentType;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import it.italiandudes.dnd_visualizer.data.enums.SerializerType;
import it.italiandudes.dnd_visualizer.data.item.*;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.interfaces.ISerializable;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public final class TabInventory {

    // Attributes
    private static String elementName = null;
    private static JSONObject elementStructure = null;

    // Methods
    public static String getElementName() {
        return elementName;
    }
    public static JSONObject getElementStructure() {
        return elementStructure;
    }

    // Load Effect Registries
    private static int loadEffect = 0;
    private static double loadPercentageEffect = 0;

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
        controller.spinnerMR.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerMA.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerME.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerMO.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerMP.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.tableColumnInventoryID.setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.tableColumnInventoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
        controller.tableColumnInventoryRarity.setCellValueFactory(new PropertyValueFactory<>("rarity"));
        controller.tableColumnInventoryWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        controller.tableColumnInventoryCostMR.setCellValueFactory(new PropertyValueFactory<>("costCopper"));
        controller.tableColumnInventoryQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        controller.tableViewInventory.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        controller.tableViewInventory.setPlaceholder(new Label("L'inventario e' vuoto."));
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
        controller.tableViewInventory.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) controller.tableViewInventory.getSelectionModel().clearSelection();
        });
    }

    // Weight Handler
    public static void updateMaxLoad(@NotNull final ControllerSceneSheetViewer controller, final int loadEffect, final double loadPercentageEffect) {
        TabInventory.loadEffect = loadEffect;
        TabInventory.loadPercentageEffect = loadPercentageEffect;
        updateLoad(controller);
    }
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
                                query = "SELECT i.weight AS weight FROM items AS i JOIN equipments AS e ON i.id = e.item_id WHERE e.is_equipped=1;";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) throw new SQLException("The database connection doesn't exist");
                                result = ps.executeQuery();
                                while (result.next()) {
                                    inventoryWeight += result.getDouble("weight");
                                }
                                ps.close();
                                totalWeight = controller.spinnerStrength.getValue()*Defs.Load.TOTAL_ACTIVE_LOAD_MULTIPLIER;
                            }
                            totalWeight += loadEffect;
                            totalWeight += (totalWeight * loadPercentageEffect) / 100;
                            totalWeight = totalWeight>=0?totalWeight:0;
                            double loadPerc = (inventoryWeight/totalWeight)*100;
                            String status;
                            if (loadPerc >= LoadCategory.LIGHT.getLoadThresholdMinPercentage() && loadPerc < LoadCategory.LIGHT.getLoadThresholdMaxPercentage()) {
                                status = LoadCategory.LIGHT.getLoadIdentifier();
                            } else if (loadPerc >= LoadCategory.NORMAL.getLoadThresholdMinPercentage() && loadPerc < LoadCategory.NORMAL.getLoadThresholdMaxPercentage()) {
                                status = LoadCategory.NORMAL.getLoadIdentifier();
                            } else if (loadPerc >= LoadCategory.HEAVY.getLoadThresholdMinPercentage() && loadPerc < LoadCategory.HEAVY.getLoadThresholdMaxPercentage()) {
                                status = LoadCategory.HEAVY.getLoadIdentifier();
                            } else if (loadPerc >= LoadCategory.OVERLOAD.getLoadThresholdMinPercentage()) {
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
                                    query = "SELECT i.id AS id, i.name AS name, i.category AS category, i.rarity AS rarity, i.weight AS weight, i.cost_copper AS cost_copper, i.quantity AS quantity FROM items AS i JOIN equipments AS e ON i.id = e.item_id WHERE i.name LIKE '%" + controller.textFieldSearchBar.getText() + "%' AND i.category=? AND e.type=?" + (controller.checkBoxShowOwned.isSelected()?" AND i.quantity>0;":";");
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
                                    query = "SELECT id, name, category, rarity, weight, cost_copper, quantity FROM items WHERE name LIKE '%" + controller.textFieldSearchBar.getText() + "%' AND category=?" + (controller.checkBoxShowOwned.isSelected()?" AND quantity>0;":";");
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
                                query = "SELECT id, name, category, rarity, weight, cost_copper, quantity FROM items WHERE name LIKE '%"+controller.textFieldSearchBar.getText()+"%'" + (controller.checkBoxShowOwned.isSelected()?" AND quantity>0;":";");
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

    // Importers & Exporters
    public static void importElementFromFile(@NotNull final ControllerSceneSheetViewer controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importazione di un Elemento");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Element", "*."+Defs.Resources.ELEMENT_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File elementPath;
        try {
            elementPath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            elementPath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        }
        if (elementPath != null) {
            File finalElementPath = elementPath;
            new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() {
                            if (!finalElementPath.exists() || !finalElementPath.isFile()) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di I/O", "Il percorso selezionato non porta a un file esistente o leggibile."));
                                return null;
                            }
                            Scanner reader;
                            try {
                                reader = new Scanner(finalElementPath);
                            } catch (FileNotFoundException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di I/O", "Il percorso selezionato non porta a un file esistente o leggibile."));
                                return null;
                            }
                            StringBuilder elementCodeBuilder = new StringBuilder();
                            while (reader.hasNext()) {
                                elementCodeBuilder.append(reader.nextLine());
                                if (reader.hasNext()) elementCodeBuilder.append('\n');
                            }
                            reader.close();
                            Platform.runLater(() -> importElement(controller, elementCodeBuilder.toString()));
                            return null;
                        }
                    };
                }
            }.start();
        }
    }
    public static void importElementFromElementCode(@NotNull final ControllerSceneSheetViewer controller) {
        String elementCode = controller.textFieldElementCode.getText();
        controller.textFieldElementCode.setText("");
        if (elementCode == null || elementCode.replace(" ", "").isEmpty()) return;
        importElement(controller, elementCode);
    }
    private static void importElement(@NotNull final ControllerSceneSheetViewer controller, @NotNull final String elementCode) {
        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            String jsonString = new String(Base64.getDecoder().decode(elementCode), StandardCharsets.UTF_8);
                            JSONObject element = new JSONObject(jsonString);
                            String dbVersion = null;
                            try {
                                dbVersion = element.getString(ISerializable.DB_VERSION);
                                if (!dbVersion.equals(Defs.SHEET_DB_VERSION)) throw new JSONException("DB Version Mismatch");
                            } catch (JSONException e) {
                                String supportedVersion = Defs.SHEET_DB_VERSION;
                                String inventoryVersion = (dbVersion!=null?dbVersion:"NA");
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di Importazione", "La versione di database dell'elemento non e' supportata.\nVersione Supportata: "+supportedVersion+"\nVersione Elemento: "+inventoryVersion);
                                    Client.getStage().setScene(thisScene);
                                });
                                return null;
                            }
                            Integer serializerID = (Integer) element.get(ISerializable.SERIALIZER_ID);
                            if (serializerID == null || serializerID < 0 || serializerID >= SerializerType.values().length) throw new IllegalArgumentException("The serializerID must be an non null integer withing SerializerTypes array bounds!");
                            SerializerType serializerType = SerializerType.values()[serializerID];
                            if (serializerType == SerializerType.INVENTORY) {
                                JSONArray elements = element.getJSONArray("elements");
                                int errored = 0;
                                int success = 0;
                                for (int i = 0; i < elements.length(); i++) {
                                    try {
                                        JSONObject singleElement = elements.getJSONObject(i);
                                        switch (SerializerType.values()[singleElement.getInt(ISerializable.SERIALIZER_ID)]) {
                                            case ITEM:
                                                new Item(singleElement).saveIntoDatabase(null);
                                                break;
                                            case ADDON:
                                                new Addon(singleElement).saveIntoDatabase(null);
                                                break;
                                            case ARMOR:
                                                new Armor(singleElement).saveIntoDatabase(null);
                                                break;
                                            case WEAPON:
                                                new Weapon(singleElement).saveIntoDatabase(null);
                                                break;
                                            case SPELL:
                                                new Spell(singleElement).saveIntoDatabase(null);
                                                break;
                                        }
                                        success++;
                                    } catch (SQLException | JSONException e) {
                                        errored++;
                                        Logger.log(e);
                                    }
                                }
                                int finalSuccess = success;
                                int finalErrored = errored;
                                Platform.runLater(() -> {
                                    new InformationAlert("IMPORTAZIONE COMPLETATA", "Importazione Inventario", "Importazione completata!\nSuccessi: "+ finalSuccess +"\nFallimenti: "+ finalErrored);
                                    Client.getStage().setScene(thisScene);
                                    search(controller);
                                    updateLoad(controller);
                                    TabSpells.updateListViews(controller);
                                    TabEquipment.reloadEquipment(controller);
                                });
                            } else {
                                elementStructure = element;
                                Platform.runLater(() -> {
                                    Scene newScene = null;
                                    switch (serializerType) {
                                        case ITEM:
                                            newScene = SceneInventoryItem.getScene();
                                            break;

                                        case ARMOR:
                                            newScene = SceneInventoryArmor.getScene();
                                            break;

                                        case ADDON:
                                            newScene = SceneInventoryAddon.getScene();
                                            break;

                                        case WEAPON:
                                            newScene = SceneInventoryWeapon.getScene();
                                            break;

                                        case SPELL:
                                            newScene = SceneInventorySpell.getScene();
                                            break;

                                        default:
                                            elementStructure = null;
                                            new ErrorAlert("ERRORE", "Errore di Importazione", "Struttura dati non riconosciuta, importazione fallita.");
                                            Client.getStage().setScene(thisScene);
                                            break;
                                    }
                                    Client.getStage().setScene(thisScene);
                                    Stage popupScene = Client.initPopupStage(newScene);
                                    popupScene.showAndWait();
                                    elementStructure = null;
                                    search(controller);
                                    updateLoad(controller);
                                    TabSpells.updateListViews(controller);
                                    TabEquipment.reloadEquipment(controller);
                                });
                            }
                        } catch (IllegalArgumentException | JSONException e) {
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Importazione", "Il codice elemento non e' valido, inserire un codice valido.");
                                Client.getStage().setScene(thisScene);
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public static void importInventory(@NotNull final ControllerSceneSheetViewer controller) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importazione dell'Inventario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Inventory", "*."+Defs.Resources.INVENTORY_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File invPath;
        try {
            invPath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            invPath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        }
        if (invPath!=null) {
            Scene thisScene = Client.getStage().getScene();
            Client.getStage().setScene(SceneLoading.getScene());
            File finalInvPath = invPath;
            new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() {
                            StringBuilder codeBuilder = new StringBuilder();
                            try {
                                Scanner invReader = new Scanner(finalInvPath);
                                while (invReader.hasNext()) {
                                    codeBuilder.append(invReader.nextLine());
                                    if (invReader.hasNext()) codeBuilder.append('\n');
                                }
                                invReader.close();
                            } catch (FileNotFoundException e) {
                                Logger.log(e);
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di I/O", "Il file specificato non esiste.");
                                    Client.getStage().setScene(thisScene);
                                });
                                return null;
                            }
                            try {
                                String elementCode = codeBuilder.toString();
                                String jsonString = new String(Base64.getDecoder().decode(elementCode), StandardCharsets.UTF_8);
                                JSONObject element = new JSONObject(jsonString);
                                String dbVersion = null;
                                try {
                                    dbVersion = element.getString(ISerializable.DB_VERSION);
                                    if (!dbVersion.equals(Defs.SHEET_DB_VERSION)) throw new JSONException("DB Version Mismatch");
                                } catch (JSONException e) {
                                    String supportedVersion = Defs.SHEET_DB_VERSION;
                                    String inventoryVersion = (dbVersion!=null?dbVersion:"NA");
                                    Platform.runLater(() -> {
                                        new ErrorAlert("ERRORE", "Errore di Importazione", "La versione di database dell'inventario non e' supportata.\nVersione Supportata: "+supportedVersion+"\nVersione Inventario: "+inventoryVersion);
                                        Client.getStage().setScene(thisScene);
                                    });
                                    return null;
                                }
                                Integer serializerID = (Integer) element.get(ISerializable.SERIALIZER_ID);
                                if (serializerID == null || serializerID < 0 || serializerID >= SerializerType.values().length)
                                    throw new IllegalArgumentException("The serializerID must be an non null integer withing SerializerTypes array bounds!");
                                SerializerType serializerType = SerializerType.values()[serializerID];
                                if (serializerType == SerializerType.INVENTORY) {
                                    JSONArray elements = element.getJSONArray("elements");
                                    int errored = 0;
                                    int success = 0;
                                    for (int i = 0; i < elements.length(); i++) {
                                        try {
                                            JSONObject singleElement = elements.getJSONObject(i);
                                            switch (SerializerType.values()[singleElement.getInt(ISerializable.SERIALIZER_ID)]) {
                                                case ITEM:
                                                    new Item(singleElement).saveIntoDatabase(null);
                                                    break;
                                                case ADDON:
                                                    new Addon(singleElement).saveIntoDatabase(null);
                                                    break;
                                                case ARMOR:
                                                    new Armor(singleElement).saveIntoDatabase(null);
                                                    break;
                                                case WEAPON:
                                                    new Weapon(singleElement).saveIntoDatabase(null);
                                                    break;
                                                case SPELL:
                                                    new Spell(singleElement).saveIntoDatabase(null);
                                                    break;
                                            }
                                            success++;
                                        } catch (SQLException | JSONException e) {
                                            errored++;
                                            Logger.log(e);
                                        }
                                    }
                                    int finalSuccess = success;
                                    int finalErrored = errored;
                                    Platform.runLater(() -> {
                                        new InformationAlert("IMPORTAZIONE COMPLETATA", "Importazione Inventario", "Importazione completata!\nSuccessi: "+ finalSuccess +"\nFallimenti: "+ finalErrored);
                                        Client.getStage().setScene(thisScene);
                                        search(controller);
                                        updateLoad(controller);
                                        TabSpells.updateListViews(controller);
                                        TabEquipment.reloadEquipment(controller);
                                    });
                                } else {
                                    Platform.runLater(() -> {
                                        new ErrorAlert("ERRORE", "Errore di Procedura", "Il file contiene del codice elemento, non del codice inventario.");
                                        Client.getStage().setScene(thisScene);
                                    });
                                }
                            } catch (IllegalArgumentException | JSONException e) {
                                Logger.log(e);
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di Importazione", "Il codice di importazione dell'inventario contenuto nel file non e' valido.");
                                    Client.getStage().setScene(thisScene);
                                });
                            }
                            return null;
                        }
                    };
                }
            }.start();
        }
    }
    public static void exportInventory() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Esportazione dell'Inventario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DND5E Inventory", "*."+Defs.Resources.INVENTORY_EXTENSION));
        fileChooser.setInitialDirectory(new File(Defs.JAR_POSITION).getParentFile());
        File destPath;
        try {
            destPath = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            destPath = fileChooser.showSaveDialog(Client.getStage().getScene().getWindow());
        }
        if (destPath!=null) {
            File finalDestPath = destPath;
            new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() {
                            String query;
                            PreparedStatement ps = null;
                            ResultSet result;

                            JSONObject inventory = new JSONObject();
                            inventory.put(ISerializable.SERIALIZER_ID, SerializerType.INVENTORY.ordinal());
                            inventory.put(ISerializable.DB_VERSION, Defs.SHEET_DB_VERSION);
                            JSONArray elementArray = new JSONArray();
                            try {
                                query = "SELECT name, category FROM items WHERE category<>?;";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) throw new SQLException("The database connection doesn't exist");
                                ps.setInt(1, Category.EQUIPMENT.getDatabaseValue());
                                result = ps.executeQuery();

                                while (result.next()) {
                                    Category category = Category.values()[result.getInt("category")];
                                    switch (category) {
                                        case ITEM:
                                            elementArray.put(new Item(result.getString("name")).exportElementJSON());
                                            break;
                                        case SPELL:
                                            elementArray.put(new Spell(result.getString("name")).exportElementJSON());
                                            break;
                                    }
                                }
                                ps.close();

                                query = "SELECT i.name AS name, e.type AS type FROM items AS i JOIN equipments AS e ON i.id=e.item_id WHERE i.category=?;";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) throw new SQLException("The database connection doesn't exist");
                                ps.setInt(1, Category.EQUIPMENT.getDatabaseValue());
                                result = ps.executeQuery();

                                while (result.next()) {
                                    EquipmentType type = EquipmentType.values()[result.getInt("type")];
                                    switch (type) {
                                        case ARMOR:
                                            elementArray.put(new Armor(result.getString("name")).exportElementJSON());
                                            break;
                                        case WEAPON:
                                            elementArray.put(new Weapon(result.getString("name")).exportElementJSON());
                                            break;
                                        case ADDON:
                                            elementArray.put(new Addon(result.getString("name")).exportElementJSON());
                                            break;
                                    }
                                }
                                ps.close();
                            } catch (SQLException e) {
                                try {
                                    if (ps != null) ps.close();
                                } catch (SQLException ignored) {
                                }
                                Logger.log(e);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI DATABASE", "Si e' verificato un errore durante la comunicazione con il database."));
                                return null;
                            }

                            inventory.put("elements", elementArray);
                            String exportableInventory = Base64.getEncoder().encodeToString(inventory.toString().getBytes(StandardCharsets.UTF_8));
                            try {
                                FileWriter writer = new FileWriter(finalDestPath);
                                writer.append(exportableInventory);
                                writer.close();
                                Platform.runLater(() -> new InformationAlert("SUCCESSO", "Esportazione Inventario", "Esportazione dell'inventario effettuata con successo!"));
                            } catch (IOException e) {
                                Logger.log(e);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI SCRITTURA", "Si e' verificato un errore durante la scrittura del file dell'inventario."));
                            }
                            return null;
                        }
                    };
                }
            }.start();
        }
    }

    // Edit & Remove EDT
    public static void editElement(@NotNull final ControllerSceneSheetViewer controller) {
        ElementPreview element = controller.tableViewInventory.getSelectionModel().getSelectedItem();
        if (element != null) editElement(controller, element);
    }
    private static void editElement(@NotNull final ControllerSceneSheetViewer controller, @NotNull final ElementPreview element) {
        elementName = element.getName();
        Scene scene;
        switch (element.getCategory()) {
            case ITEM:
                scene = SceneInventoryItem.getScene();
                break;

            case EQUIPMENT:
                try {
                    EquipmentType equipmentType = new Equipment(element.getName()).getType();
                    switch (equipmentType) {
                        case ARMOR:
                            scene = SceneInventoryArmor.getScene();
                            break;

                        case WEAPON:
                            scene = SceneInventoryWeapon.getScene();
                            break;

                        case ADDON:
                            scene = SceneInventoryAddon.getScene();
                            break;

                        default: // Invalid
                            new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria equipaggiamento valida.");
                            return;
                    }
                } catch (SQLException ex) {
                    new ErrorAlert("ERRORE", "ERRORE COL DATABASE", "Si e' verificato un errore durante l'interrogazione del database.");
                    return;
                }
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
        TabSpells.updateListViews(controller);
        TabEquipment.reloadEquipment(controller);
    }
    private static void deleteElement(@NotNull final ControllerSceneSheetViewer controller, @NotNull final ObservableList<ElementPreview> elementList) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        for (ElementPreview element : elementList) {
                            try {
                                PreparedStatement ps = DBManager.preparedStatement("DELETE FROM items WHERE id=?;");
                                if (ps == null) throw new SQLException("The database connection doesn't exist");
                                ps.setInt(1, element.getId());
                                ps.executeUpdate();
                                ps.close();
                                Platform.runLater(() -> {
                                    search(controller);
                                    TabSpells.updateListViews(controller);
                                    TabEquipment.reloadEquipment(controller);
                                });
                            } catch (SQLException e) {
                                Logger.log(e);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Rimozione", "Si e' verificato un errore durante la rimozione dell'elemento: \"" + element.getName() + "\""));
                                return null;
                            }
                        }

                        return null;
                    }
                };
            }
        }.start();
    }


    // Context Menu EDT
    public static void openInventoryContextMenu(@NotNull final ControllerSceneSheetViewer controller, @NotNull final ContextMenuEvent event) {
        ContextMenu menu = new ContextMenu();
        Menu addMenu = getContextAddMenu(controller);
        menu.getItems().add(addMenu);

        ObservableList<ElementPreview> elementList = controller.tableViewInventory.getSelectionModel().getSelectedItems();
        if (!elementList.isEmpty()) {
            if (elementList.size() == 1) {
                MenuItem editSelected = new MenuItem("Modifica Elemento");
                editSelected.setOnAction((e) -> editElement(controller, elementList.get(0)));
                menu.getItems().add(editSelected);
            }
            MenuItem deleteSelected = new MenuItem("Elimina Elemento");
            deleteSelected.setOnAction((e) -> deleteElement(controller, elementList));
            menu.getItems().add(deleteSelected);
        }

        MenuItem resetFilters = new MenuItem("Reimposta Filtri");
        resetFilters.setOnAction((e) -> resetSearchBarAndCategory(controller));
        menu.getItems().add(resetFilters);

        menu.setAutoHide(true);
        menu.show(Client.getStage(), event.getScreenX(), event.getScreenY());
    }
    private static Menu getContextAddMenu(@NotNull final ControllerSceneSheetViewer controller) {
        Menu addMenu = new Menu("Aggiungi");
        MenuItem addItem = new MenuItem("Oggetto");
        addItem.setOnAction((e) -> {
            Stage popupStage = Client.initPopupStage(SceneInventoryItem.getScene());
            popupStage.showAndWait();
            search(controller);
            TabSpells.updateListViews(controller);
            TabEquipment.reloadEquipment(controller);
        });
        Menu equipmentMenu = getContextAddEquipmentMenu(controller);
        MenuItem addSpell = new MenuItem("Incantesimo");
        addSpell.setOnAction((e) -> {
            Stage popupStage = Client.initPopupStage(SceneInventorySpell.getScene());
            popupStage.showAndWait();
            search(controller);
            TabSpells.updateListViews(controller);
            TabEquipment.reloadEquipment(controller);
        });
        Menu bagMenu = getContextAddBagMenu(controller);
        addMenu.getItems().addAll(addItem, equipmentMenu, addSpell, bagMenu);
        return addMenu;
    }
    private static Menu getContextAddEquipmentMenu(@NotNull final ControllerSceneSheetViewer controller) {
        Menu equipmentMenu = new Menu("Equipaggiamento");
        MenuItem addArmor = new MenuItem("Armatura");
        addArmor.setOnAction((e) -> {
            Stage popupStage = Client.initPopupStage(SceneInventoryArmor.getScene());
            popupStage.showAndWait();
            search(controller);
            TabSpells.updateListViews(controller);
            TabEquipment.reloadEquipment(controller);
        });
        MenuItem addWeapon = new MenuItem("Arma");
        addWeapon.setOnAction((e) -> {
            Stage popupStage = Client.initPopupStage(SceneInventoryWeapon.getScene());
            popupStage.showAndWait();
            search(controller);
            TabSpells.updateListViews(controller);
            TabEquipment.reloadEquipment(controller);
        });
        MenuItem addAddon = new MenuItem("Addon");
        addAddon.setOnAction((e) -> {
            Stage popupStage = Client.initPopupStage(SceneInventoryAddon.getScene());
            popupStage.showAndWait();
            search(controller);
            TabSpells.updateListViews(controller);
            TabEquipment.reloadEquipment(controller);
        });
        equipmentMenu.getItems().addAll(addArmor, addWeapon, addAddon);
        return equipmentMenu;
    }
    private static Menu getContextAddBagMenu(@NotNull final ControllerSceneSheetViewer controller) {
        Menu bagMenu = new Menu("Dotazione");
        for (DnDBag bag : DnDBags.BAGS) {
            MenuItem item = new MenuItem(bag.getName());
            item.setOnAction((e) -> addBagContentToInventory(controller, bag));
            bagMenu.getItems().add(item);
        }
        return bagMenu;
    }
    private static void addBagContentToInventory(@NotNull final ControllerSceneSheetViewer controller, @NotNull final DnDBag bag) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        if (bag.isIncludeBag()) {
                            try {
                                if (Addon.checkIfExist(DnDBag.BAG.getName())) {
                                    Item bagItem = new Addon(DnDBag.BAG.getName());
                                    bagItem.setQuantity(bagItem.getQuantity() + 1);
                                    bagItem.saveIntoDatabase(bagItem.getName());
                                } else {
                                    DnDBag.BAG.saveIntoDatabase(null);
                                }
                            } catch (SQLException e) {
                                Logger.log(e);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Database", "Si e' verificato un errore durante l'interrogazione del database."));
                                return null;
                            }
                        }
                        for (Item bagItem : bag.getItems()) {
                            try {
                                if (Item.checkIfExist(bagItem.getName())) {
                                    Item invItem = new Item(bagItem.getName());
                                    invItem.setQuantity(invItem.getQuantity() + bagItem.getQuantity());
                                    invItem.saveIntoDatabase(invItem.getName());
                                } else {
                                    bagItem.saveIntoDatabase(null);
                                }
                            } catch (SQLException e) {
                                Logger.log(e);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Ricerca", "Si e' verificato un errore durante la ricerca dell'elemento: \"" + bagItem.getName() + "\""));
                                return null;
                            }
                        }
                        Platform.runLater(() -> {
                            search(controller);
                            TabSpells.updateListViews(controller);
                            TabEquipment.reloadEquipment(controller);
                        });
                        return null;
                    }
                };
            }
        }.start();
    }
}
