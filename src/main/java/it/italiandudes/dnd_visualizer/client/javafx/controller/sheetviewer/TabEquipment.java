package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import org.jetbrains.annotations.NotNull;

public final class TabEquipment {

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        setOnChangeTriggers(controller);
        onLostFocusFireActionEvent(controller);
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
