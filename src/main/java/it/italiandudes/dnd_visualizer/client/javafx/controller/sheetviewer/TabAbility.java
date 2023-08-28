package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.client.javafx.util.StatsCalculator;
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
        setOnChangeTriggers(controller);
        updateParameters(controller);
    }

    // OnChange Triggers Setter
    public static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.spinnerStrength.getEditor().textProperty().addListener((observable -> updateStrength(controller)));
        controller.spinnerDexterity.getEditor().textProperty().addListener((observable -> updateDexterity(controller)));
        controller.spinnerConstitution.getEditor().textProperty().addListener((observable -> updateConstitution(controller)));
        controller.spinnerIntelligence.getEditor().textProperty().addListener((observable -> updateIntelligence(controller)));
        controller.spinnerWisdom.getEditor().textProperty().addListener((observable -> updateWisdom(controller)));
        controller.spinnerCharisma.getEditor().textProperty().addListener((observable -> updateCharisma(controller)));
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
    }
    public static void updateDexterity(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerDexterity.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModDexterity.setText(String.valueOf(mod));
        toggleSTDexterityProficiency(controller);
        controller.textFieldInitiative.setText(String.valueOf(mod));
        updateDexterityAbilities(controller, mod, controller.spinnerProficiencyBonus.getValue());
        TabSpells.updateSpellModifiers(controller);
    }
    public static void updateConstitution(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerConstitution.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModConstitution.setText(String.valueOf(mod));
        toggleSTConstitutionProficiency(controller);
        TabSpells.updateSpellModifiers(controller);
    }
    public static void updateIntelligence(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerIntelligence.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModIntelligence.setText(String.valueOf(mod));
        toggleSTIntelligenceProficiency(controller);
        updateIntelligenceAbilities(controller, mod, controller.spinnerProficiencyBonus.getValue());
        TabSpells.updateSpellModifiers(controller);
    }
    public static void updateWisdom(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerWisdom.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModWisdom.setText(String.valueOf(mod));
        toggleSTWisdomProficiency(controller);
        updateWisdomAbilities(controller, mod, controller.spinnerProficiencyBonus.getValue());
        TabSpells.updateSpellModifiers(controller);
    }
    public static void updateCharisma(@NotNull final ControllerSceneSheetViewer controller) {
        int value = controller.spinnerCharisma.getValue();
        int mod = (int) Math.floor((value-10)/2.0);
        controller.labelModCharisma.setText(String.valueOf(mod));
        toggleSTCharismaProficiency(controller);
        updateCharismaAbilities(controller, mod, controller.spinnerProficiencyBonus.getValue());
        TabSpells.updateSpellModifiers(controller);
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
