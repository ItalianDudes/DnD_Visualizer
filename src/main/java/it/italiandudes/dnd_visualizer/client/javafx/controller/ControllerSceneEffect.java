package it.italiandudes.dnd_visualizer.client.javafx.controller;

import it.italiandudes.dnd_visualizer.client.javafx.Client;
import it.italiandudes.dnd_visualizer.client.javafx.alert.ErrorAlert;
import it.italiandudes.dnd_visualizer.client.javafx.alert.InformationAlert;
import it.italiandudes.dnd_visualizer.client.javafx.controller.sheetviewer.TabEffects;
import it.italiandudes.dnd_visualizer.client.javafx.util.UIElementConfigurator;
import it.italiandudes.dnd_visualizer.data.effect.Effect;
import it.italiandudes.dnd_visualizer.data.enums.EffectKnowledge;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jetbrains.annotations.NotNull;

public final class ControllerSceneEffect {

    // Attributes
    private Effect effect = null;

    // Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private ComboBox<EffectKnowledge> comboBoxIsTreatable;
    @FXML private ComboBox<EffectKnowledge> comboBoxIsCurable;
    @FXML private ComboBox<EffectKnowledge> comboBoxIsLethal;
    @FXML private TextField textFieldDuration;
    @FXML private Spinner<Integer> spinnerIntensity;
    @FXML private TextField textFieldEffectCA;
    @FXML private TextField textFieldEffectLife;
    @FXML private TextField textFieldEffectLoad;
    @FXML private TextField textFieldEffectLifePerc;
    @FXML private TextField textFieldEffectLoadPerc;
    @FXML private TextArea textAreaOtherEffects;
    @FXML private TextArea textAreaDescription;
    @FXML private CheckBox checkBoxIsEffectActive;

    // Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        spinnerIntensity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1));
        spinnerIntensity.getEditor().setTextFormatter(UIElementConfigurator.configureNewIntegerTextFormatter());
        comboBoxIsTreatable.setItems(FXCollections.observableList(EffectKnowledge.knowledge));
        comboBoxIsTreatable.getSelectionModel().selectFirst();
        comboBoxIsCurable.setItems(FXCollections.observableList(EffectKnowledge.knowledge));
        comboBoxIsCurable.getSelectionModel().selectFirst();
        comboBoxIsLethal.setItems(FXCollections.observableList(EffectKnowledge.knowledge));
        comboBoxIsLethal.getSelectionModel().selectFirst();
        String effectName = TabEffects.getEffectName();
        if (effectName != null) initExistingEffect(effectName);
    }

    // EDT
    @FXML
    private void save() {
        if (textFieldName.getText().replace(" ", "").isEmpty()) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un nome all'effetto.");
            return;
        }
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override @SuppressWarnings("DuplicatedCode")
                    protected Void call() {
                        try {
                            String oldName = null;
                            String duration = textFieldDuration.getText();
                            int intensity = spinnerIntensity.getValueFactory().getValue();
                            EffectKnowledge isTreatable = comboBoxIsTreatable.getSelectionModel().getSelectedItem();
                            EffectKnowledge isCurable = comboBoxIsCurable.getSelectionModel().getSelectedItem();
                            EffectKnowledge isLethal = comboBoxIsLethal.getSelectionModel().getSelectedItem();
                            int lifeEffect, loadEffect, caEffect;
                            double lifeEffectPerc, loadEffectPerc;
                            try {
                                lifeEffect = Integer.parseInt(textFieldEffectLife.getText());
                                loadEffect = Integer.parseInt(textFieldEffectLoad.getText());
                                caEffect = Integer.parseInt(textFieldEffectCA.getText());
                            } catch (NumberFormatException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Gli effetti sulla vita, sul carico e sulla CA devono essere dei numeri interi"));
                                return null;
                            }
                            try {
                                lifeEffectPerc = Double.parseDouble(textFieldEffectLifePerc.getText());
                                if (lifeEffectPerc <= -100) throw new NumberFormatException();
                            } catch (NumberFormatException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "L'effetto percentuale sulla vita deve essere un numero intero o decimale (decimale con punto) maggiore di -100"));
                                return null;
                            }
                            try {
                                loadEffectPerc = Double.parseDouble(textFieldEffectLoadPerc.getText());
                                if (loadEffectPerc < -100) throw new NumberFormatException();
                            } catch (NumberFormatException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "L'effetto percentuale sul carico deve essere un numero intero o decimale (decimale con punto) maggiore o uguale a di -100"));
                                return null;
                            }
                            String otherEffects = textAreaOtherEffects.getText();
                            if (effect == null) {
                                if (Effect.checkIfExist(textFieldName.getText())) {
                                    Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Esiste gia' qualcosa con questo nome registrato!"));
                                    return null;
                                }
                                effect = new Effect(
                                        null,
                                        textFieldName.getText(),
                                        duration,
                                        intensity,
                                        isTreatable,
                                        isCurable,
                                        isLethal,
                                        lifeEffect,
                                        lifeEffectPerc,
                                        caEffect,
                                        loadEffect,
                                        loadEffectPerc,
                                        otherEffects,
                                        textAreaDescription.getText(),
                                        checkBoxIsEffectActive.isSelected()
                                );
                            } else {
                                oldName = effect.getName();
                                effect = new Effect(
                                        effect.getId(),
                                        textFieldName.getText(),
                                        duration,
                                        intensity,
                                        isTreatable,
                                        isCurable,
                                        isLethal,
                                        lifeEffect,
                                        lifeEffectPerc,
                                        caEffect,
                                        loadEffect,
                                        loadEffectPerc,
                                        otherEffects,
                                        textAreaDescription.getText(),
                                        checkBoxIsEffectActive.isSelected()
                                );
                            }

                            effect.saveIntoDatabase(oldName);
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Salvataggio dei Dati", "Salvataggio dei dati completato con successo!"));
                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Salvataggio", "Si e' verificato un errore durante il salvataggio dei dati");
                                textFieldName.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    @FXML
    private void backToSheet() {
        textFieldName.getScene().getWindow().hide();
    }

    // Methods
    private void initExistingEffect(@NotNull final String name) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            effect = new Effect(name);
                            Platform.runLater(() -> {
                                textFieldName.setText(effect.getName());
                                comboBoxIsTreatable.getSelectionModel().select(effect.getIsTreatable().getDatabaseValue()+1);
                                comboBoxIsCurable.getSelectionModel().select(effect.getIsCurable().getDatabaseValue()+1);
                                comboBoxIsLethal.getSelectionModel().select(effect.getIsLethal().getDatabaseValue()+1);
                                textFieldDuration.setText(effect.getDuration());
                                spinnerIntensity.getValueFactory().setValue(effect.getIntensity());
                                textFieldEffectLife.setText(String.valueOf(effect.getLifeEffect()));
                                textFieldEffectLoad.setText(String.valueOf(effect.getLoadEffect()));
                                textFieldEffectCA.setText(String.valueOf(effect.getCaEffect()));
                                textFieldEffectLifePerc.setText(String.valueOf(effect.getLifePercentageEffect()));
                                textFieldEffectLoadPerc.setText(String.valueOf(effect.getLoadPercentageEffect()));
                                textAreaOtherEffects.setText(effect.getOtherEffects());
                                textAreaDescription.setText(effect.getDescription());
                                checkBoxIsEffectActive.setSelected(effect.isActive());
                            });
                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'effetto dal database");
                                textFieldName.getScene().getWindow().hide();
                            });
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
}
