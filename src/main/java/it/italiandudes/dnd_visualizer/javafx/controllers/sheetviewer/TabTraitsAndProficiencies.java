package it.italiandudes.dnd_visualizer.javafx.controllers.sheetviewer;

import it.italiandudes.dnd_visualizer.db.SheetKeyParameters;
import it.italiandudes.dnd_visualizer.javafx.controllers.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.utils.SheetDataHandler;
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
                return new Task<>() {
                    @Override
                    protected Void call() {
                        String featuresAndTraits = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.FEATURES_AND_TRAITS);
                        String languages = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.LANGUAGES);
                        String armors = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.ARMORS);
                        String weapons = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.WEAPONS);
                        String tools = SheetDataHandler.readKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.TOOLS);
                        Platform.runLater(() -> {
                            if (featuresAndTraits != null)
                                controller.textAreaFeaturesAndTraits.setText(featuresAndTraits);
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
        controller.textAreaFeaturesAndTraits.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.FEATURES_AND_TRAITS, newValue));
        controller.textAreaLanguages.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.LANGUAGES, newValue));
        controller.textAreaProficiencyWeapons.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.WEAPONS, newValue));
        controller.textAreaProficiencyArmors.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.ARMORS, newValue));
        controller.textAreaProficiencyTools.textProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(SheetKeyParameters.TabTraitsAndCompetences.TOOLS, newValue));
    }

}
