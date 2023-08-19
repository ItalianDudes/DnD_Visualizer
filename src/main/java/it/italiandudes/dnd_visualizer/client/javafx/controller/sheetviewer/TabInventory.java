package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.data.ElementPreview;
import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

public final class TabInventory {

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
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
    }

    // Lost Focus On Action Fire Event
    private static void onLostFocusFireActionEvent(@NotNull final ControllerSceneSheetViewer controller) {
        // TODO: add eventual TextField that need to run their onClick method
    }

    // EDT
}
