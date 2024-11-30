package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer.*;
import it.italiandudes.dnd_visualizer.data.ElementPreview;
import it.italiandudes.dnd_visualizer.data.Note;
import it.italiandudes.dnd_visualizer.data.PrivilegeOrTrait;
import it.italiandudes.dnd_visualizer.data.effect.EffectPreview;
import it.italiandudes.dnd_visualizer.data.enums.*;
import it.italiandudes.dnd_visualizer.data.item.Addon;
import it.italiandudes.dnd_visualizer.data.item.Armor;
import it.italiandudes.dnd_visualizer.data.item.Weapon;
import it.italiandudes.dnd_visualizer.utils.CalendarEventManager;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.dnd_visualizer.utils.DiscordRichPresenceManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

public final class ControllerSceneSheetViewer {

    // Sheet Background
    @FXML private ImageView imageViewSheetBackground;

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
    @FXML public Label labelCA;
    @FXML public ImageView imageViewCurrentHP;
    @FXML public StackPane stackPaneCurrentHP;
    @FXML public Label labelHPLeftPercentage;
    @FXML public ImageView imageViewCurrentCA;
    @FXML public StackPane stackPaneCurrentCA;
    @FXML public TextField textFieldCalculatedMaxHP;
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

    // TabTraitsAndProficiencies
    @FXML public TextArea textAreaFeaturesAndTraits;
    @FXML public TextArea textAreaLanguages;
    @FXML public TextArea textAreaProficiencyWeapons;
    @FXML public TextArea textAreaProficiencyArmors;
    @FXML public TextArea textAreaProficiencyTools;

    // TabPrivilegesAndTraits
    @FXML public TextField textFieldPrivilegesAndTraitsSearchBar;
    @FXML public ComboBox<PrivilegeType> comboBoxPrivilegesAndTraitsType;
    @FXML public ListView<PrivilegeOrTrait> listViewPrivilegesAndTraits;

    // TabEquipment
    @FXML public ComboBox<Armor> comboBoxEquipmentHead;
    @FXML public ComboBox<Addon> comboBoxEquipmentNecklace;
    @FXML public ComboBox<Armor> comboBoxEquipmentFullSet;
    @FXML public ComboBox<Armor> comboBoxEquipmentLeftShoulder;
    @FXML public ComboBox<Armor> comboBoxEquipmentLeftArm;
    @FXML public ComboBox<Armor> comboBoxEquipmentLeftForearm;
    @FXML public ComboBox<Armor> comboBoxEquipmentLeftHand;
    @FXML public ComboBox<Addon> comboBoxEquipmentMantle;
    @FXML public ComboBox<Armor> comboBoxEquipmentBack;
    @FXML public ComboBox<Armor> comboBoxEquipmentLeftLeg;
    @FXML public ComboBox<Armor> comboBoxEquipmentLeftKnee;
    @FXML public ComboBox<Armor> comboBoxEquipmentLeftFoot;
    @FXML public ComboBox<Addon> comboBoxEquipmentLeftEarring;
    @FXML public ComboBox<Addon> comboBoxEquipmentLeftBracelet;
    @FXML public ComboBox<Addon> comboBoxEquipmentRing1;
    @FXML public ComboBox<Addon> comboBoxEquipmentRing2;
    @FXML public ComboBox<Armor> comboBoxEquipmentRightShoulder;
    @FXML public ComboBox<Armor> comboBoxEquipmentRightArm;
    @FXML public ComboBox<Armor> comboBoxEquipmentRightForearm;
    @FXML public ComboBox<Armor> comboBoxEquipmentRightHand;
    @FXML public ComboBox<Armor> comboBoxEquipmentChest;
    @FXML public ComboBox<Armor> comboBoxEquipmentAbdomen;
    @FXML public ComboBox<Armor> comboBoxEquipmentRightLeg;
    @FXML public ComboBox<Armor> comboBoxEquipmentRightKnee;
    @FXML public ComboBox<Armor> comboBoxEquipmentRightFoot;
    @FXML public ComboBox<Addon> comboBoxEquipmentRightEarring;
    @FXML public ComboBox<Addon> comboBoxEquipmentRightBracelet;
    @FXML public ComboBox<Addon> comboBoxEquipmentRing3;
    @FXML public ComboBox<Addon> comboBoxEquipmentRing4;
    @FXML public ComboBox<Addon> comboBoxEquipmentBackpack;
    @FXML public ComboBox<Addon> comboBoxEquipmentBandolier;
    @FXML public ComboBox<Addon> comboBoxEquipmentBelt;
    @FXML public ListView<Weapon> listViewEquippedWeapons;

