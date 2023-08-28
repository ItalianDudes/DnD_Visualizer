package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.data.enums.Stats;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

public final class TabSpells {

    // Attributes

    // Old Values
    private static String oldValueSpell1Tot = "0";
    private static String oldValueSpell1Current = "0";
    private static String oldValueSpell2Tot = "0";
    private static String oldValueSpell2Current = "0";
    private static String oldValueSpell3Tot = "0";
    private static String oldValueSpell3Current = "0";
    private static String oldValueSpell4Tot = "0";
    private static String oldValueSpell4Current = "0";
    private static String oldValueSpell5Tot = "0";
    private static String oldValueSpell5Current = "0";
    private static String oldValueSpell6Tot = "0";
    private static String oldValueSpell6Current = "0";
    private static String oldValueSpell7Tot = "0";
    private static String oldValueSpell7Current = "0";
    private static String oldValueSpell8Tot = "0";
    private static String oldValueSpell8Current = "0";
    private static String oldValueSpell9Tot = "0";
    private static String oldValueSpell9Current = "0";

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        setOnChangeTriggers(controller);
        onLostFocusFireActionEvent(controller);
        controller.comboBoxSpellCasterStat.setItems(FXCollections.observableList(Stats.statsNames));
        controller.textFieldSpellCasterClass.setText(controller.textFieldClass.getText());
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.comboBoxSpellCasterStat.getSelectionModel().selectedItemProperty().addListener((observable) -> updateSpellModifiers(controller));
        controller.textFieldSpell1SlotTot.textProperty().addListener((observable -> validateLevel1Slots(controller)));
        controller.textFieldSpell1SlotCurrent.textProperty().addListener((observable -> validateLevel1Slots(controller)));
        controller.textFieldSpell2SlotTot.textProperty().addListener((observable -> validateLevel2Slots(controller)));
        controller.textFieldSpell2SlotCurrent.textProperty().addListener((observable -> validateLevel2Slots(controller)));
        controller.textFieldSpell3SlotTot.textProperty().addListener((observable -> validateLevel3Slots(controller)));
        controller.textFieldSpell3SlotCurrent.textProperty().addListener((observable -> validateLevel3Slots(controller)));
        controller.textFieldSpell4SlotTot.textProperty().addListener((observable -> validateLevel4Slots(controller)));
        controller.textFieldSpell4SlotCurrent.textProperty().addListener((observable -> validateLevel4Slots(controller)));
        controller.textFieldSpell5SlotTot.textProperty().addListener((observable -> validateLevel5Slots(controller)));
        controller.textFieldSpell5SlotCurrent.textProperty().addListener((observable -> validateLevel5Slots(controller)));
        controller.textFieldSpell6SlotTot.textProperty().addListener((observable -> validateLevel6Slots(controller)));
        controller.textFieldSpell6SlotCurrent.textProperty().addListener((observable -> validateLevel6Slots(controller)));
        controller.textFieldSpell7SlotTot.textProperty().addListener((observable -> validateLevel7Slots(controller)));
        controller.textFieldSpell7SlotCurrent.textProperty().addListener((observable -> validateLevel7Slots(controller)));
        controller.textFieldSpell8SlotTot.textProperty().addListener((observable -> validateLevel8Slots(controller)));
        controller.textFieldSpell8SlotCurrent.textProperty().addListener((observable -> validateLevel8Slots(controller)));
        controller.textFieldSpell9SlotTot.textProperty().addListener((observable -> validateLevel9Slots(controller)));
        controller.textFieldSpell9SlotCurrent.textProperty().addListener((observable -> validateLevel9Slots(controller)));
    }

    // Lost Focus On Action Fire Event
    private static void onLostFocusFireActionEvent(@NotNull final ControllerSceneSheetViewer controller) {
        controller.textFieldSpell1SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel1Slots(controller);
        });
        controller.textFieldSpell1SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel1Slots(controller);
        });
        controller.textFieldSpell2SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel2Slots(controller);
        });
        controller.textFieldSpell2SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel2Slots(controller);
        });
        controller.textFieldSpell3SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel3Slots(controller);
        });
        controller.textFieldSpell3SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel3Slots(controller);
        });
        controller.textFieldSpell4SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel4Slots(controller);
        });
        controller.textFieldSpell4SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel4Slots(controller);
        });
        controller.textFieldSpell5SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel5Slots(controller);
        });
        controller.textFieldSpell5SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel5Slots(controller);
        });
        controller.textFieldSpell6SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel6Slots(controller);
        });
        controller.textFieldSpell6SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel6Slots(controller);
        });
        controller.textFieldSpell7SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel7Slots(controller);
        });
        controller.textFieldSpell7SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel7Slots(controller);
        });
        controller.textFieldSpell8SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel8Slots(controller);
        });
        controller.textFieldSpell8SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel8Slots(controller);
        });
        controller.textFieldSpell9SlotTot.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) validateLevel9Slots(controller);
        });
        controller.textFieldSpell9SlotCurrent.focusedProperty().addListener((observable, oldValue, newValue) -> {
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
            maxSlot = Integer.parseInt(controller.textFieldSpell1SlotTot.getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell1SlotTot.setText(oldValueSpell1Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell1Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.textFieldSpell1SlotCurrent.getText());
            if (currentSlot < 0 || currentSlot > maxSlot) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell1SlotCurrent.setText(oldValueSpell1Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell1Current = String.valueOf(currentSlot);
    }
    public static void validateLevel2Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.textFieldSpell2SlotTot.getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell2SlotTot.setText(oldValueSpell2Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell2Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.textFieldSpell2SlotCurrent.getText());
            if (currentSlot < 0 || currentSlot > maxSlot) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell2SlotCurrent.setText(oldValueSpell2Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell2Current = String.valueOf(currentSlot);
    }
    public static void validateLevel3Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.textFieldSpell3SlotTot.getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell3SlotTot.setText(oldValueSpell3Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell3Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.textFieldSpell3SlotCurrent.getText());
            if (currentSlot < 0 || currentSlot > maxSlot) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell3SlotCurrent.setText(oldValueSpell3Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell3Current = String.valueOf(currentSlot);
    }
    public static void validateLevel4Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.textFieldSpell4SlotTot.getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell4SlotTot.setText(oldValueSpell4Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell4Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.textFieldSpell4SlotCurrent.getText());
            if (currentSlot < 0 || currentSlot > maxSlot) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell4SlotCurrent.setText(oldValueSpell4Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell4Current = String.valueOf(currentSlot);
    }
    public static void validateLevel5Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.textFieldSpell5SlotTot.getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell5SlotTot.setText(oldValueSpell5Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell5Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.textFieldSpell5SlotCurrent.getText());
            if (currentSlot < 0 || currentSlot > maxSlot) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell5SlotCurrent.setText(oldValueSpell5Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell5Current = String.valueOf(currentSlot);
    }
    public static void validateLevel6Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.textFieldSpell6SlotTot.getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell6SlotTot.setText(oldValueSpell6Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell6Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.textFieldSpell6SlotCurrent.getText());
            if (currentSlot < 0 || currentSlot > maxSlot) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell6SlotCurrent.setText(oldValueSpell6Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell6Current = String.valueOf(currentSlot);
    }
    public static void validateLevel7Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.textFieldSpell7SlotTot.getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell7SlotTot.setText(oldValueSpell7Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell7Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.textFieldSpell7SlotCurrent.getText());
            if (currentSlot < 0 || currentSlot > maxSlot) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell7SlotCurrent.setText(oldValueSpell7Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell7Current = String.valueOf(currentSlot);
    }
    public static void validateLevel8Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.textFieldSpell8SlotTot.getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell8SlotTot.setText(oldValueSpell8Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell8Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.textFieldSpell8SlotCurrent.getText());
            if (currentSlot < 0 || currentSlot > maxSlot) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell8SlotCurrent.setText(oldValueSpell8Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell8Current = String.valueOf(currentSlot);
    }
    public static void validateLevel9Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.textFieldSpell9SlotTot.getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell9SlotTot.setText(oldValueSpell9Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell9Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.textFieldSpell9SlotCurrent.getText());
            if (currentSlot < 0 || currentSlot > maxSlot) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.textFieldSpell9SlotCurrent.setText(oldValueSpell9Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell9Current = String.valueOf(currentSlot);
    }
}
