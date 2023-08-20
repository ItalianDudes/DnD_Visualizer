package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.data.ElementPreview;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jetbrains.annotations.NotNull;

public final class TabInventory {

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
        controller.tableColumnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        controller.tableColumnRarity.setCellValueFactory(new PropertyValueFactory<>("rarity"));
        controller.tableViewInventory.setRowFactory(tv -> new TableRow<ElementPreview>() {
            @Override
            protected void updateItem(ElementPreview item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("-fx-font-weight: bold;");
                } else {
                    setText(item.getName());
                    setStyle("-fx-background-color: "+item.getRarityColor()+";");
                }
            }
        });
        controller.tableColumnWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        controller.tableColumnCostMR.setCellValueFactory(new PropertyValueFactory<>("costCopper"));
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
}