    // TabInventory
    @FXML public Label labelLoadCurrent;
    @FXML public Label labelLoadTotal;
    @FXML public Label labelLoadPercentage;
    @FXML public Label labelLoadStatus;
    @FXML public Spinner<Integer> spinnerMR;
    @FXML public Spinner<Integer> spinnerMA;
    @FXML public Spinner<Integer> spinnerME;
    @FXML public Spinner<Integer> spinnerMO;
    @FXML public Spinner<Integer> spinnerMP;
    @FXML public TextField textFieldElementCode;
    @FXML public TextField textFieldSearchBar;
    @FXML public ComboBox<Category> comboBoxCategory;
    @FXML public ComboBox<EquipmentType> comboBoxEquipmentType;
    @FXML public CheckBox checkBoxShowOwned;
    @FXML public TableView<ElementPreview> tableViewInventory;
    @FXML public TableColumn<ElementPreview, Integer> tableColumnInventoryID;
    @FXML public TableColumn<ElementPreview, String> tableColumnInventoryName;
    @FXML public TableColumn<ElementPreview, Rarity> tableColumnInventoryRarity;
    @FXML public TableColumn<ElementPreview, Double> tableColumnInventoryWeight;
    @FXML public TableColumn<ElementPreview, Integer> tableColumnInventoryCostMR;
    @FXML public TableColumn<ElementPreview, Integer> tableColumnInventoryQuantity;

    // TabSpells
    @FXML public TextField textFieldSpellCasterClass;
    @FXML public ComboBox<String> comboBoxSpellCasterStat;
    @FXML public TextField textFieldSpellSTDC;
    @FXML public TextField textFieldSpellAttackBonus;
    @FXML public ListView<String> listViewSpellCantrips;
    @FXML public Spinner<Integer> spinnerSpell1SlotTot;
    @FXML public Spinner<Integer> spinnerSpell1SlotCurrent;
    @FXML public ListView<String> listViewSpell1;
    @FXML public Spinner<Integer> spinnerSpell2SlotTot;
    @FXML public Spinner<Integer> spinnerSpell2SlotCurrent;
    @FXML public ListView<String> listViewSpell2;
    @FXML public Spinner<Integer> spinnerSpell3SlotTot;
    @FXML public Spinner<Integer> spinnerSpell3SlotCurrent;
    @FXML public ListView<String> listViewSpell3;
    @FXML public Spinner<Integer> spinnerSpell4SlotTot;
    @FXML public Spinner<Integer> spinnerSpell4SlotCurrent;
    @FXML public ListView<String> listViewSpell4;
    @FXML public Spinner<Integer> spinnerSpell5SlotTot;
    @FXML public Spinner<Integer> spinnerSpell5SlotCurrent;
    @FXML public ListView<String> listViewSpell5;
    @FXML public Spinner<Integer> spinnerSpell6SlotTot;
    @FXML public Spinner<Integer> spinnerSpell6SlotCurrent;
    @FXML public ListView<String> listViewSpell6;
    @FXML public Spinner<Integer> spinnerSpell7SlotTot;
    @FXML public Spinner<Integer> spinnerSpell7SlotCurrent;
    @FXML public ListView<String> listViewSpell7;
    @FXML public Spinner<Integer> spinnerSpell8SlotTot;
    @FXML public Spinner<Integer> spinnerSpell8SlotCurrent;
    @FXML public ListView<String> listViewSpell8;
    @FXML public Spinner<Integer> spinnerSpell9SlotTot;
    @FXML public Spinner<Integer> spinnerSpell9SlotCurrent;
    @FXML public ListView<String> listViewSpell9;

    // TabPhysicalDescription
    @FXML public TextField textFieldAge;
    @FXML public TextField textFieldHeight;
    @FXML public TextField textFieldWeight;
    @FXML public TextField textFieldEyes;
    @FXML public TextField textFieldSkin;
    @FXML public TextField textFieldHair;
    @FXML public ImageView imageViewCharacterBodyImage;
    @FXML public TextArea textAreaPhysicalDescription;

