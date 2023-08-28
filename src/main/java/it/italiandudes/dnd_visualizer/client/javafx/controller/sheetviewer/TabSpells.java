package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.data.enums.Stats;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.SpinnerValueFactory;
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
        setOnChangeTriggers(controller);
        onLostFocusFireActionEvent(controller);
        updateListViews(controller);
    }

    // OnChange Triggers Setter
    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.comboBoxSpellCasterStat.getSelectionModel().selectedItemProperty().addListener((observable) -> updateSpellModifiers(controller));
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
            controller.spinnerSpell1SlotTot.getEditor().setText(oldValueSpell1Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell1Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell1SlotCurrent.getEditor().getText());
            if (currentSlot < 0) throw new NumberFormatException();
            if (currentSlot > maxSlot) {
                oldValueSpell1Current = String.valueOf(maxSlot);
                controller.spinnerSpell1SlotCurrent.getEditor().setText(String.valueOf(oldValueSpell1Current));
                return;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell1SlotCurrent.getEditor().setText(oldValueSpell1Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell1Current = String.valueOf(currentSlot);
    }
    public static void validateLevel2Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell2SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell2SlotTot.getEditor().setText(oldValueSpell2Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell2Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell2SlotCurrent.getEditor().getText());
            if (currentSlot < 0) throw new NumberFormatException();
            if (currentSlot > maxSlot) {
                oldValueSpell2Current = String.valueOf(maxSlot);
                controller.spinnerSpell2SlotCurrent.getEditor().setText(String.valueOf(oldValueSpell2Current));
                return;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell2SlotCurrent.getEditor().setText(oldValueSpell2Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell2Current = String.valueOf(currentSlot);
    }
    public static void validateLevel3Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell3SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell3SlotTot.getEditor().setText(oldValueSpell3Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell3Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell3SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                oldValueSpell3Current = String.valueOf(maxSlot);
                controller.spinnerSpell3SlotCurrent.getEditor().setText(String.valueOf(oldValueSpell3Current));
                return;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell3SlotCurrent.getEditor().setText(oldValueSpell3Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell3Current = String.valueOf(currentSlot);
    }
    public static void validateLevel4Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell4SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell4SlotTot.getEditor().setText(oldValueSpell4Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell4Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell4SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                oldValueSpell4Current = String.valueOf(maxSlot);
                controller.spinnerSpell4SlotCurrent.getEditor().setText(String.valueOf(oldValueSpell4Current));
                return;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell4SlotCurrent.getEditor().setText(oldValueSpell4Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell4Current = String.valueOf(currentSlot);
    }
    public static void validateLevel5Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell5SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell5SlotTot.getEditor().setText(oldValueSpell5Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell5Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell5SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                oldValueSpell5Current = String.valueOf(maxSlot);
                controller.spinnerSpell5SlotCurrent.getEditor().setText(String.valueOf(oldValueSpell5Current));
                return;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell5SlotCurrent.getEditor().setText(oldValueSpell5Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell5Current = String.valueOf(currentSlot);
    }
    public static void validateLevel6Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell6SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell6SlotTot.getEditor().setText(oldValueSpell6Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell6Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell6SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                oldValueSpell6Current = String.valueOf(maxSlot);
                controller.spinnerSpell6SlotCurrent.getEditor().setText(String.valueOf(oldValueSpell6Current));
                return;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell6SlotCurrent.getEditor().setText(oldValueSpell6Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell6Current = String.valueOf(currentSlot);
    }
    public static void validateLevel7Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell7SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell7SlotTot.getEditor().setText(oldValueSpell7Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell7Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell7SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                oldValueSpell7Current = String.valueOf(maxSlot);
                controller.spinnerSpell7SlotCurrent.getEditor().setText(String.valueOf(oldValueSpell7Current));
                return;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell7SlotCurrent.getEditor().setText(oldValueSpell7Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell7Current = String.valueOf(currentSlot);
    }
    public static void validateLevel8Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell8SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell8SlotTot.getEditor().setText(oldValueSpell8Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell8Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell8SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                oldValueSpell8Current = String.valueOf(maxSlot);
                controller.spinnerSpell8SlotCurrent.getEditor().setText(String.valueOf(oldValueSpell8Current));
                return;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell8SlotCurrent.getEditor().setText(oldValueSpell8Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell8Current = String.valueOf(currentSlot);
    }
    public static void validateLevel9Slots(@NotNull final ControllerSceneSheetViewer controller) {
        int maxSlot;
        try {
            maxSlot = Integer.parseInt(controller.spinnerSpell9SlotTot.getEditor().getText());
            if (maxSlot < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            controller.spinnerSpell9SlotTot.getEditor().setText(oldValueSpell9Tot);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot totali devono essere un numero intero maggiore o uguale a 0.");
            return;
        }
        oldValueSpell9Tot = String.valueOf(maxSlot);
        int currentSlot;
        try {
            currentSlot = Integer.parseInt(controller.spinnerSpell9SlotCurrent.getEditor().getText());
            if (currentSlot > maxSlot) {
                oldValueSpell9Current = String.valueOf(maxSlot);
                controller.spinnerSpell9SlotCurrent.getEditor().setText(String.valueOf(oldValueSpell9Current));
                return;
            }
        } catch (NumberFormatException e) {
            controller.spinnerSpell9SlotCurrent.getEditor().setText(oldValueSpell9Current);
            new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli slot attuali devono essere un numero intero maggiore o uguale a 0 e minore o uguale agli slot massimi.");
            return;
        }
        oldValueSpell9Current = String.valueOf(currentSlot);
    }
    public static void updateListViews(@NotNull final ControllerSceneSheetViewer controller) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {

                        String query = "";

                        return null;
                    }
                };
            }
        }.start();
    }
}
