package it.italiandudes.dnd_visualizer.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.utils.SheetDataHandler;
import it.italiandudes.dnd_visualizer.javafx.utils.StatsCalculator;
import it.italiandudes.dnd_visualizer.utils.Defs.KeyParameters;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.SpinnerValueFactory;
import org.jetbrains.annotations.NotNull;

public final class TabAbility {

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.spinnerStrength.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 8, 1));
        controller.spinnerDexterity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 8, 1));
        controller.spinnerConstitution.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 8, 1));
        controller.spinnerIntelligence.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 8, 1));
        controller.spinnerWisdom.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 8, 1));
        controller.spinnerCharisma.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 8, 1));
        readTabData(controller);
        setOnChangeTriggers(controller);
        updateParameters(controller);
    }

    // Data Reader
    private static void readTabData(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String strength = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.STRENGTH);
                        String dexterity = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.DEXTERITY);
                        String constitution = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.CONSTITUTION);
                        String intelligence = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.INTELLIGENCE);
                        String wisdom = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.WISDOM);
                        String charisma = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.CHARISMA);
                        String strProfSTStrength = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ST_STRENGTH);
                        boolean profSTStrength = strProfSTStrength != null && Integer.parseInt(strProfSTStrength) != 0;
                        String strProfSTDexterity = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ST_DEXTERITY);
                        boolean profSTDexterity = strProfSTDexterity != null && Integer.parseInt(strProfSTDexterity) != 0;
                        String strProfConstitution = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ST_CONSTITUTION);
                        boolean profSTConstitution = strProfConstitution != null && Integer.parseInt(strProfConstitution) != 0;
                        String strProfIntelligence = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ST_INTELLIGENCE);
                        boolean profSTIntelligence = strProfIntelligence != null && Integer.parseInt(strProfIntelligence) != 0;
                        String strProfWisdom = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ST_WISDOM);
                        boolean profSTWisdom = strProfWisdom != null && Integer.parseInt(strProfWisdom) != 0;
                        String strProfCharisma = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ST_CHARISMA);
                        boolean profSTCharisma = strProfCharisma != null && Integer.parseInt(strProfCharisma) != 0;
                        String strProfAthletics = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ATHLETICS);
                        int profAthletics = strProfAthletics != null ? Integer.parseInt(strProfAthletics) : 0;
                        String strProfAcrobatics = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ACROBATICS);
                        int profAcrobatics = strProfAcrobatics != null ? Integer.parseInt(strProfAcrobatics) : 0;
                        String strProfStealth = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_STEALTH);
                        int profStealth = strProfStealth != null ? Integer.parseInt(strProfStealth) : 0;
                        String strProfSleightOfHand = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_SLEIGHT_OF_HAND);
                        int profSleightOfHand = strProfSleightOfHand != null ? Integer.parseInt(strProfSleightOfHand) : 0;
                        String strProfArcana = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ARCANA);
                        int profArcana = strProfArcana != null ? Integer.parseInt(strProfArcana) : 0;
                        String strProfInvestigation = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_INVESTIGATION);
                        int profInvestigation = strProfInvestigation != null ? Integer.parseInt(strProfInvestigation) : 0;
                        String strProfNature = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_NATURE);
                        int profNature = strProfNature != null ? Integer.parseInt(strProfNature) : 0;
                        String strProfReligion = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_RELIGION);
                        int profReligion = strProfReligion != null ? Integer.parseInt(strProfReligion) : 0;
                        String strProfHistory = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_HISTORY);
                        int profHistory = strProfHistory != null ? Integer.parseInt(strProfHistory) : 0;
                        String strProfAnimalHandling = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_ANIMAL_HANDLING);
                        int profAnimalHandling = strProfAnimalHandling != null ? Integer.parseInt(strProfAnimalHandling) : 0;
                        String strProfInsight = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_INSIGHT);
                        int profInsight = strProfInsight != null ? Integer.parseInt(strProfInsight) : 0;
                        String strProfMedicine = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_MEDICINE);
                        int profMedicine = strProfMedicine != null ? Integer.parseInt(strProfMedicine) : 0;
                        String strProfPerception = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_PERCEPTION);
                        int profPerception = strProfPerception != null ? Integer.parseInt(strProfPerception) : 0;
                        String strProfSurvival = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_SURVIVAL);
                        int profSurvival = strProfSurvival != null ? Integer.parseInt(strProfSurvival) : 0;
                        String strProfDeception = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_DECEPTION);
                        int profDeception = strProfDeception != null ? Integer.parseInt(strProfDeception) : 0;
                        String strProfIntimidation = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_INTIMIDATION);
                        int profIntimidation = strProfIntimidation != null ? Integer.parseInt(strProfIntimidation) : 0;
                        String strProfPerformance = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_PERFORMANCE);
                        int profPerformance = strProfPerformance != null ? Integer.parseInt(strProfPerformance) : 0;
                        String strProfPersuasion = SheetDataHandler.readKeyParameter(KeyParameters.TabAbility.PROF_PERSUASION);
                        int profPersuasion = strProfPersuasion != null ? Integer.parseInt(strProfPersuasion) : 0;

                        Platform.runLater(() -> {
                            if (strength != null) controller.spinnerStrength.getValueFactory().setValue(Integer.parseInt(strength));
                            if (dexterity != null) controller.spinnerDexterity.getValueFactory().setValue(Integer.parseInt(dexterity));
                            if (constitution != null) controller.spinnerConstitution.getValueFactory().setValue(Integer.parseInt(constitution));
                            if (intelligence != null) controller.spinnerIntelligence.getValueFactory().setValue(Integer.parseInt(intelligence));
                            if (wisdom != null) controller.spinnerWisdom.getValueFactory().setValue(Integer.parseInt(wisdom));
                            if (charisma != null) controller.spinnerCharisma.getValueFactory().setValue(Integer.parseInt(charisma));

                            controller.radioButtonSTStrengthProficiency.setSelected(profSTStrength);
                            controller.radioButtonSTDexterityProficiency.setSelected(profSTDexterity);
                            controller.radioButtonSTConstitutionProficiency.setSelected(profSTConstitution);
                            controller.radioButtonSTIntelligenceProficiency.setSelected(profSTIntelligence);
                            controller.radioButtonSTWisdomProficiency.setSelected(profSTWisdom);
                            controller.radioButtonSTCharismaProficiency.setSelected(profSTCharisma);

                            if (profAthletics == 1) controller.radioButtonProficiencyAthletics.setSelected(true);
                            else if (profAthletics == 2) controller.radioButtonMasteryAthletics.setSelected(true);
                            if (profAcrobatics == 1) controller.radioButtonProficiencyAcrobatics.setSelected(true);
                            else if (profAcrobatics == 2) controller.radioButtonMasteryAcrobatics.setSelected(true);
                            if (profStealth == 1) controller.radioButtonProficiencyStealth.setSelected(true);
                            else if (profStealth == 2) controller.radioButtonMasteryStealth.setSelected(true);
                            if (profSleightOfHand == 1) controller.radioButtonProficiencySleightOfHand.setSelected(true);
                            else if (profSleightOfHand == 2) controller.radioButtonMasterySleightOfHand.setSelected(true);
                            if (profArcana == 1) controller.radioButtonProficiencyArcana.setSelected(true);
                            else if (profArcana == 2) controller.radioButtonMasteryArcana.setSelected(true);
                            if (profInvestigation == 1) controller.radioButtonProficiencyInvestigation.setSelected(true);
                            else if (profInvestigation == 2) controller.radioButtonMasteryInvestigation.setSelected(true);
                            if (profNature == 1) controller.radioButtonProficiencyNature.setSelected(true);
                            else if (profNature == 2) controller.radioButtonMasteryNature.setSelected(true);
                            if (profReligion == 1) controller.radioButtonProficiencyReligion.setSelected(true);
                            else if (profReligion == 2) controller.radioButtonMasteryReligion.setSelected(true);
                            if (profHistory == 1) controller.radioButtonProficiencyHistory.setSelected(true);
                            else if (profHistory == 2) controller.radioButtonMasteryHistory.setSelected(true);
                            if (profAnimalHandling == 1) controller.radioButtonProficiencyAnimalHandling.setSelected(true);
                            else if (profAnimalHandling == 2) controller.radioButtonMasteryAnimalHandling.setSelected(true);
                            if (profInsight == 1) controller.radioButtonProficiencyInsight.setSelected(true);
                            else if (profInsight == 2) controller.radioButtonMasteryInsight.setSelected(true);
                            if (profMedicine == 1) controller.radioButtonProficiencyMedicine.setSelected(true);
                            else if (profMedicine == 2) controller.radioButtonMasteryMedicine.setSelected(true);
                            if (profPerception == 1) controller.radioButtonProficiencyPerception.setSelected(true);
                            else if (profPerception == 2) controller.radioButtonMasteryPerception.setSelected(true);
                            if (profSurvival == 1) controller.radioButtonProficiencySurvival.setSelected(true);
                            else if (profSurvival == 2) controller.radioButtonMasterySurvival.setSelected(true);
                            if (profDeception == 1) controller.radioButtonProficiencyDeception.setSelected(true);
                            else if (profDeception == 2) controller.radioButtonMasteryDeception.setSelected(true);
                            if (profIntimidation == 1) controller.radioButtonProficiencyIntimidation.setSelected(true);
                            else if (profIntimidation == 2) controller.radioButtonMasteryIntimidation.setSelected(true);
                            if (profPerformance == 1) controller.radioButtonProficiencyPerformance.setSelected(true);
                            else if (profPerformance == 2) controller.radioButtonMasteryPerformance.setSelected(true);
                            if (profPersuasion == 1) controller.radioButtonProficiencyPersuasion.setSelected(true);
                            else if (profPerception == 2) controller.radioButtonMasteryPersuasion.setSelected(true);
                            updateParameters(controller);
                        });

                        return null;
                    }
                };
            }
        }.start();
    }

    // OnChange Triggers Setter
    public static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.spinnerStrength.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.STRENGTH, newValue);
            updateStrength(controller);
        });
        controller.spinnerDexterity.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.DEXTERITY, newValue);
            updateDexterity(controller);
        });
        controller.spinnerConstitution.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.CONSTITUTION, newValue);
            updateConstitution(controller);
        });
        controller.spinnerIntelligence.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.INTELLIGENCE, newValue);
            updateIntelligence(controller);
        });
        controller.spinnerWisdom.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.WISDOM, newValue);
            updateWisdom(controller);
        });
        controller.spinnerCharisma.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.CHARISMA, newValue);
            updateCharisma(controller);
        });
        controller.radioButtonSTStrengthProficiency.selectedProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ST_STRENGTH, String.valueOf(newValue?1:0)));
        controller.radioButtonSTDexterityProficiency.selectedProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ST_DEXTERITY, String.valueOf(newValue?1:0)));
        controller.radioButtonSTConstitutionProficiency.selectedProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ST_CONSTITUTION, String.valueOf(newValue?1:0)));
        controller.radioButtonSTIntelligenceProficiency.selectedProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ST_INTELLIGENCE, String.valueOf(newValue?1:0)));
        controller.radioButtonSTWisdomProficiency.selectedProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ST_WISDOM, String.valueOf(newValue?1:0)));
        controller.radioButtonSTCharismaProficiency.selectedProperty().addListener((observable, oldValue, newValue) -> SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ST_CHARISMA, String.valueOf(newValue?1:0)));
        controller.radioButtonProficiencyAthletics.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryAthletics.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ATHLETICS, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ATHLETICS, newValue?"1":"0");
        });
        controller.radioButtonMasteryAthletics.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyAthletics.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ATHLETICS, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ATHLETICS, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyAcrobatics.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryAcrobatics.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ACROBATICS, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ACROBATICS, newValue?"1":"0");
        });
        controller.radioButtonMasteryAcrobatics.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyAcrobatics.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ACROBATICS, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ACROBATICS, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyStealth.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryStealth.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_STEALTH, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_STEALTH, newValue?"1":"0");
        });
        controller.radioButtonMasteryStealth.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyStealth.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_STEALTH, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_STEALTH, proficiency?"1":"0");
        });
        controller.radioButtonProficiencySleightOfHand.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasterySleightOfHand.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_SLEIGHT_OF_HAND, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_SLEIGHT_OF_HAND, newValue?"1":"0");
        });
        controller.radioButtonMasterySleightOfHand.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencySleightOfHand.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_SLEIGHT_OF_HAND, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_SLEIGHT_OF_HAND, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyArcana.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryArcana.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ARCANA, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ARCANA, newValue?"1":"0");
        });
        controller.radioButtonMasteryArcana.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyArcana.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ARCANA, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ARCANA, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyInvestigation.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryInvestigation.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INVESTIGATION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INVESTIGATION, newValue?"1":"0");
        });
        controller.radioButtonMasteryInvestigation.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyInvestigation.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INVESTIGATION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INVESTIGATION, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyNature.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryNature.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_NATURE, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_NATURE, newValue?"1":"0");
        });
        controller.radioButtonMasteryNature.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyNature.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_NATURE, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_NATURE, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyReligion.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryReligion.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_RELIGION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_RELIGION, newValue?"1":"0");
        });
        controller.radioButtonMasteryReligion.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyReligion.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_RELIGION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_RELIGION, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyHistory.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryHistory.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_HISTORY, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_HISTORY, newValue?"1":"0");
        });
        controller.radioButtonMasteryHistory.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyHistory.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_HISTORY, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_HISTORY, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyAnimalHandling.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryAnimalHandling.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ANIMAL_HANDLING, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ANIMAL_HANDLING, newValue?"1":"0");
        });
        controller.radioButtonMasteryAnimalHandling.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyAnimalHandling.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ANIMAL_HANDLING, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_ANIMAL_HANDLING, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyInsight.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryInsight.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INSIGHT, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INSIGHT, newValue?"1":"0");
        });
        controller.radioButtonMasteryInsight.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyInsight.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INSIGHT, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INSIGHT, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyMedicine.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryMedicine.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_MEDICINE, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_MEDICINE, newValue?"1":"0");
        });
        controller.radioButtonMasteryMedicine.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyMedicine.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_MEDICINE, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_MEDICINE, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyPerception.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryPerception.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERCEPTION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERCEPTION, newValue?"1":"0");
        });
        controller.radioButtonMasteryPerception.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyPerception.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERCEPTION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERCEPTION, proficiency?"1":"0");
        });
        controller.radioButtonProficiencySurvival.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasterySurvival.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_SURVIVAL, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_SURVIVAL, newValue?"1":"0");
        });
        controller.radioButtonMasterySurvival.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencySurvival.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_SURVIVAL, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_SURVIVAL, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyDeception.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryDeception.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_DECEPTION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_DECEPTION, newValue?"1":"0");
        });
        controller.radioButtonMasteryDeception.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyDeception.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_DECEPTION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_DECEPTION, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyIntimidation.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryIntimidation.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INTIMIDATION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INTIMIDATION, newValue?"1":"0");
        });
        controller.radioButtonMasteryIntimidation.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyIntimidation.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INTIMIDATION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_INTIMIDATION, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyPerformance.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryPerformance.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERFORMANCE, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERFORMANCE, newValue?"1":"0");
        });
        controller.radioButtonMasteryPerformance.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyPerformance.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERFORMANCE, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERFORMANCE, proficiency?"1":"0");
        });
        controller.radioButtonProficiencyPersuasion.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean mastery = controller.radioButtonMasteryPersuasion.isSelected();
            if (mastery) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERSUASION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERSUASION, newValue?"1":"0");
        });
        controller.radioButtonMasteryPersuasion.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean proficiency = controller.radioButtonProficiencyPersuasion.isSelected();
            if (newValue) SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERSUASION, "2");
            else SheetDataHandler.writeKeyParameter(KeyParameters.TabAbility.PROF_PERSUASION, proficiency?"1":"0");
        });
    }

    // EDT
    public static void updateParameters(@NotNull final ControllerSceneSheetViewer controller) {
        updateStrength(controller);
        updateDexterity(controller);
        updateConstitution(controller);
        updateIntelligence(controller);
        updateWisdom(controller);
        updateCharisma(controller);
    }
    public static void updateStrength(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerStrength.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModStrength.setText(String.valueOf(mod));
        toggleSTStrengthProficiency(controller);
        updateStrengthAbilities(controller, mod, controller.spinnerProficiencyBonus.getValue());
        TabSpells.updateSpellModifiers(controller);
        TabInventory.updateLoad(controller);
        TabEquipment.updateCA(controller);
    }
    public static void updateDexterity(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerDexterity.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModDexterity.setText(String.valueOf(mod));
        toggleSTDexterityProficiency(controller);
        controller.textFieldInitiative.setText(String.valueOf(mod));
        updateDexterityAbilities(controller, mod, controller.spinnerProficiencyBonus.getValue());
        TabSpells.updateSpellModifiers(controller);
        TabEquipment.updateCA(controller);
    }
    public static void updateConstitution(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerConstitution.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModConstitution.setText(String.valueOf(mod));
        toggleSTConstitutionProficiency(controller);
        TabSpells.updateSpellModifiers(controller);
        TabEquipment.updateCA(controller);
    }
    public static void updateIntelligence(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerIntelligence.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModIntelligence.setText(String.valueOf(mod));
        toggleSTIntelligenceProficiency(controller);
        updateIntelligenceAbilities(controller, mod, controller.spinnerProficiencyBonus.getValue());
        TabSpells.updateSpellModifiers(controller);
        TabEquipment.updateCA(controller);
    }
    public static void updateWisdom(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerWisdom.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModWisdom.setText(String.valueOf(mod));
        toggleSTWisdomProficiency(controller);
        updateWisdomAbilities(controller, mod, controller.spinnerProficiencyBonus.getValue());
        TabSpells.updateSpellModifiers(controller);
        TabEquipment.updateCA(controller);
    }
    public static void updateCharisma(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerCharisma.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModCharisma.setText(String.valueOf(mod));
        toggleSTCharismaProficiency(controller);
        updateCharismaAbilities(controller, mod, controller.spinnerProficiencyBonus.getValue());
        TabSpells.updateSpellModifiers(controller);
        TabEquipment.updateCA(controller);
    }
    public static void toggleSTStrengthProficiency(@NotNull final ControllerSceneSheetViewer controller) {
        int proficiencyBonus = controller.spinnerProficiencyBonus.getValue();
        if (controller.radioButtonSTStrengthProficiency.isSelected()) {
            controller.labelSTStrength.setText(String.valueOf(Integer.parseInt(controller.labelModStrength.getText()) + proficiencyBonus));
        } else {
            controller.labelSTStrength.setText(controller.labelModStrength.getText());
        }
    }
    public static void toggleSTDexterityProficiency(@NotNull final ControllerSceneSheetViewer controller) {
        int proficiencyBonus = controller.spinnerProficiencyBonus.getValue();
        if (controller.radioButtonSTDexterityProficiency.isSelected()) {
            controller.labelSTDexterity.setText(String.valueOf(Integer.parseInt(controller.labelModDexterity.getText()) + proficiencyBonus));
        } else {
            controller.labelSTDexterity.setText(controller.labelModDexterity.getText());
        }
    }
    public static void toggleSTConstitutionProficiency(@NotNull final ControllerSceneSheetViewer controller) {
        int proficiencyBonus = controller.spinnerProficiencyBonus.getValue();
        if (controller.radioButtonSTConstitutionProficiency.isSelected()) {
            controller.labelSTConstitution.setText(String.valueOf(Integer.parseInt(controller.labelModConstitution.getText()) + proficiencyBonus));
        } else {
            controller.labelSTConstitution.setText(controller.labelModConstitution.getText());
        }
    }
    public static void toggleSTIntelligenceProficiency(@NotNull final ControllerSceneSheetViewer controller) {
        int proficiencyBonus = controller.spinnerProficiencyBonus.getValue();
        if (controller.radioButtonSTIntelligenceProficiency.isSelected()) {
            controller.labelSTIntelligence.setText(String.valueOf(Integer.parseInt(controller.labelModIntelligence.getText()) + proficiencyBonus));
        } else {
            controller.labelSTIntelligence.setText(controller.labelModIntelligence.getText());
        }
    }
    public static void toggleSTWisdomProficiency(@NotNull final ControllerSceneSheetViewer controller) {
        int proficiencyBonus = controller.spinnerProficiencyBonus.getValue();
        if (controller.radioButtonSTWisdomProficiency.isSelected()) {
            controller.labelSTWisdom.setText(String.valueOf(Integer.parseInt(controller.labelModWisdom.getText()) + proficiencyBonus));
        } else {
            controller.labelSTWisdom.setText(controller.labelModWisdom.getText());
        }
    }
    public static void toggleSTCharismaProficiency(@NotNull final ControllerSceneSheetViewer controller) {
        int proficiencyBonus = controller.spinnerProficiencyBonus.getValue();
        if (controller.radioButtonSTCharismaProficiency.isSelected()) {
            controller.labelSTCharisma.setText(String.valueOf(Integer.parseInt(controller.labelModCharisma.getText()) + proficiencyBonus));
        } else {
            controller.labelSTCharisma.setText(controller.labelModCharisma.getText());
        }
    }

    // Ability
    public static void updateStrengthAbilities(@NotNull final ControllerSceneSheetViewer controller, int mod, int proficiencyBonus) {
        StatsCalculator.setNewAbilityValue(
                controller.labelAthletics, mod, proficiencyBonus,
                controller.radioButtonProficiencyAthletics,
                controller.radioButtonMasteryAthletics
        );
    }
    public static void updateDexterityAbilities(@NotNull final ControllerSceneSheetViewer controller, int mod, int proficiencyBonus) {
        StatsCalculator.setNewAbilityValue(
                controller.labelAcrobatics, mod, proficiencyBonus,
                controller.radioButtonProficiencyAcrobatics,
                controller.radioButtonMasteryAcrobatics
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelStealth, mod, proficiencyBonus,
                controller.radioButtonProficiencyStealth,
                controller.radioButtonMasteryStealth
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelSleightOfHand, mod, proficiencyBonus,
                controller.radioButtonProficiencySleightOfHand,
                controller.radioButtonMasterySleightOfHand
        );
    }
    public static void updateIntelligenceAbilities(@NotNull final ControllerSceneSheetViewer controller, int mod, int proficiencyBonus) {
        StatsCalculator.setNewAbilityValue(
                controller.labelArcana, mod, proficiencyBonus,
                controller.radioButtonProficiencyArcana,
                controller.radioButtonMasteryArcana
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelInvestigation, mod, proficiencyBonus,
                controller.radioButtonProficiencyInvestigation,
                controller.radioButtonMasteryInvestigation
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelNature, mod, proficiencyBonus,
                controller.radioButtonProficiencyNature,
                controller.radioButtonMasteryNature
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelReligion, mod, proficiencyBonus,
                controller.radioButtonProficiencyReligion,
                controller.radioButtonMasteryReligion
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelHistory, mod, proficiencyBonus,
                controller.radioButtonProficiencyHistory,
                controller.radioButtonMasteryHistory
        );
    }
    public static void updateWisdomAbilities(@NotNull final ControllerSceneSheetViewer controller, int mod, int proficiencyBonus) {
        StatsCalculator.setNewAbilityValue(
                controller.labelAnimalHandling, mod, proficiencyBonus,
                controller.radioButtonProficiencyAnimalHandling,
                controller.radioButtonMasteryAnimalHandling
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelInsight, mod, proficiencyBonus,
                controller.radioButtonProficiencyInsight,
                controller.radioButtonMasteryInsight
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelMedicine, mod, proficiencyBonus,
                controller.radioButtonProficiencyMedicine,
                controller.radioButtonMasteryMedicine
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelPerception, mod, proficiencyBonus,
                controller.radioButtonProficiencyPerception,
                controller.radioButtonMasteryPerception
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelSurvival, mod, proficiencyBonus,
                controller.radioButtonProficiencySurvival,
                controller.radioButtonMasterySurvival
        );
        controller.labelPassivePerception.setText(String.valueOf(10 + Integer.parseInt(controller.labelPerception.getText())));
    }
    public static void updateCharismaAbilities(@NotNull final ControllerSceneSheetViewer controller, int mod, int proficiencyBonus) {
        StatsCalculator.setNewAbilityValue(
                controller.labelDeception, mod, proficiencyBonus,
                controller.radioButtonProficiencyDeception,
                controller.radioButtonMasteryDeception
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelIntimidation, mod, proficiencyBonus,
                controller.radioButtonProficiencyIntimidation,
                controller.radioButtonMasteryIntimidation
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelPerformance, mod, proficiencyBonus,
                controller.radioButtonProficiencyPerformance,
                controller.radioButtonMasteryPerformance
        );
        StatsCalculator.setNewAbilityValue(
                controller.labelPersuasion, mod, proficiencyBonus,
                controller.radioButtonProficiencyPersuasion,
                controller.radioButtonMasteryPersuasion
        );
    }
}