    // TabStory
    @FXML public ImageView imageViewSymbolImage;
    @FXML public TextArea textAreaBackstory;
    @FXML public TextArea textAreaCult;
    @FXML public TextArea textAreaAlliesAndOrganizations;

    // TabNotes
    @FXML public TableView<Note> tableViewNotes;
    @FXML public TableColumn<Note, Integer> tableColumnNotesID;
    @FXML public TableColumn<Note, String> tableColumnNotesTitle;
    @FXML public TableColumn<Note, String> tableColumnNotesCreationDate;
    @FXML public TableColumn<Note, String> tableColumnNotesLastEdit;

    // TabEffects
    @FXML public TextField textFieldEffectSearchBar;
    @FXML public CheckBox checkBoxShowActiveEffects;
    @FXML public TableView<EffectPreview> tableViewEffects;
    @FXML public TableColumn<EffectPreview, Integer> tableColumnEffectID;
    @FXML public TableColumn<EffectPreview, String> tableColumnEffectName;
    @FXML public TableColumn<EffectPreview, String> tableColumnEffectDuration;
    @FXML public TableColumn<EffectPreview, Integer> tableColumnEffectIntensity;
    @FXML public TableColumn<EffectPreview, EffectKnowledge> tableColumnEffectIsTreatable;
    @FXML public TableColumn<EffectPreview, EffectKnowledge> tableColumnEffectIsCurable;
    @FXML public TableColumn<EffectPreview, EffectKnowledge> tableColumnEffectIsLethal;
    @FXML public TableColumn<EffectPreview, Boolean> tableColumnEffectIsActive;

