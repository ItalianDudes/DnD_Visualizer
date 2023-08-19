package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer.TabAbility;
import it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer.TabCharacter;
import it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer.TabEquipment;
import it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer.TabInventory;
import it.italiandudes.dnd_visualizer.data.ElementPreview;
import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.Rarity;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

//@SuppressWarnings("unused")
public final class ControllerSceneSheetViewer {

    //Graphic Elements
    // TabCharacter
    @FXML public TextField textFieldCharacterName;
    @FXML public TextField textFieldClass;
    @FXML public Spinner<Integer> spinnerLevel;
    @FXML public TextField textFieldBackground;
    @FXML public TextField textFieldPlayerName;
    @FXML public TextField textFieldRace;
    @FXML public TextField textFieldAlignment;
    @FXML public TextField textFieldExperience;
    @FXML public ImageView imageViewCharacterImage;
    @FXML public ImageView imageViewCurrentHP;
    @FXML public StackPane stackPaneCurrentHP;
    @FXML public Label labelHPLeftPercentage;
    @FXML public TextField textFieldMaxHP;
    @FXML public TextField textFieldCurrentHP;
    @FXML public TextField textFieldTempHP;
    @FXML public TextField textFieldTotalLifeDiceAmount;
    @FXML public TextField textFieldTotalLifeDiceFaces;
    @FXML public TextField textFieldCurrentLifeDiceAmount;
    @FXML public TextField textFieldCurrentLifeDiceFaces;
    @FXML public Spinner<Integer> spinnerProficiencyBonus;
    @FXML public TextField textFieldInitiative;
    @FXML public Spinner<Integer> spinnerInspiration;
    @FXML public TextField textFieldSpeed;
    @FXML public TextArea textAreaPersonalityTraits;
    @FXML public TextArea textAreaBonds;
    @FXML public TextArea textAreaIdeals;
    @FXML public TextArea textAreaFlaws;
    @FXML public Slider sliderDSTSuccesses;
    @FXML public Slider sliderDSTFailures;

    // TabAbility
    @FXML public Spinner<Integer> spinnerStrength;
    @FXML public Spinner<Integer> spinnerDexterity;
    @FXML public Spinner<Integer> spinnerConstitution;
    @FXML public Spinner<Integer> spinnerIntelligence;
    @FXML public Spinner<Integer> spinnerWisdom;
    @FXML public Spinner<Integer> spinnerCharisma;
    @FXML public Label labelModStrength;
    @FXML public Label labelModDexterity;
    @FXML public Label labelModConstitution;
    @FXML public Label labelModIntelligence;
    @FXML public Label labelModWisdom;
    @FXML public Label labelPassivePerception;
    @FXML public Label labelModCharisma;
    @FXML public RadioButton radioButtonSTStrengthProficiency;
    @FXML public RadioButton radioButtonSTDexterityProficiency;
    @FXML public RadioButton radioButtonSTConstitutionProficiency;
    @FXML public RadioButton radioButtonSTIntelligenceProficiency;
    @FXML public RadioButton radioButtonSTWisdomProficiency;
    @FXML public RadioButton radioButtonSTCharismaProficiency;
    @FXML public Label labelSTStrength;
    @FXML public Label labelSTDexterity;
    @FXML public Label labelSTConstitution;
    @FXML public Label labelSTIntelligence;
    @FXML public Label labelSTWisdom;
    @FXML public Label labelSTCharisma;
    @FXML public Label labelAthletics;
    @FXML public RadioButton radioButtonProficiencyAthletics;
    @FXML public RadioButton radioButtonMasteryAthletics;
    @FXML public Label labelAcrobatics;
    @FXML public RadioButton radioButtonProficiencyAcrobatics;
    @FXML public RadioButton radioButtonMasteryAcrobatics;
    @FXML public Label labelStealth;
    @FXML public RadioButton radioButtonProficiencyStealth;
    @FXML public RadioButton radioButtonMasteryStealth;
    @FXML public Label labelSleightOfHand;
    @FXML public RadioButton radioButtonProficiencySleightOfHand;
    @FXML public RadioButton radioButtonMasterySleightOfHand;
    @FXML public Label labelArcana;
    @FXML public RadioButton radioButtonProficiencyArcana;
    @FXML public RadioButton radioButtonMasteryArcana;
    @FXML public Label labelInvestigation;
    @FXML public RadioButton radioButtonProficiencyInvestigation;
    @FXML public RadioButton radioButtonMasteryInvestigation;
    @FXML public Label labelNature;
    @FXML public RadioButton radioButtonProficiencyNature;
    @FXML public RadioButton radioButtonMasteryNature;
    @FXML public Label labelReligion;
    @FXML public RadioButton radioButtonProficiencyReligion;
    @FXML public RadioButton radioButtonMasteryReligion;
    @FXML public Label labelHistory;
    @FXML public RadioButton radioButtonProficiencyHistory;
    @FXML public RadioButton radioButtonMasteryHistory;
    @FXML public Label labelAnimalHandling;
    @FXML public RadioButton radioButtonProficiencyAnimalHandling;
    @FXML public RadioButton radioButtonMasteryAnimalHandling;
    @FXML public Label labelInsight;
    @FXML public RadioButton radioButtonProficiencyInsight;
    @FXML public RadioButton radioButtonMasteryInsight;
    @FXML public Label labelMedicine;
    @FXML public RadioButton radioButtonProficiencyMedicine;
    @FXML public RadioButton radioButtonMasteryMedicine;
    @FXML public Label labelPerception;
    @FXML public RadioButton radioButtonProficiencyPerception;
    @FXML public RadioButton radioButtonMasteryPerception;
    @FXML public Label labelSurvival;
    @FXML public RadioButton radioButtonProficiencySurvival;
    @FXML public RadioButton radioButtonMasterySurvival;
    @FXML public Label labelDeception;
    @FXML public RadioButton radioButtonProficiencyDeception;
    @FXML public RadioButton radioButtonMasteryDeception;
    @FXML public Label labelIntimidation;
    @FXML public RadioButton radioButtonProficiencyIntimidation;
    @FXML public RadioButton radioButtonMasteryIntimidation;
    @FXML public Label labelPerformance;
    @FXML public RadioButton radioButtonProficiencyPerformance;
    @FXML public RadioButton radioButtonMasteryPerformance;
    @FXML public Label labelPersuasion;
    @FXML public RadioButton radioButtonProficiencyPersuasion;
    @FXML public RadioButton radioButtonMasteryPersuasion;

