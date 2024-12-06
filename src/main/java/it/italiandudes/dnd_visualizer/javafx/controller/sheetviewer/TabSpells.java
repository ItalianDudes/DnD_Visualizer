package it.italiandudes.dnd_visualizer.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.javafx.util.SheetDataHandler;
import it.italiandudes.dnd_visualizer.javafx.util.UIElementConfigurator;
import it.italiandudes.dnd_visualizer.data.enums.Category;
import it.italiandudes.dnd_visualizer.data.enums.Stats;
import it.italiandudes.dnd_visualizer.db.DBManager;
import it.italiandudes.dnd_visualizer.utils.Defs.KeyParameters;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.SpinnerValueFactory;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class TabSpells {

    // Old Values
    private static int oldValueSpell1Tot = 0;
    private static int oldValueSpell1Current = 0;
    private static int oldValueSpell2Tot = 0;
    private static int oldValueSpell2Current = 0;
    private static int oldValueSpell3Tot = 0;
    private static int oldValueSpell3Current = 0;
    private static int oldValueSpell4Tot = 0;
    private static int oldValueSpell4Current = 0;
    private static int oldValueSpell5Tot = 0;
    private static int oldValueSpell5Current = 0;
    private static int oldValueSpell6Tot = 0;
    private static int oldValueSpell6Current = 0;
    private static int oldValueSpell7Tot = 0;
    private static int oldValueSpell7Current = 0;
    private static int oldValueSpell8Tot = 0;
    private static int oldValueSpell8Current = 0;
    private static int oldValueSpell9Tot = 0;
    private static int oldValueSpell9Current = 0;

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.comboBoxSpellCasterStat.setItems(FXCollections.observableList(Stats.statsNames));
        controller.textFieldSpellCasterClass.setText(controller.textFieldClass.getText());
        controller.spinnerSpell1SlotTot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell1SlotCurrent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell2SlotTot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell2SlotCurrent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell3SlotTot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell3SlotCurrent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell4SlotTot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell4SlotCurrent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell5SlotTot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell5SlotCurrent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell6SlotTot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell6SlotCurrent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell7SlotTot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell7SlotCurrent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell8SlotTot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell8SlotCurrent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell9SlotTot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell9SlotCurrent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
        controller.spinnerSpell1SlotTot.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell1SlotCurrent.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell2SlotTot.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell2SlotCurrent.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell3SlotTot.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell3SlotCurrent.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell4SlotTot.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell4SlotCurrent.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell5SlotTot.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell5SlotCurrent.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell6SlotTot.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell6SlotCurrent.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell7SlotTot.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell7SlotCurrent.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell8SlotTot.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell8SlotCurrent.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell9SlotTot.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerSpell9SlotCurrent.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        readTabData(controller);
        setOnChangeTriggers(controller);
        onLostFocusFireActionEvent(controller);
        updateListViews(controller);
    }

    // Data Reader
    @SuppressWarnings({"DataFlowIssue", "DuplicatedCode"})
    private static void readTabData(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        int casterStat = -1, slot1Tot = -1, slot1Spent = -1, slot2Tot = -1, slot2Spent = -1,
                        slot3Tot = -1, slot3Spent = -1, slot4Tot = -1, slot4Spent = -1, slot5Tot = -1,
                        slot5Spent = -1, slot6Tot = -1, slot6Spent = -1, slot7Tot = -1, slot7Spent = -1,
                        slot8Tot = -1, slot8Spent = -1, slot9Tot = -1, slot9Spent = -1;
                        try {
                            casterStat = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.CASTER_STAT));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot1Tot = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_1));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot1Spent = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_1));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot2Tot = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_2));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot2Spent = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_2));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot3Tot = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_3));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot3Spent = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_3));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot4Tot = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_4));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot4Spent = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_4));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot5Tot = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_5));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot5Spent = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_5));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot6Tot = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_6));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot6Spent = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_6));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot7Tot = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_7));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot7Spent = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_7));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot8Tot = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_8));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot8Spent = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_8));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot9Tot = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_9));
                        } catch (NumberFormatException ignored) {}
                        try {
                            slot9Spent = Integer.parseInt(SheetDataHandler.readKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_9));
                        } catch (NumberFormatException ignored) {}
                        int finalSlot1Tot = slot1Tot;
                        int finalSlot1Spent = slot1Spent;
                        int finalSlot2Tot = slot2Tot;
                        int finalSlot2Spent = slot2Spent;
                        int finalSlot3Tot = slot3Tot;
                        int finalSlot3Spent = slot3Spent;
                        int finalSlot4Tot = slot4Tot;
                        int finalSlot4Spent = slot4Spent;
                        int finalSlot5Tot = slot5Tot;
                        int finalSlot5Spent = slot5Spent;
                        int finalSlot6Tot = slot6Tot;
                        int finalSlot6Spent = slot6Spent;
                        int finalSlot7Tot = slot7Tot;
                        int finalSlot7Spent = slot7Spent;
                        int finalSlot8Tot = slot8Tot;
                        int finalSlot8Spent = slot8Spent;
                        int finalSlot9Tot = slot9Tot;
                        int finalSlot9Spent = slot9Spent;
                        int finalCasterStat = casterStat;
                        Platform.runLater(() -> {
                            if (finalCasterStat >= 0) controller.comboBoxSpellCasterStat.getSelectionModel().select(finalCasterStat);
                            if (finalSlot1Tot >= 0) controller.spinnerSpell1SlotTot.getValueFactory().setValue(finalSlot1Tot);
                            if (finalSlot1Spent >= 0) controller.spinnerSpell1SlotCurrent.getValueFactory().setValue(finalSlot1Spent);
                            if (finalSlot2Tot >= 0) controller.spinnerSpell2SlotTot.getValueFactory().setValue(finalSlot2Tot);
                            if (finalSlot2Spent >= 0) controller.spinnerSpell2SlotCurrent.getValueFactory().setValue(finalSlot2Spent);
                            if (finalSlot3Tot >= 0) controller.spinnerSpell3SlotTot.getValueFactory().setValue(finalSlot3Tot);
                            if (finalSlot3Spent >= 0) controller.spinnerSpell3SlotCurrent.getValueFactory().setValue(finalSlot3Spent);
                            if (finalSlot4Tot >= 0) controller.spinnerSpell4SlotTot.getValueFactory().setValue(finalSlot4Tot);
                            if (finalSlot4Spent >= 0) controller.spinnerSpell4SlotCurrent.getValueFactory().setValue(finalSlot4Spent);
                            if (finalSlot5Tot >= 0) controller.spinnerSpell5SlotTot.getValueFactory().setValue(finalSlot5Tot);
                            if (finalSlot5Spent >= 0) controller.spinnerSpell5SlotCurrent.getValueFactory().setValue(finalSlot5Spent);
                            if (finalSlot6Tot >= 0) controller.spinnerSpell6SlotTot.getValueFactory().setValue(finalSlot6Tot);
                            if (finalSlot6Spent >= 0) controller.spinnerSpell6SlotCurrent.getValueFactory().setValue(finalSlot6Spent);
                            if (finalSlot7Tot >= 0) controller.spinnerSpell7SlotTot.getValueFactory().setValue(finalSlot7Tot);
                            if (finalSlot7Spent >= 0) controller.spinnerSpell7SlotCurrent.getValueFactory().setValue(finalSlot7Spent);
                            if (finalSlot8Tot >= 0) controller.spinnerSpell8SlotTot.getValueFactory().setValue(finalSlot8Tot);
                            if (finalSlot8Spent >= 0) controller.spinnerSpell8SlotCurrent.getValueFactory().setValue(finalSlot8Spent);
                            if (finalSlot9Tot >= 0) controller.spinnerSpell9SlotTot.getValueFactory().setValue(finalSlot9Tot);
                            if (finalSlot9Spent >= 0) controller.spinnerSpell9SlotCurrent.getValueFactory().setValue(finalSlot9Spent);
                        });
                        return null;
                    }
                };
            }
        }.start();
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.comboBoxSpellCasterStat.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateSpellModifiers(controller);
            SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.CASTER_STAT, String.valueOf(newValue.intValue()));
        });
        controller.spinnerSpell1SlotTot.getEditor().textProperty().addListener((observable -> validateLevel1Slots(controller)));
        controller.spinnerSpell1SlotCurrent.getEditor().textProperty().addListener((observable -> validateLevel1Slots(controller)));
        controller.spinnerSpell2SlotTot.getEditor().textProperty().addListener((observable -> validateLevel2Slots(controller)));
        controller.spinnerSpell2SlotCurrent.getEditor().textProperty().addListener((observable -> validateLevel2Slots(controller)));
        controller.spinnerSpell3SlotTot.getEditor().textProperty().addListener((observable -> validateLevel3Slots(controller)));
        controller.spinnerSpell3SlotCurrent.getEditor().textProperty().addListener((observable -> validateLevel3Slots(controller)));
        controller.spinnerSpell4SlotTot.getEditor().textProperty().addListener((observable -> validateLevel4Slots(controller)));
        controller.spinnerSpell4SlotCurrent.getEditor().textProperty().addListener((observable -> validateLevel4Slots(controller)));
        controller.spinnerSpell5SlotTot.getEditor().textProperty().addListener((observable -> validateLevel5Slots(controller)));
        controller.spinnerSpell5SlotCurrent.getEditor().textProperty().addListener((observable -> validateLevel5Slots(controller)));
        controller.spinnerSpell6SlotTot.getEditor().textProperty().addListener((observable -> validateLevel6Slots(controller)));
        controller.spinnerSpell6SlotCurrent.getEditor().textProperty().addListener((observable -> validateLevel6Slots(controller)));
        controller.spinnerSpell7SlotTot.getEditor().textProperty().addListener((observable -> validateLevel7Slots(controller)));
        controller.spinnerSpell7SlotCurrent.getEditor().textProperty().addListener((observable -> validateLevel7Slots(controller)));
        controller.spinnerSpell8SlotTot.getEditor().textProperty().addListener((observable -> validateLevel8Slots(controller)));
        controller.spinnerSpell8SlotCurrent.getEditor().textProperty().addListener((observable -> validateLevel8Slots(controller)));
        controller.spinnerSpell9SlotTot.getEditor().textProperty().addListener((observable -> validateLevel9Slots(controller)));
        controller.spinnerSpell9SlotCurrent.getEditor().textProperty().addListener((observable -> validateLevel9Slots(controller)));
    }

    // Lost Focus On Action Fire Event
    private static void onLostFocusFireActionEvent(@NotNull final ControllerSceneSheetViewer controller) {
        controller.spinnerSpell1SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel1Slots(controller);
        });
        controller.spinnerSpell1SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel1Slots(controller);
        });
        controller.spinnerSpell2SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel2Slots(controller);
        });
        controller.spinnerSpell2SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel2Slots(controller);
        });
        controller.spinnerSpell3SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel3Slots(controller);
        });
        controller.spinnerSpell3SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel3Slots(controller);
        });
        controller.spinnerSpell4SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel4Slots(controller);
        });
        controller.spinnerSpell4SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel4Slots(controller);
        });
        controller.spinnerSpell5SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel5Slots(controller);
        });
        controller.spinnerSpell5SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel5Slots(controller);
        });
        controller.spinnerSpell6SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel6Slots(controller);
        });
        controller.spinnerSpell6SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel6Slots(controller);
        });
        controller.spinnerSpell7SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel7Slots(controller);
        });
        controller.spinnerSpell7SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel7Slots(controller);
        });
        controller.spinnerSpell8SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel8Slots(controller);
        });
        controller.spinnerSpell8SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel8Slots(controller);
        });
        controller.spinnerSpell9SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel9Slots(controller);
        });
        controller.spinnerSpell9SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel9Slots(controller);
        });
    }

    // EDT
    public static void updateSpellModifiers(@NotNull final ControllerSceneSheetViewer controller) {
        if (controller.comboBoxSpellCasterStat.getSelectionModel().isEmpty()) return;
        Stats stat = Stats.values()[Stats.statsNames.indexOf(controller.comboBoxSpellCasterStat.getSelectionModel().getSelectedItem())];
        String modStr = null;
        switch (stat) {
            case STRENGTH:
                modStr = controller.labelModStrength.getText();
                break;

            case DEXTERITY:
                modStr = controller.labelModDexterity.getText();
                break;

            case CONSTITUTION:
                modStr = controller.labelModConstitution.getText();
                break;

            case INTELLIGENCE:
                modStr = controller.labelModIntelligence.getText();
                break;

            case WISDOM:
                modStr = controller.labelModWisdom.getText();
                break;

            case CHARISMA:
                modStr = controller.labelModCharisma.getText();
                break;
        }
        int mod = Integer.parseInt(modStr);
        int profBonus = controller.spinnerProficiencyBonus.getValue();
        int spellDC = 8 + mod + profBonus;
        int spellAttackMod = mod + profBonus;
        controller.textFieldSpellSTDC.setText(String.valueOf(spellDC));
        controller.textFieldSpellAttackBonus.setText(String.valueOf(spellAttackMod));
    }
    public static void validateLevel1Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell1SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell1SlotTot.getValueFactory().setValue(oldValueSpell1Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        controller.spinnerSpell1SlotTot.getValueFactory().setValue(maxSlot);
        oldValueSpell1Tot = maxSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_1, String.valueOf(maxSlot));
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell1SlotCurrent.getEditor().getText());
            if (currentSlot < 0) throw new NumberFormatException();
            if (currentSlot > maxSlot) {
                currentSlot = maxSlot;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell1SlotCurrent.getValueFactory().setValue(oldValueSpell1Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        controller.spinnerSpell1SlotCurrent.getValueFactory().setValue(currentSlot);
        oldValueSpell1Current = currentSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_1, String.valueOf(currentSlot));
    }
    public static void validateLevel2Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell2SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell2SlotTot.getValueFactory().setValue(oldValueSpell2Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        controller.spinnerSpell2SlotTot.getValueFactory().setValue(maxSlot);
        oldValueSpell2Tot = maxSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_2, String.valueOf(maxSlot));
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell2SlotCurrent.getEditor().getText());
            if (currentSlot < 0) throw new NumberFormatException();
            if (currentSlot > maxSlot) {
                currentSlot = maxSlot;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell2SlotCurrent.getValueFactory().setValue(oldValueSpell2Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        controller.spinnerSpell2SlotCurrent.getValueFactory().setValue(currentSlot);
        oldValueSpell2Current = currentSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_2, String.valueOf(currentSlot));
    }
    public static void validateLevel3Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell3SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell3SlotTot.getValueFactory().setValue(oldValueSpell3Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        controller.spinnerSpell3SlotTot.getValueFactory().setValue(maxSlot);
        oldValueSpell3Tot = maxSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_3, String.valueOf(maxSlot));
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell3SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                currentSlot = maxSlot;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell3SlotCurrent.getValueFactory().setValue(oldValueSpell3Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        controller.spinnerSpell3SlotCurrent.getValueFactory().setValue(currentSlot);
        oldValueSpell3Current = currentSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_3, String.valueOf(currentSlot));
    }
    public static void validateLevel4Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell4SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell4SlotTot.getValueFactory().setValue(oldValueSpell4Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        controller.spinnerSpell4SlotTot.getValueFactory().setValue(maxSlot);
        oldValueSpell4Tot = maxSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_4, String.valueOf(maxSlot));
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell4SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                currentSlot = maxSlot;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell4SlotCurrent.getValueFactory().setValue(oldValueSpell4Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        controller.spinnerSpell4SlotCurrent.getValueFactory().setValue(currentSlot);
        oldValueSpell4Current = currentSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_4, String.valueOf(currentSlot));
    }
    public static void validateLevel5Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell5SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell5SlotTot.getValueFactory().setValue(oldValueSpell5Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        controller.spinnerSpell5SlotTot.getValueFactory().setValue(maxSlot);
        oldValueSpell5Tot = maxSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_5, String.valueOf(maxSlot));
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell5SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                currentSlot = maxSlot;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell5SlotCurrent.getValueFactory().setValue(oldValueSpell5Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        controller.spinnerSpell5SlotCurrent.getValueFactory().setValue(currentSlot);
        oldValueSpell5Current = currentSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_5, String.valueOf(currentSlot));
    }
    public static void validateLevel6Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell6SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell6SlotTot.getValueFactory().setValue(oldValueSpell6Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        controller.spinnerSpell6SlotTot.getValueFactory().setValue(maxSlot);
        oldValueSpell6Tot = maxSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_6, String.valueOf(maxSlot));
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell6SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                currentSlot = maxSlot;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell6SlotCurrent.getValueFactory().setValue(oldValueSpell6Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        controller.spinnerSpell6SlotCurrent.getValueFactory().setValue(currentSlot);
        oldValueSpell6Current = currentSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_6, String.valueOf(currentSlot));
    }
    public static void validateLevel7Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell7SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell7SlotTot.getValueFactory().setValue(oldValueSpell7Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        controller.spinnerSpell7SlotTot.getValueFactory().setValue(maxSlot);
        oldValueSpell7Tot = maxSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_7, String.valueOf(maxSlot));
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell7SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                currentSlot = maxSlot;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell7SlotCurrent.getValueFactory().setValue(oldValueSpell7Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        controller.spinnerSpell7SlotCurrent.getValueFactory().setValue(currentSlot);
        oldValueSpell7Current = currentSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_7, String.valueOf(currentSlot));
    }
    public static void validateLevel8Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell8SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell8SlotTot.getValueFactory().setValue(oldValueSpell8Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        controller.spinnerSpell8SlotTot.getValueFactory().setValue(maxSlot);
        oldValueSpell8Tot = maxSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_8, String.valueOf(maxSlot));
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell8SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                currentSlot = maxSlot;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell8SlotCurrent.getValueFactory().setValue(oldValueSpell8Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        controller.spinnerSpell8SlotCurrent.getValueFactory().setValue(currentSlot);
        oldValueSpell8Current = currentSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_8, String.valueOf(currentSlot));
    }
    public static void validateLevel9Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell9SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell9SlotTot.getValueFactory().setValue(oldValueSpell9Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        controller.spinnerSpell9SlotTot.getValueFactory().setValue(maxSlot);
        oldValueSpell9Tot = maxSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_TOTAL_9, String.valueOf(maxSlot));
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell9SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                currentSlot = maxSlot;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell9SlotCurrent.getValueFactory().setValue(oldValueSpell9Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        controller.spinnerSpell9SlotCurrent.getValueFactory().setValue(currentSlot);
        oldValueSpell9Current = currentSlot;
        SheetDataHandler.writeKeyParameter(KeyParameters.TabSpells.SLOT_SPENT_9, String.valueOf(currentSlot));
    }
    public static void updateListViews(@NotNull final ControllerSceneSheetViewer controller) {
        controller.listViewSpellCantrips.getItems().clear();
        controller.listViewSpell1.getItems().clear();
        controller.listViewSpell2.getItems().clear();
        controller.listViewSpell3.getItems().clear();
        controller.listViewSpell4.getItems().clear();
        controller.listViewSpell5.getItems().clear();
        controller.listViewSpell6.getItems().clear();
        controller.listViewSpell7.getItems().clear();
        controller.listViewSpell8.getItems().clear();
        controller.listViewSpell9.getItems().clear();
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        PreparedStatement ps = null;
                        try {
                            String query = "SELECT i.name AS name, s.level AS level FROM items AS i JOIN spells AS s ON i.id = s.item_id WHERE i.category=?;";
                            ps = DBManager.preparedStatement(query);
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            ps.setInt(1, Category.SPELL.getDatabaseValue());
                            ResultSet result = ps.executeQuery();
                            while (result.next()) {
                                String name = result.getString("name");
                                int level = result.getInt("level");
                                switch (level) {
                                    case 0:
                                        Platform.runLater(() -> controller.listViewSpellCantrips.getItems().add(name));
                                        break;
                                    case 1:
                                        Platform.runLater(() -> controller.listViewSpell1.getItems().add(name));
                                        break;
                                    case 2:
                                        Platform.runLater(() -> controller.listViewSpell2.getItems().add(name));
                                        break;
                                    case 3:
                                        Platform.runLater(() -> controller.listViewSpell3.getItems().add(name));
                                        break;
                                    case 4:
                                        Platform.runLater(() -> controller.listViewSpell4.getItems().add(name));
                                        break;
                                    case 5:
                                        Platform.runLater(() -> controller.listViewSpell5.getItems().add(name));
                                        break;
                                    case 6:
                                        Platform.runLater(() -> controller.listViewSpell6.getItems().add(name));
                                        break;
                                    case 7:
                                        Platform.runLater(() -> controller.listViewSpell7.getItems().add(name));
                                        break;
                                    case 8:
                                        Platform.runLater(() -> controller.listViewSpell8.getItems().add(name));
                                        break;
                                    case 9:
                                        Platform.runLater(() -> controller.listViewSpell9.getItems().add(name));
                                        break;
                                    default:
                                        if (level < 0) {
                                            Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DATI", "Incantesimo: "+name+"\nIl livello di un incantesimo non puo' essere inferiore a 0."));
                                            return null;
                                        }
                                }
                            }
                            ps.close();
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
}