    // TabDiceRoller
    @FXML public ImageView imageViewToggleDiceRolling;
    @FXML public ComboBox<DiceRepresentation> comboBoxDiceFaces;
    @FXML public Spinner<Integer> spinnerDiceAmount;
    @FXML public ImageView imageViewDice;
    @FXML public Label labelDiceValue;
    @FXML public TextArea textAreaDiceSum;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        DiscordRichPresenceManager.updateRichPresenceState(DiscordRichPresenceManager.States.IN_GAME);
        TabCharacter.initialize(this);
        TabAbility.initialize(this);
        TabTraitsAndProficiencies.initialize(this);
        TabPrivilegesAndTraits.initialize(this);
        TabEquipment.initialize(this);
        TabInventory.initialize(this);
        TabSpells.initialize(this);
        TabPhysicalDescription.initialize(this);
        TabStory.initialize(this);
        TabNotes.initialize(this);
        TabEffects.initialize(this);
        TabDiceRoller.initialize(this);
        if (Client.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_EVENT_THEME)) {
            Client.getStage().widthProperty().addListener(observable -> imageViewSheetBackground.setFitWidth(Client.getStage().getWidth()));
            Client.getStage().heightProperty().addListener(observable -> imageViewSheetBackground.setFitHeight(Client.getStage().getHeight()));
            activateBackgroundDecoration();
            imageViewSheetBackground.setVisible(true);
        }
    }

    // Decoration
    private void activateBackgroundDecoration() {
        if (CalendarEventManager.isXmasEvent()) {
            enableXmasTheme();
        }
    }
    private void enableXmasTheme() {
        imageViewSheetBackground.setImage(new Image(Defs.Resources.getAsStream(JFXDefs.Resources.GIF.GIF_BACKGROUND_XMAS)));
    }

    // Direct EDT Method Calls
    @FXML private void openCAContextMenu(@NotNull final ContextMenuEvent event) {
        TabCharacter.openCAContextMenu(this, event);
    }
    @FXML private void validateNewCurrentLifeDiceAmount() {
        TabCharacter.validateNewCurrentLifeDiceAmount(this);
    }
    @FXML private void recalculateHealthPercentage() {
        TabCharacter.recalculateHealthPercentage(this);
    }
    @FXML private void openCharacterImageFileChooser() {
        TabCharacter.openCharacterImageFileChooser(this);
    }
    @FXML private void openCharacterBodyImageFileChooser() {
        TabPhysicalDescription.openCharacterBodyImageFileChooser(this);
    }
    @FXML private void openCultImageFileChooser() {
        TabStory.openSymbolImageFileChooser(this);
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
    @FXML private void inventoryDoubleClickEdit(@NotNull final MouseEvent event) {
        if (event.getClickCount() >= 2) TabInventory.editElement(this);
    }
    @FXML private void inventoryDetectEnterOnRow(@NotNull final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && tableViewInventory.getSelectionModel().getSelectedItem() != null) {
            TabInventory.editElement(this);
        }
    }
    @FXML private void notesDoubleClickEdit(@NotNull final MouseEvent event) {
        if (event.getClickCount() >= 2) TabNotes.editNote(this);
    }
    @FXML private void notesDetectEnterOnRow(@NotNull final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && tableViewNotes.getSelectionModel().getSelectedItem() != null) {
            TabNotes.editNote(this);
        }
    }
    @FXML private void search() {
        TabInventory.search(this);
    }
    @FXML private void resetSearchBarAndCategory() {
        TabInventory.resetSearchBarAndCategory(this);
    }
    @FXML private void deleteElement() {
        TabInventory.deleteElement(this);
    }
    @FXML private void editElement() {
        TabInventory.editElement(this);
    }
    @FXML private void addElement() {
        TabInventory.addElement(this);
    }
    @FXML private void addNote() {
        TabNotes.addNote(this);
    }
    @FXML private void deleteNote() {
        TabNotes.deleteNote(this);
    }
    @FXML private void editNote() {
        TabNotes.editNote(this);
    }
    @FXML private void equipmentToggleFullSet() {
        TabEquipment.equipmentToggleFullSet(this);
    }
    @FXML private void updateEquipmentProperties() {
        TabEquipment.updateEquipmentProperties(this);
    }
    @FXML private void importElementFromElementCode() {
        TabInventory.importElementFromElementCode(this);
    }
    @FXML private void importElementFromFile() {
        TabInventory.importElementFromFile(this);
    }
    @FXML private void toggleDiceRolling() {
        TabDiceRoller.toggleDiceRolling(this);
    }
    @FXML private void importInventory() {
        TabInventory.importInventory(this);
    }
    @FXML private void exportInventory() {
        TabInventory.exportInventory();
    }
    @FXML private void settingsBackToMenu() {
        TabSettings.backToMenu();
    }
    @FXML private void settingsQuit() {
        TabSettings.quit();
    }
    @FXML private void searchEffect() {
        TabEffects.searchEffect(this);
    }
    @FXML private void deleteEffect() {
        TabEffects.deleteEffect(this);
    }
    @FXML private void editEffect() {
        TabEffects.editEffect(this);
    }
    @FXML private void addEffect() {
        TabEffects.addEffect(this);
    }
    @FXML private void effectDetectEnterOnRow(@NotNull final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && tableViewEffects.getSelectionModel().getSelectedItem() != null) {
            TabEffects.editEffect(this);
        }
    }
    @FXML private void effectDoubleClickEdit(@NotNull final MouseEvent event) {
        if (event.getClickCount() >= 2) TabEffects.editEffect(this);
    }
    @FXML private void searchPrivilegesAndTraits() {
        TabPrivilegesAndTraits.searchPrivilegesAndTraits(this);
    }
    @FXML private void resetPrivilegesAndTraitsSearchBar() {
        TabPrivilegesAndTraits.resetPrivilegesAndTraitsSearchBar(this);
    }
    @FXML private void deletePrivilegesAndTraits() {
        TabPrivilegesAndTraits.deletePrivilegesAndTraits(this);
    }
    @FXML private void editPrivilegesAndTraits() {
        TabPrivilegesAndTraits.editPrivilegesAndTraits(this);
    }
    @FXML private void addPrivilegesAndTraits() {
        TabPrivilegesAndTraits.addPrivilegesAndTraits(this);
    }
    @FXML private void privilegesAndTraitsDoubleClickEdit(@NotNull final MouseEvent event) {
        if (event.getClickCount() >= 2) TabPrivilegesAndTraits.editPrivilegesAndTraits(this);
    }
    @FXML private void privilegesAndTraitsDetectEnterOnRow(@NotNull final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && listViewPrivilegesAndTraits.getSelectionModel().getSelectedItem() != null) {
            TabPrivilegesAndTraits.editPrivilegesAndTraits(this);
        }
    }
}