    // TabInventory
    @FXML public TextField textFieldSearchBar;
    @FXML public TableView<ElementPreview> tableViewInventory;
    @FXML public TableColumn<ElementPreview, Integer> tableColumnID;
    @FXML public TableColumn<ElementPreview, String> tableColumnName;
    @FXML public TableColumn<ElementPreview, Category> tableColumnCategory;
    @FXML public TableColumn<ElementPreview, Rarity> tableColumnRarity;
    @FXML public TableColumn<ElementPreview, Double> tableColumnWeight;
    @FXML public TableColumn<ElementPreview, Integer> tableColumnCostMR;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        TabCharacter.initialize(this);
        TabAbility.initialize(this);
        TabEquipment.initialize(this);
        TabInventory.initialize(this);
    }

    // Direct EDT Method Calls
    @FXML private void validateNewCurrentLifeDiceAmount() {
        TabCharacter.validateNewCurrentLifeDiceAmount(this);
    }
    @FXML private void recalculateHealthPercentage() {
        TabCharacter.recalculateHealthPercentage(this);
    }
    @FXML private void openCharacterImageFileChooser() {
        TabCharacter.openCharacterImageFileChooser(this);
    }
    @FXML private void updateLifeDiceFaces() {
        TabCharacter.updateLifeDiceFaces(this);
    }
    @FXML private void toggleSTStrengthProficiency() {
        TabAbility.toggleSTStrengthProficiency(this);
    }
    @FXML private void toggleSTDexterityProficiency() {
        TabAbility.toggleSTDexterityProficiency(this);
    }
    @FXML private void toggleSTConstitutionProficiency() {
        TabAbility.toggleSTConstitutionProficiency(this);
    }
    @FXML private void toggleSTIntelligenceProficiency() {
        TabAbility.toggleSTIntelligenceProficiency(this);
    }
    @FXML private void toggleSTWisdomProficiency() {
        TabAbility.toggleSTWisdomProficiency(this);
    }
    @FXML private void toggleSTCharismaProficiency() {
        TabAbility.toggleSTCharismaProficiency(this);
    }
    @FXML private void updateStrengthAbilities() {
        TabAbility.updateStrengthAbilities(this, Integer.parseInt(labelModStrength.getText()), spinnerProficiencyBonus.getValue());
    }
    @FXML private void updateDexterityAbilities() {
        TabAbility.updateDexterityAbilities(this, Integer.parseInt(labelModDexterity.getText()), spinnerProficiencyBonus.getValue());
    }
    @FXML private void updateIntelligenceAbilities() {
        TabAbility.updateIntelligenceAbilities(this, Integer.parseInt(labelModIntelligence.getText()), spinnerProficiencyBonus.getValue());
    }
    @FXML private void updateWisdomAbilities() {
        TabAbility.updateWisdomAbilities(this, Integer.parseInt(labelModWisdom.getText()), spinnerProficiencyBonus.getValue());
    }
    @FXML private void updateCharismaAbilities() {
        TabAbility.updateCharismaAbilities(this, Integer.parseInt(labelModCharisma.getText()), spinnerProficiencyBonus.getValue());
    }
    @FXML private void doubleClickEdit(@NotNull final MouseEvent event) {
        if (event.getClickCount() >= 2) editElement();
    }
    @FXML private void search() {}
    @FXML private void deleteElement() {}
    @FXML private void editElement() {}
    @FXML private void addElement() {}
}