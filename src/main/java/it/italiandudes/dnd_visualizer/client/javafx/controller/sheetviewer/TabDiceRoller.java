package it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer;

import it.italiandudes.dnd_visualizer.DnD_Visualizer;
import it.italiandudes.dnd_visualizer.client.javafx.JFXDefs;
import it.italiandudes.dnd_visualizer.client.javafx.controller.ControllerSceneSheetViewer;
import it.italiandudes.dnd_visualizer.client.javafx.util.UIElementConfigurator;
import it.italiandudes.dnd_visualizer.data.enums.DiceRepresentation;
import it.italiandudes.dnd_visualizer.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

public final class TabDiceRoller {

    // Attributes
    private static boolean diceRolling = false;

    // Play | Stop
    private static final Image PLAY = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.IMAGE_PLAY));
    private static final Image STOP = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.IMAGE_STOP));

    // Dices
    private static final Image HEAD = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.Dice.HEAD));
    private static final Image TAIL = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.Dice.TAIL));
    private static final Image D4 = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.Dice.D4));
    private static final Image D6 = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.Dice.D6));
    private static final Image D8 = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.Dice.D8));
    private static final Image D10 = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.Dice.D10));
    private static final Image D12 = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.Dice.D12));
    private static final Image D20 = new Image(Defs.Resources.getAsStream(JFXDefs.Resources.Image.Dice.D20));
    private static final Image D100 = D6;

    // Initialize
    public static void initialize(@NotNull final ControllerSceneSheetViewer controller) {
        controller.imageViewToggleDiceRolling.setImage(PLAY);
        controller.comboBoxDiceFaces.setItems(FXCollections.observableList(DiceRepresentation.DICES));
        controller.comboBoxDiceFaces.getSelectionModel().select(DiceRepresentation.D20);
        controller.spinnerDiceAmount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1));
        controller.spinnerDiceAmount.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        controller.spinnerDiceAmount.getEditor().setText("1");
        setOnChangeTriggers(controller);

    }

    private static void setOnChangeTriggers(@NotNull final ControllerSceneSheetViewer controller) {
        controller.comboBoxDiceFaces.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case COIN:
                    if (DnD_Visualizer.RANDOMIZER.nextBoolean()) {
                        Platform.runLater(() -> controller.imageViewDice.setImage(HEAD));
                    } else {
                        Platform.runLater(() -> controller.imageViewDice.setImage(TAIL));
                    }
                    break;
                case D4:
                    controller.imageViewDice.setImage(D4);
                    break;
                case D6:
                    controller.imageViewDice.setImage(D6);
                    break;
                case D8:
                    controller.imageViewDice.setImage(D8);
                    break;
                case D10:
                    controller.imageViewDice.setImage(D10);
                    break;
                case D12:
                    controller.imageViewDice.setImage(D12);
                    break;
                case D20:
                    controller.imageViewDice.setImage(D20);
                    break;
                case D100:
                    controller.imageViewDice.setImage(D100);
                    break;
                default:
                    throw new IllegalArgumentException("Choice not valid");
            }
        });
    }

    // EDT
    public static void toggleDiceRolling(@NotNull final ControllerSceneSheetViewer controller) {
        if (diceRolling) {
            controller.imageViewToggleDiceRolling.setImage(PLAY);
            controller.comboBoxDiceFaces.setDisable(false);
            controller.spinnerDiceAmount.setDisable(false);
            controller.labelDiceValue.setText("");
            controller.textAreaDiceSum.setText("");
            diceRolling = false;
        } else {
            controller.imageViewToggleDiceRolling.setImage(STOP);
            controller.comboBoxDiceFaces.setDisable(true);
            controller.spinnerDiceAmount.setDisable(true);
            doDiceRoll(controller);
        }
    }
    private static void doDiceRoll(@NotNull final ControllerSceneSheetViewer controller) {
        if (diceRolling) return;
        diceRolling = true;
        DiceRepresentation diceRepresentation = controller.comboBoxDiceFaces.getSelectionModel().getSelectedItem();
        int amount = Integer.parseInt(controller.spinnerDiceAmount.getEditor().getText());
        controller.textAreaDiceSum.setText("");
        controller.labelDiceValue.setText("");
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            int sum = 0;
                            int result = 0;
                            StringBuilder sumBuilder = new StringBuilder();
                            for (int i = 0; i < amount; i++) {
                                for (int j = 0; j < 5; j++) {
                                    if (!diceRolling) return null;
                                    result = DnD_Visualizer.RANDOMIZER.nextInt(diceRepresentation.getFaces()) + 1;
                                    int finalResult = result;
                                    if (DiceRepresentation.COIN.getFaces() != diceRepresentation.getFaces()) Platform.runLater(() -> controller.labelDiceValue.setText(String.valueOf(finalResult)));
                                    else Platform.runLater(() -> controller.imageViewDice.setImage(finalResult == 1 ? HEAD : TAIL));
                                    Thread.sleep(175);
                                }
                                Thread.sleep(1000);
                                if (!diceRolling) return null;
                                sum += result;
                                if (sumBuilder.toString().isEmpty()) {
                                    if (DiceRepresentation.COIN.getFaces() == diceRepresentation.getFaces()) {
                                        if (result == 1) sumBuilder.append("TESTA");
                                        else sumBuilder.append("CROCE");
                                    } else sumBuilder.append(result);
                                } else {
                                    if (DiceRepresentation.COIN.getFaces() == diceRepresentation.getFaces()) {
                                        sumBuilder.append(" + ");
                                        if (result == 1) sumBuilder.append("TESTA");
                                        else sumBuilder.append("CROCE");
                                    } else sumBuilder.append(" + ").append(result);
                                }
                                int finalResult1 = result;
                                Platform.runLater(() -> {
                                    if (DiceRepresentation.COIN.getFaces() != diceRepresentation.getFaces()) controller.labelDiceValue.setText(String.valueOf(finalResult1));
                                    controller.textAreaDiceSum.setText(sumBuilder.toString());
                                });
                            }

                            if (DiceRepresentation.COIN.getFaces() != diceRepresentation.getFaces()) {
                                sumBuilder.append(" = ").append(sum);
                                Platform.runLater(() -> controller.textAreaDiceSum.setText(sumBuilder.toString()));
                            }
                        } catch (Exception e) {
                            Logger.log(e);
                        }
                        diceRolling = false;
                        Platform.runLater(() -> {
                            controller.comboBoxDiceFaces.setDisable(false);
                            controller.spinnerDiceAmount.setDisable(false);
                            controller.imageViewToggleDiceRolling.setImage(PLAY);
                        });
                        return null;
                    }
                };
            }
        }.start();
    }
}
