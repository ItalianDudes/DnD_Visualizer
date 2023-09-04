package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.client.javafx.util.SheetDataHandler;
import it.italiandudes.dnd_visualizer.utils.Defs;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

public final class TabTraitsAndProficiencies {

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        readTabData(controller);
        setOnChangeTriggers(controller);
    }

    // Data Reader
    private static void readTabData(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {

                        String featuresAndTraits = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.FEATURES_AND_TRAITS);
                        String languages = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.LANGUAGES);
                        String armors = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.ARMORS);
                        String weapons = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.WEAPONS);
                        String tools = SheetDataHandler.readKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.TOOLS);
                        Platform.runLater(() -> {
                            if (featuresAndTraits != null) controller.textAreaFeaturesAndTraits.setText(featuresAndTraits);
                            if (languages != null) controller.textAreaLanguages.setText(languages);
                            if (armors != null) controller.textAreaProficiencyArmors.setText(armors);
                            if (weapons != null) controller.textAreaProficiencyWeapons.setText(weapons);
                            if (tools != null) controller.textAreaProficiencyTools.setText(tools);
                        });
                        return null;
                    }
                };
            }
        }.start();
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textAreaFeaturesAndTraits.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.FEATURES_AND_TRAITS, newValue));
        controller.textAreaLanguages.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.LANGUAGES, newValue));
        controller.textAreaProficiencyWeapons.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.WEAPONS, newValue));
        controller.textAreaProficiencyArmors.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.ARMORS, newValue));
        controller.textAreaProficiencyTools.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(Defs.KeyParameters.TabTraitsAndCompetences.TOOLS, newValue));
    }

}
